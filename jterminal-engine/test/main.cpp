#include <iostream>
#include "../include/terminal.h"
#include <conio.h>
#include <Windows.h>
#include <stdio.h>

using namespace std;
using namespace jterminal;

int main() {
  Terminal::create();
  Terminal::setFlags(FLAG_ENHANCED_INPUT | FLAG_MOUSE_INPUT);
  Terminal::Window::setCursor({5, 1});
  Terminal::Window::setTitle("Test");
  pos_t pos;
  bool state = Terminal::Window::requestCursorPosition(&pos);
  std::cout << state << ": " << pos.x << " " << pos.y << endl;
  while(1) {


  }
}