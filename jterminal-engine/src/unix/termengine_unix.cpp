#ifdef TERMINAL_UNIX
#include "../../include/terminal.h"
#include <termios.h>
#include <unistd.h>
#include <sys/signal.h>
#include <bits/stdc++.h>
#include <sys/ioctl.h>

namespace jterminal {

dim_t window_event_old_dim_;

void TermEngine::threadInputLoop() {
  uint8_t buf[INPUT_BUFFER_SIZE + 1];
  std::unique_lock lock(input_thread_mutex_);
  while(!closed_) {
    while(!(flags_memory_ & FLAG_EXTENDED_INPUT) || instance_ == nullptr) {
      input_thread_cv_.wait(lock);
    }
    size_t len = read(STDIN_FILENO, buf, INPUT_BUFFER_SIZE);
    Terminal* instance = instance_;
    if(instance == nullptr) {
      continue;
    }
    instance->writeInput(buf, len);
  }
}

void TermEngine::signalWindowInput(int) {
  if(!(flags_memory_ & FLAG_WINDOW_INPUT) || instance_ == nullptr) {
    return;
  }
  ESCBuffer esc_buffer(24);
  dim_t current_dim;
  readConsoleDim(&current_dim);
  Terminal* instance = instance_;
  if(instance == nullptr) {
    return;
  }
  if(current_dim != window_event_old_dim_) {
    esc_buffer.reset();
    CSISequence sequence = CSI_SEQUENCE_PRIVATE('=','W',current_dim.width, current_dim.height,
                                                window_event_old_dim_.width, window_event_old_dim_.height);
    esc_buffer.writeSequence(sequence);
    window_event_old_dim_ = current_dim;
    instance->writeInput(esc_buffer.ptr(), esc_buffer.cursor());
  }
}

void TermEngine::signalSigInt(int signum) {
  shutdown();
  exit(signum);
}

void TermEngine::signalSigQuit(int signum) {
  shutdown();
  exit(signum);
}

void TermEngine::signalSigTstp(int signum) {
  shutdown();
  exit(signum);
}


void TermEngine::create(Settings settings) {
  settings_ = settings;
  enabled_ = true;
  input_thread_ = new std::thread(threadInputLoop);
  readConsoleDim(&window_event_old_dim_);
  signal(SIGWINCH, signalWindowInput);
  signal(SIGINT, signalSigInt);
  signal(SIGTSTP, signalSigTstp);
  signal(SIGQUIT, signalSigQuit);
}

void TermEngine::set(Terminal *instance) {
  instance_ = instance;
  if(instance == nullptr) {
    resetAll();
    return;
  }
  instance->update();
  instance->window_->setup();
}

void TermEngine::get(Terminal *instance) {
  *instance = *instance_;
}

void TermEngine::write(const char* cstr) {
  ::write(STDOUT_FILENO, cstr, strlen(cstr));
}

void TermEngine::write(void* bytes, size_t len) {
  ::write(STDOUT_FILENO, bytes, len);
}

void TermEngine::shutdown() {
  closed_ = true;
  resetAll();
  input_thread_->detach();
  char newline = '\n';
  write(&newline, 1);
}

void TermEngine::resetAll() {
  input_thread_cv_.notify_all();
  write("\033[0m");
  writeFlags(FLAG_DEFAULT, CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING);
  setBufferChannel(BUFFER_MAIN);
}

void TermEngine::writeFlags(uint8_t flags, uint8_t cursor_flags) {
  flags_memory_ = flags;
  termios attributes{};
  tcgetattr(STDIN_FILENO, &attributes);
  tcflag_t local_mode = attributes.c_lflag;
  if(flags & FLAG_SIGNAL_INPUT) {
    local_mode |= ISIG;
  } else {
    local_mode &= ~ISIG;
  }
  if(flags & FLAG_ECHO) {
    local_mode |= ECHO;
  } else {
    local_mode &= ~ECHO;
  }
  if(flags & FLAG_LINE_INPUT) {
    local_mode |= ICANON;
  } else {
    local_mode &= ~ICANON;
  }
  attributes.c_lflag = local_mode;
  attributes.c_cc[VMIN] = 1;
  attributes.c_cc[VTIME] = 1;
  tcsetattr(STDIN_FILENO, TCSANOW, &attributes);
  strstream buf;
  buf << (cursor_flags & CURSOR_FLAG_VISIBLE ? ESC_ENABLE_CURSOR : ESC_DISABLE_CURSOR);
  buf << (cursor_flags & CURSOR_FLAG_BLINKING
             ? ESC_ENABLE_CURSOR_BLINKING : ESC_DISABLE_CURSOR_BLINKING);
  buf << (flags & FLAG_MOUSE_EXTENDED_INPUT ? ESC_ENABLE_MOUSE_EXTENDED :
             flags & FLAG_MOUSE_INPUT ? ESC_ENABLE_MOUSE : ESC_DISABLE_MOUSE);
  write(buf.str().c_str());
  input_thread_cv_.notify_all();
}

void TermEngine::setBufferChannel(uint8_t buf_ch) {
  if(buffer_channel_ != buf_ch) {
    buffer_channel_ = buf_ch;
    if(buf_ch) {
      write(ESC_ENABLE_ALT_BUFFER);
    } else {
      write(ESC_DISABLE_ALT_BUFFER);
    }
  }
}

bool TermEngine::isEnabled() {
  return enabled_;
}

bool TermEngine::isClosed() {
  return closed_;
}

bool TermEngine::isActive(Terminal* instance) {
  return instance_ == instance;
}

void TermEngine::readConsoleDim(dim_t* dim) {
  winsize size{};
  ioctl(STDOUT_FILENO, TIOCGWINSZ, &size);
  dim->width = size.ws_col;
  dim->height = size.ws_row;
}

Settings TermEngine::getSettings() {
  return settings_;
}

void TermEngine::waitForClose() {
  input_thread_->join();
}

}

#endif

