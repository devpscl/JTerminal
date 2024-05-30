#include <iostream>
#include "../include/bufnio.h"

using namespace std;
using namespace jterminal;

int main() {
  StringBuffer buf(14);
  buf.writeString("Hello ");
  buf.write(' ');
  buf.writeString("World!");
  buf.erase();
  std::cout << buf.str() << "; " << buf;

  return 0;
}
