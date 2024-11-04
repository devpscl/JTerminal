#ifdef TERMINAL_UNIX
#include "../../include/terminal.h"
#include <termios.h>
#include <unistd.h>
#include <sys/signal.h>
#include <bits/stdc++.h>
#include <fcntl.h>
#include <poll.h>

namespace jterminal {

dim_t window_event_old_dim_;

void Terminal::signalWindowInput(int) {
  if(!(flags_ & FLAG_WINDOW_INPUT)) {
    return;
  }
  ESCBuffer esc_buffer(32);
  dim_t current_dim;
  Window::getDimension(&current_dim);
  if(current_dim != window_event_old_dim_) {
    if(window_event_old_dim_.width == 0 && window_event_old_dim_.height == 0) {
      window_event_old_dim_ = current_dim;
      return;
    }
    esc_buffer.reset();
    CSISequence sequence = CSI_SEQUENCE_PRIVATE('=','W',current_dim.width, current_dim.height,
                                                window_event_old_dim_.width, window_event_old_dim_.height);
    esc_buffer.writeSequence(sequence);
    window_event_old_dim_ = current_dim;
    writeInput(esc_buffer.ptr(), esc_buffer.cursor());
  }
}

void Terminal::threadInputLoop() {
  uint8_t buf[INPUT_BUFFER_SIZE + 1];
  std::unique_lock lock(input_thread_mutex_);
  pollfd fds[1];
  fds[0].fd = STDIN_FILENO;
  fds[0].events = POLLIN;
  int result;
  while(!closed_) {
    while(flags_ & FLAG_LINE_INPUT || !(flags_ & FLAG_EXTENDED_INPUT)) {
      input_thread_cv_.wait(lock);
    }
    result = poll(fds, STDIN_FILENO + 1, 100);
    if(result <= 0) {
      continue;
    }
    if(flags_ & FLAG_LINE_INPUT || !(flags_ & FLAG_EXTENDED_INPUT)) {
      continue;
    }
    size_t len = read(STDIN_FILENO, buf, INPUT_BUFFER_SIZE);
    if(len <= 0) {
      continue;
    }
    writeInput(buf, len);
  }
}

void Terminal::writeFlags(uint8_t flags, uint8_t cursor_flags) {
  flags_ = flags;
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

void Terminal::signalSigInt(int signum) {
  shutdown();
  exit(signum);
}

void Terminal::signalSigQuit(int signum) {
  shutdown();
  exit(signum);
}

void Terminal::signalSigTstp(int signum) {
  shutdown();
  exit(signum);
}

void Terminal::setNonblocking(bool non_block) {
  int flags = fcntl(STDIN_FILENO, F_GETFL, 0);
  if(non_block) {
    flags |= O_NONBLOCK;
  } else {
    flags &= ~O_NONBLOCK;
  }
  fcntl(STDIN_FILENO, F_SETFL, flags);
}


void Terminal::create(Settings settings) {
  settings_ = settings;
  enabled_ = true;
  input_thread_ = new std::thread(threadInputLoop);
  Window::getDimension(&window_event_old_dim_);
  signal(SIGWINCH, signalWindowInput);
  signal(SIGINT, signalSigInt);
  signal(SIGTSTP, signalSigTstp);
  signal(SIGQUIT, signalSigQuit);
}

void Terminal::write(const char* cstr) {
  ::write(STDOUT_FILENO, cstr, strlen(cstr));
}

void Terminal::write(void* bytes, size_t len) {
  ::write(STDOUT_FILENO, bytes, len);
}

void Terminal::shutdown() {
  if(closed_) {
    return;
  }
  closed_ = true;
  resetAll();
  input_thread_->detach();
  char newline = '\n';
  write(&newline, 1);
}

void Terminal::resetAll() {
  input_thread_cv_.notify_all();
  write("\033[0m");
  writeFlags(FLAG_DEFAULT, CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING);
  setBuffer(BUFFER_MAIN);
}

void Terminal::clear() {
  system("clear");
  update();
}

size_t Terminal::readLine(void* bytes, size_t len) {
  if(!(flags_ & FLAG_LINE_INPUT)) {
    return 0;
  }
  return read(STDIN_FILENO, bytes, len);
}

}

#endif

