#include "../include/AbstractNativeTerminal.h"
#include <cstring>
#include "../../include/terminal.h"

#define TO_INPUT_STREAM(ptr) reinterpret_cast<InputStreamPtr>(ptr)

using namespace jterminal;

void Java_net_jterminal_instance_AbstractNativeTerminal__1create(JNIEnv*, jobject, jbyte mode, jint buffer) {
  Settings settings;
  settings.mode = mode;
  settings.input_buffer_size = buffer;
  Terminal::create(settings);
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1shutdown(JNIEnv*, jobject) {
  Terminal::shutdown();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1joinFutureClose(JNIEnv*, jobject) {
  Terminal::joinFutureClose();
}

jboolean Java_net_jterminal_instance_AbstractNativeTerminal__1isEnabled(JNIEnv*, jobject) {
  return Terminal::isEnabled();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setFlags(JNIEnv*, jobject, jbyte flags) {
  Terminal::setFlags(flags);
}

jbyte Java_net_jterminal_instance_AbstractNativeTerminal__1getFlags(JNIEnv*, jobject) {
  uint8_t flags = 0;
  Terminal::getFlags(&flags);
  return flags;
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1clear(JNIEnv*, jobject) {
  Terminal::clear();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1update(JNIEnv*, jobject) {
  Terminal::update();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1reset(JNIEnv*, jobject, jboolean clear_screen) {
  Terminal::reset(clear_screen);
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1beep(JNIEnv*, jobject) {
  Terminal::beep();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setBuffer(JNIEnv*, jobject, jbyte buffer) {
  Terminal::setBuffer(buffer);
}

jbyte Java_net_jterminal_instance_AbstractNativeTerminal__1getBuffer(JNIEnv*, jobject) {
  return static_cast<jbyte>(Terminal::getBuffer());
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setTitle(JNIEnv* env, jobject, jbyteArray arr) {
  size_t length = env->GetArrayLength(arr);
  char buf[length + 1];
  jbyte* bytes = env->GetByteArrayElements(arr, nullptr);
  for(size_t idx = 0; idx < length; idx++) {
    buf[idx] = bytes[idx];
  }
  buf[length] = '\0';
  Window::setTitle(buf);
}

jbyteArray Java_net_jterminal_instance_AbstractNativeTerminal__1getTitle(JNIEnv* env, jobject) {
  const auto & str = Window::getTitle();
  const size_t length = str.length();
  const char* cstr = str.c_str();
  auto* bytes = new jbyte[length];
  for(size_t idx = 0; idx < length; idx++) {
    bytes[idx] = cstr[idx];
  }
  jbyteArray ret = env->NewByteArray(length);
  env->SetByteArrayRegion(ret, 0, length, bytes);
  return ret;
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setDim(JNIEnv*, jobject, jint width, jint height) {
  Dim dim(width, height);
  Window::setDimension(dim);
}

jint Java_net_jterminal_instance_AbstractNativeTerminal__1getDim(JNIEnv*, jobject) {
  Dim dim;
  Window::getDimension(&dim);
  return dim.height << 16 | dim.width;
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setCursor(JNIEnv*, jobject, jint x, jint y) {
  Pos pos(x, y);
  Window::setCursor(pos);
}

jint Java_net_jterminal_instance_AbstractNativeTerminal__1requestCursor(JNIEnv*, jobject) {
  Pos pos;
  if(Window::requestCursorPosition(&pos)) {
    return pos.y << 16 | pos.x;
  }
  return -1;
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setCursorFlags(JNIEnv*, jobject, jbyte flags) {
  Window::setCursorFlags(flags);
}

jbyte Java_net_jterminal_instance_AbstractNativeTerminal__1getCursorFlags(JNIEnv*, jobject) {
  return static_cast<jbyte>(Window::getCursorFlags());
}
