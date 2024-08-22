
#ifdef TERMINAL_UNIX

#include "../include/terminal.h"
#include <bits/stdc++.h>
#include <sys/ioctl.h>
#include <sstream>
#include <termios.h>
#include <unistd.h>
#include <sys/signal.h>

namespace jterminal {

using strstream = std::stringstream;

std::string window_title_ = TERMINAL_DEFAULT_TITLE;
dim_t window_event_old_dim_;

void Terminal::signalWindowEvent(int) {
  if(!(flags_ & FLAG_WINDOW_INPUT)) {
    return;
  }
  ESCBuffer esc_buffer(24);
  dim_t current_dim;
  Window::getDimension(&current_dim);
  if(current_dim == window_event_old_dim_) {
    return;
  }
  esc_buffer.reset();
  CSISequence sequence = CSI_SEQUENCE_PRIVATE('=','W',current_dim.width,
                                              current_dim.height,
                                              window_event_old_dim_.width,
                                              window_event_old_dim_.height);
  esc_buffer.writeSequence(sequence);
  window_event_old_dim_ = current_dim;
  sendInput(esc_buffer.ptr(), esc_buffer.cursor());
}

void Terminal::sendInput(uint8_t *bytes, size_t len) {
  if(pipelines_.empty()) {
    return;
  }
  for (auto &pl : pipelines_) {
    if(pl == nullptr) {
      continue;
    }
    pl->write(bytes, len);
    if(pl->priority_ == INPUT_PRIO_HIGHEST_SINGLETON) {
      break;
    }
  }
}

void Terminal::threadRead() {
  uint8_t buf[INPUT_BUFFER_SIZE + 1];
  std::unique_lock<std::mutex> lock(input_thread_mutex_);
  while(!disposed_) {
    while(!(flags_ & FLAG_EXTENDED_INPUT)) {
      input_thread_cv_.wait(lock);
    }
    size_t len = ::read(STDIN_FILENO, buf, INPUT_BUFFER_SIZE);
    uint8_t data[len];
    for(size_t idx = 0; idx < len; idx++) {
      data[idx] = buf[idx];
    }
    sendInput(data, len);
  }
}

void Terminal::create(Settings settings) {
  main_pipeline_ = new InputPipeline(INPUT_PRIO_HIGH);
  pipelines_.push_back(main_pipeline_);
  settings_ = settings;
  flags_ = FLAG_DEFAULT;
  Window::getDimension(&window_event_old_dim_);
  update();
  input_thread_ = new std::thread(threadRead);
  signal(SIGWINCH, signalWindowEvent);
  enabled_ = true;
}

void Terminal::dispose() {
  disposed_ = true;
  reset(false);
  input_thread_->detach();
}

bool Terminal::isValid() {
  return !disposed_ && enabled_;
}

bool Terminal::isDisposed() {
  return disposed_;
}

void Terminal::attachInputPipeline(InputPipeline *input_pipeline) {
  input_pipeline->reset();
  if(pipelines_.size() >= MAXIMUM_PIPELINE_COUNT) {
    return;
  }
  pipelines_.push_back(input_pipeline);
  std::sort(pipelines_.begin(), pipelines_.end(),
            [](InputPipeline* a, InputPipeline* b) {
              if(a == nullptr || b == nullptr) {
                return false;
              }
              return a->priority_ > b->priority_;
            });
}

void Terminal::detachInputPipeline(InputPipeline *input_pipeline) {
  for(uint8_t idx = 0; idx < pipelines_.size(); idx++) {
    if(pipelines_[idx] == input_pipeline) {
      pipelines_.erase(pipelines_.begin() + idx);
      input_pipeline->reset();
      return;
    }
  }
}

void Terminal::setFlags(uint8_t flags) {
  flags_ = flags;
  update();
}

void Terminal::getFlags(uint8_t *flags_ptr) {
  *flags_ptr = flags_;
}

void Terminal::clear() {
  system("clear");
  update();
}

void Terminal::update() {
  termios attributes{};
  tcgetattr(STDIN_FILENO, &attributes);
  tcflag_t local_mode = attributes.c_lflag;
  if(flags_ & FLAG_SIGNAL_INPUT) {
    local_mode |= ISIG;
  } else {
    local_mode &= ~ISIG;
  }
  if(flags_ & FLAG_ECHO) {
    local_mode |= ECHO;
  } else {
    local_mode &= ~ECHO;
  }
  if(flags_ & FLAG_LINE_INPUT) {
    local_mode |= ICANON;
  } else {
    local_mode &= ~ICANON;
  }
  attributes.c_lflag = local_mode;
  attributes.c_cc[VMIN] = 1;
  attributes.c_cc[VTIME] = 1;
  tcsetattr(STDIN_FILENO, TCSANOW, &attributes);
  strstream buf;
  buf << (cursor_flags_ & CURSOR_FLAG_VISIBLE ? ESC_ENABLE_CURSOR : ESC_DISABLE_CURSOR);
  buf << (cursor_flags_ & CURSOR_FLAG_BLINKING
             ? ESC_ENABLE_CURSOR_BLINKING : ESC_DISABLE_CURSOR_BLINKING);
  buf << (flags_ & FLAG_MOUSE_EXTENDED_INPUT ? ESC_ENABLE_MOUSE_EXTENDED :
             ((flags_ & FLAG_MOUSE_INPUT) ? ESC_ENABLE_MOUSE : ESC_DISABLE_MOUSE));
  buf << ESC_TITLE_START << window_title_ << ESC_TITLE_END;
  write(buf.str().c_str());
}

void Terminal::reset(bool clear_screen) {
  flags_ = FLAG_DEFAULT;
  cursor_flags_ = CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING;
  input_thread_cv_.notify_all();
  write("\033[0m");
  update();
  if(clear_screen) {
    clear();
  }
}

void Terminal::write(uint8_t *bytes, size_t len) {
  char* arr = reinterpret_cast<char*>(bytes);
  std::cout.write(const_cast<const char*>(arr), len);
}

void Terminal::write(const char *cstr) {
  ::write(STDOUT_FILENO, cstr, strlen(cstr));
}

void Terminal::beep() {
  write("\a");
}

size_t Terminal::read(uint8_t *bytes, size_t size) {
  return main_pipeline_->read(bytes, size);
}

void Terminal::readInput(InputEvent *input_event) {
  main_pipeline_->readInput(input_event);
}

void Terminal::Window::setTitle(const char *cstr) {
  if(cstr == nullptr) {
    cstr = TERMINAL_DEFAULT_TITLE;
  }
  window_title_ = std::string(cstr);
  update();
}

std::string Terminal::Window::getTitle() {
  return window_title_;
}

void Terminal::Window::setDimension(const dim_t &dim) {
  winsize size{};
  size.ws_col = dim.width;
  size.ws_row = dim.height;
  ioctl(STDOUT_FILENO, TIOCSWINSZ, &size);
  CSISequence sequence = CSI_SEQUENCE('t', 8, dim.height, dim.width);
  ESCBuffer buffer(16);
  buffer.writeSequence(sequence);
  write(buffer.ptr(), buffer.size());
}

void Terminal::Window::setCursor(const pos_t &pos) {
  CSISequence sequence = CSI_SEQUENCE('f', 8, pos.y, pos.x);
  ESCBuffer buffer(16);
  buffer.writeSequence(sequence);
  write(buffer.ptr(), buffer.size());
}

void Terminal::Window::getDimension(dim_t *dim_ptr) {
  winsize size{};
  ioctl(STDOUT_FILENO, TIOCGWINSZ, &size);
  dim_ptr->width = size.ws_col;
  dim_ptr->height = size.ws_row;
}

bool Terminal::Window::requestCursorPosition(pos_t *pos_ptr) {
  InputPipeline pipeline(INPUT_PRIO_HIGHEST_SINGLETON, 24);
  uint8_t buf[8];
  attachInputPipeline(&pipeline);
  write(ESC_CURSOR_REQUEST);
  size_t len = pipeline.read(buf, 8, std::chrono::milliseconds(
      settings_.mode == TERMINAL_MODE_PERFORMANCE ? 10 : 500));
  if(len == -1 || len == 0) {
    detachInputPipeline(&pipeline);
    return false;
  }
  ESCBuffer buffer(buf, len);
  if(!buffer.skipToNextSequence()) {
    detachInputPipeline(&pipeline);
    return false;
  }
  CSISequenceString sequence_string;
  if(!buffer.readSequence(&sequence_string)) {
    detachInputPipeline(&pipeline);
    return false;
  }
  if(sequence_string.endSymbol() == 'R' && sequence_string.paramCount() == 2) {
    detachInputPipeline(&pipeline);
    pos_ptr->x = sequence_string[1];
    pos_ptr->y = sequence_string[0];
    return true;
  }
  detachInputPipeline(&pipeline);
  return false;
}

void Terminal::Window::setCursorFlags(uint8_t flags) {
  cursor_flags_ = flags;
  update();
}

uint8_t Terminal::Window::getCursorFlags() {
  return cursor_flags_;
}

}
#endif