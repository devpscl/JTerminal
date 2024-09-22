#include "../include/AbstractNativeTerminal.h"

#include <cstring>

#include "../../include/terminal.h"

#define TO_INSTANCE(ptr) reinterpret_cast<TerminalPtr>(ptr)
#define TO_WINDOW(ptr) reinterpret_cast<WindowPtr>(ptr)
#define TO_INPUT_STREAM(ptr) reinterpret_cast<InputStreamPtr>(ptr)

using namespace jterminal;

void Java_net_jterminal_instance_AbstractNativeTerminal__1create(JNIEnv* env, jobject, jbyte mode, jint buffer) {
  Settings settings;
  settings.mode = mode;
  settings.input_buffer_size = buffer;
  TermEngine::create(settings);
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1set(JNIEnv* env, jobject, jlong instance_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  TermEngine::set(terminal);
}

jboolean Java_net_jterminal_instance_AbstractNativeTerminal__1isActive(JNIEnv* env, jobject, jlong instance_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  return TermEngine::isActive(terminal);
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1shutdown(JNIEnv* env, jobject) {
  TermEngine::shutdown();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1resetAll(JNIEnv* env, jobject) {
  TermEngine::resetAll();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setBuffer(JNIEnv* env, jobject, jbyte channel) {
  TermEngine::setBufferChannel(channel);
}

jboolean Java_net_jterminal_instance_AbstractNativeTerminal__1isEnabled(JNIEnv* env, jobject) {
  return TermEngine::isEnabled();
}

jboolean Java_net_jterminal_instance_AbstractNativeTerminal_isAlive(JNIEnv* env, jobject) {
  return !TermEngine::isClosed() && TermEngine::isEnabled();
}

jlong Java_net_jterminal_instance_AbstractNativeTerminal__1construct(JNIEnv* env, jobject) {
  TerminalPtr terminal = new Terminal();
  return reinterpret_cast<jlong>(terminal);
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1destruct(JNIEnv* env, jobject, jlong instance_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  if(terminal->isActive()) {
    TermEngine::set(nullptr);
  }
  delete terminal;
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setFlags(JNIEnv* env, jobject,
  jlong instance_ptr, jbyte flags) {
  auto terminal = TO_INSTANCE(instance_ptr);
  terminal->setFlags(flags);
}

jbyte Java_net_jterminal_instance_AbstractNativeTerminal__1getFlags(JNIEnv* env, jobject, jlong instance_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  uint8_t value;
  terminal->getFlags(&value);
  return value;
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1clear(JNIEnv* env, jobject, jlong instance_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  terminal->clear();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1update(JNIEnv* env, jobject, jlong instance_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  terminal->update();
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1reset(JNIEnv* env, jobject,
  jlong instance_ptr, jboolean state) {
  auto terminal = TO_INSTANCE(instance_ptr);
  terminal->reset(state);
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1beep(JNIEnv* env, jobject, jlong instance_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  terminal->beep();
}

jlong Java_net_jterminal_instance_AbstractNativeTerminal__1createInputStream(JNIEnv* env, jobject,
  jlong instance_ptr, jint capacity) {
  auto terminal = TO_INSTANCE(instance_ptr);

  auto result = terminal->newInputStream(InputStreamPriority::Normal, capacity);
  return reinterpret_cast<jlong>(result);
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1disposeInputStream(JNIEnv* env, jobject,
  jlong instance_ptr, jlong is_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  auto input_stream = TO_INPUT_STREAM(is_ptr);
  terminal->disposeInputStream(input_stream);
}

jlong Java_net_jterminal_instance_AbstractNativeTerminal__1getWindowPointer(JNIEnv* env, jobject, jlong instance_ptr) {
  auto terminal = TO_INSTANCE(instance_ptr);
  return reinterpret_cast<jlong>(terminal->getWindow());
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setTitle(JNIEnv* env, jobject,
  jlong window_ptr, jbyteArray arr) {
  auto window = TO_WINDOW(window_ptr);
  size_t length = env->GetArrayLength(arr);
  char buf[length + 1];
  jbyte* bytes = env->GetByteArrayElements(arr, nullptr);
  for(size_t idx = 0; idx < length; idx++) {
    buf[idx] = bytes[idx];
  }
  buf[length] = '\0';
  window->setTitle(buf);
}

jbyteArray Java_net_jterminal_instance_AbstractNativeTerminal__1getTitle(JNIEnv* env, jobject, jlong window_ptr) {
  auto window = TO_WINDOW(window_ptr);
  const auto & str = window->getTitle();
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

void Java_net_jterminal_instance_AbstractNativeTerminal__1setDim(JNIEnv* env, jobject, jlong window_ptr,
  jint width, jint height) {
  auto window = TO_WINDOW(window_ptr);
  Dim dim(width, height);
  window->setDimension(dim);
}

jint Java_net_jterminal_instance_AbstractNativeTerminal__1getDim(JNIEnv* env, jobject, jlong window_ptr) {
  auto window = TO_WINDOW(window_ptr);
  Dim dim;
  window->getDimension(&dim);
  return dim.height << 16 | dim.width;
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setCursor(JNIEnv* env, jobject, jlong window_ptr,
  jint x, jint y) {
  auto window = TO_WINDOW(window_ptr);
  Pos pos(x, y);
  window->setCursor(pos);
}

jint Java_net_jterminal_instance_AbstractNativeTerminal__1requestCursor(JNIEnv* env, jobject, jlong window_ptr) {
  auto window = TO_WINDOW(window_ptr);
  Pos pos;
  if(window->requestCursorPosition(&pos)) {
    return pos.y << 16 | pos.x;
  }
  return -1;
}

void Java_net_jterminal_instance_AbstractNativeTerminal__1setCursorFlags(JNIEnv* env, jobject,
  jlong window_ptr, jbyte flags) {
  auto window = TO_WINDOW(window_ptr);
  window->setCursorFlags(flags);
}

jbyte Java_net_jterminal_instance_AbstractNativeTerminal__1getCursorFlags(JNIEnv* env, jobject, jlong window_ptr) {
  auto window = TO_WINDOW(window_ptr);
  return window->getCursorFlags();
}

