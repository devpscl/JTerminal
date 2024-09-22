#ifdef TERMINAL_WIN
#include "../../include/terminal.h"
#include <Windows.h>
#include <conio.h>

namespace jterminal {

void Terminal::clear() {
  if(!isActive()) {
    return;
  }
  system("cls");
  update();
}

}
#endif