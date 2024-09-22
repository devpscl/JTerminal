#include <iostream>
#include "../include/terminal.h"

#include <chrono>

using namespace std::chrono;
using namespace std::chrono_literals;
using namespace std;
using namespace jterminal;

void printInputEvent(InputEvent* input_event) {
  std::stringstream ss;
  ss << "Type: " << static_cast<int>(input_event->type) << "\n";
  std::cout << ss.str();
}

TerminalPtr a;
TerminalPtr b;

TerminalPtr terminalA() {
  auto terminal = new Terminal();
  Window* window = terminal->getWindow();
  terminal->setFlags(FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT | FLAG_SIGNAL_INPUT);
  window->setTitle("Test A");
  return terminal;
}

TerminalPtr terminalB() {
  TerminalPtr terminal = new Terminal();
  Window* window = terminal->getWindow();
  terminal->setFlags(FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT | FLAG_SIGNAL_INPUT | FLAG_MOUSE_INPUT);
  window->setTitle("Test B");
  return terminal;
}

void loopA() {
  auto isp = a->newInputStream(InputStreamPriority::High);
  Window* window = a->getWindow();
  InputEvent event;
  while(1) {
    isp->readInput(&event);
    printInputEvent(&event);
    if(event.keyboard.wide_char == 'c') {
      pos_t p;
      if(window->requestCursorPosition(&p)) {
        strstream ss;
        ss << "Cursor position: " << p.x << ", " << p.y;
        window->setTitle(ss.str().c_str());
      }
    }
    if(event.keyboard.wide_char == 'x') {
      TermEngine::set(b);
    }
  }
}

int ac = 0;

void loopB() {
  auto isp = b->newInputStream(InputStreamPriority::High);
  InputEvent event;
  while(1) {
    isp->readInput(&event);
    std::cout << "Input!";
    strstream ss;
    ss << "Count: " << ac++ << ":";
    b->getWindow()->setTitle(ss.str().c_str());
    if(event.keyboard.wide_char == 'x') {
      TermEngine::set(a);
    }
    std::cout.flush();
  }
}

int main() {
  a = terminalA();
  std::thread t(loopA);
  b = terminalB();
  std::thread t2(loopB);
  TermEngine::create();
  TermEngine::set(a);
  TermEngine::waitForClose();
}


/*
int main() {
  Terminal::create();
  Terminal::setFlags(FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT | FLAG_SIGNAL_INPUT);
  InputEvent buf;
  uint8_t b;
  long long time_watch_ = 0;
  std::cout << "test\n";
  while(1) {
    Terminal::readInput(&buf);
    std::cout << (int)buf.keyboard.state << "\n";
    std::cout << (int)buf.keyboard.key << "\n";
    std::cout << (int)buf.type << "\n";
    std::cout << (int)buf.keyboard.wide_char << "\n";
    if(buf.keyboard.key == KEY_A && buf.type == jterminal::InputType::Keyboard) {
      pos_t p;
      time_watch_ = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch()).count();
      if(Terminal::Window::requestCursorPosition(&p)) {
        long long l = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch()).count() - time_watch_;
        std::cout << "X: " << p.x << ", Y: " << p.y << "\n";
        std::cout << "Time: " << (l) << "ms\n";
      } else {
        std::cout << "Failure!\n";
      }
      continue;
    }
    if(buf.keyboard.key == KEY_ESCAPE) {
      std::cout << "Escape!\n";
      Terminal::dispose();
      break;
    }
  }
}*/

/*

int main() {
  Terminal::create();
  Terminal::setFlags(FLAG_EXTENDED_INPUT | FLAG_MOUSE_INPUT | FLAG_MOUSE_EXTENDED_INPUT);
  InputEvent buf[16];
  uint8_t bbuf[1024];
  while(1) {
    //std::this_thread::sleep_for(100ms);
    //size_t len = Terminal::read(nullptr, buf, 16);
    size_t len = Terminal::read(nullptr, bbuf, 1024);
    //std::cout << len << "\n";
    int x = 0;
    StringBuffer buffer(bbuf, len);
    while(translateInput(buf, &buffer)) {
      x++;
    }
    for (const auto &item : buffer.str()) {
      std::cout << ", " << (int) item;
    }
    std::cout << "\n";
    std::cout << x << "\n";
  }
}
/*
int main() {
  deque<uint8_t> d(100);

  stringbuf buf;
  buf.sputn("Hello World!", 12);
  std::cout << buf.str() << "\n";
  buf.sgetc();
  std::cout << buf.str() << "\n";
  while(1);
}

/*
int main() {
  Terminal::create(TERMINAL_MODE_EFFICIENCY);
  Terminal::Window::setCursorFlags(CURSOR_FLAG_VISIBLE);
  Terminal::setFlags(FLAG_EXTENDED_INPUT | FLAG_MOUSE_INPUT | FLAG_WINDOW_INPUT);
  Terminal::Window::setCursor({5, 1});
  Terminal::Window::setTitle("Test");
  InputPipeline pipeline(INPUT_PRIO_LOW, 1000);
  Terminal::attachInputPipeline(&pipeline);
  uint8_t buf[128];
  while(1) {
    size_t len = pipeline.read(buf, 128);
    if(len == -1) {
      std::cout << "Timeout!\n";
      continue;
    }
    for(size_t idx = 0; idx < len; idx++) {
      std::cout << (int)buf[idx] << " ";
    }
    std::cout << endl;
    for(size_t idx = 1; idx < len; idx++) {
      std::cout << (char)buf[idx] << " ";
    }
    std::cout << endl;
    std::cout << endl;
  }
}*/