#ifdef TERMINAL_UNIX
#include "../../include/terminal.h"

namespace jterminal {

void Terminal::clear() {
  if(!isActive()) {
    return;
  }
  system("clear");
  update();
}

}

#endif

