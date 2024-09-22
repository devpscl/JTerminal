#ifdef TERMINAL_UNIX
#include "../../include/terminal.h"
#include <termios.h>
#include <unistd.h>
#include <sys/signal.h>
#include <bits/stdc++.h>
#include <sys/ioctl.h>

namespace jterminal {

Window::Window(Terminal* instance) {
  instance_ = instance;
}

void Window::setup() {
  strstream buf;
  buf << ESC_TITLE_START << title_ << ESC_TITLE_END;
  TermEngine::write(buf.str().c_str());
}

bool Window::isActive() const {
  return TermEngine::isActive(instance_);
}

void Window::setTitle(const char* cstr) {
  if(cstr == nullptr) {
    cstr = TERMINAL_DEFAULT_TITLE;
  }
  title_.assign(cstr);
  if(!isActive()) {
    return;
  }
  strstream buf;
  buf << ESC_TITLE_START << title_ << ESC_TITLE_END;
  TermEngine::write(buf.str().c_str());
}

std::string Window::getTitle() {
  return title_;
}

void Window::setDimension(const dim_t& dim) {
  if(!isActive()) {
    return;
  }
  winsize size{};
  size.ws_col = dim.width;
  size.ws_row = dim.height;
  ioctl(STDOUT_FILENO, TIOCSWINSZ, &size);
  CSISequence sequence = CSI_SEQUENCE('t', 8, dim.height, dim.width);
  ESCBuffer buffer(16);
  buffer.writeSequence(sequence);
  TermEngine::write(buffer.ptr(), buffer.size());
}

void Window::setCursor(const pos_t& pos) {
  if(!isActive()) {
    return;
  }
  CSISequence sequence = CSI_SEQUENCE('f', 8, pos.y, pos.x);
  ESCBuffer buffer(16);
  buffer.writeSequence(sequence);
  TermEngine::write(buffer.ptr(), buffer.size());
}

void Window::getDimension(dim_t* dim_ptr) {
  TermEngine::readConsoleDim(dim_ptr);
}

bool Window::requestCursorPosition(pos_t* pos_ptr) {
  if(!isActive()) {
    return false;
  }
  uint8_t buf[8];
  InputStreamPtr input_stream = instance_->newSingletonInputStream(24);
  TermEngine::write(ESC_CURSOR_REQUEST);
  Settings settings = TermEngine::getSettings();
  int ms = settings.mode == TERMINAL_MODE_PERFORMANCE ? 10 : 500;
  size_t len = input_stream->read(buf, 8, std::chrono::milliseconds(ms));
  instance_->disposeInputStream(input_stream);

  if(len == -1 || len == 0) {
    return false;
  }
  ESCBuffer buffer(buf, len);
  if(!buffer.skipToNextSequence()) {
    return false;
  }
  CSISequenceString sequence_string;
  if(!buffer.readSequence(&sequence_string)) {
    return false;
  }
  if(sequence_string.endSymbol() == 'R' && sequence_string.paramCount() == 2) {
    pos_ptr->x = sequence_string[1];
    pos_ptr->y = sequence_string[0];
    return true;
  }
  return false;
}

void Window::setCursorFlags(uint8_t flags) {
  cursor_flags_ = flags;
  instance_->update();
}

uint8_t Window::getCursorFlags() {
  return cursor_flags_;
}

}
#endif