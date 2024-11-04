#include "../include/NativeTerminalInput.h"
#include "../../include/terminal.h"
#include "../include/jni_helper.h"

#define TO_INPUT_STREAM(ptr) reinterpret_cast<InputStreamPtr>(ptr)

using namespace jterminal;

jlong Java_net_jterminal_input_NativeTerminalInput__1createInputStream(JNIEnv*, jobject, jint capacity) {
  InputStreamPtr ptr = Terminal::newInputStream(InputStreamPriority::Normal, capacity);
  return reinterpret_cast<jlong>(ptr);
}

void Java_net_jterminal_input_NativeTerminalInput__1disposeInputStream(JNIEnv*, jobject, jlong ptr) {
  auto input_stream_ptr = TO_INPUT_STREAM(ptr);
  Terminal::disposeInputStream(input_stream_ptr);
}

jint Java_net_jterminal_input_NativeTerminalInput__1read__J(JNIEnv* env, jobject, jlong ptr) {
  if(ptr == 0) {
    return 0;
  }
  auto input_stream = TO_INPUT_STREAM(ptr);
  return input_stream->read();
}

jint Java_net_jterminal_input_NativeTerminalInput__1avail(JNIEnv*, jobject, jlong ptr) {
  if(ptr == 0) {
    return 0;
  }
  auto input_stream = TO_INPUT_STREAM(ptr);
  return input_stream->available();
}

jint Java_net_jterminal_input_NativeTerminalInput__1peek(JNIEnv* env, jobject, jlong ptr,
  jbyteArray b_array, jint off, jint len) {
  if(ptr == 0) {
    return 0;
  }
  off = MATH_MIN(off, len);
  auto input_stream = TO_INPUT_STREAM(ptr);
  size_t arr_len = env->GetArrayLength(b_array);
  size_t buf_len = MATH_MIN(len, arr_len) - off;
  jbyte* arr = env->GetByteArrayElements(b_array, nullptr);
  char buf[buf_len];
  size_t ret = input_stream->peek(buf, buf_len);
  for (size_t idx = 0; idx < buf_len; idx++) {
    arr[idx + off] = buf[idx];
  }
  env->SetByteArrayRegion(b_array, 0, arr_len, arr);
  return ret;
}

jint Java_net_jterminal_input_NativeTerminalInput__1read__J_3BIIJ(JNIEnv* env, jobject, jlong ptr,
  jbyteArray b_array, jint off, jint len, jlong timeout) {
  if(ptr == 0) {
    return 0;
  }
  off = MATH_MIN(off, len);
  auto input_stream = TO_INPUT_STREAM(ptr);
  size_t arr_len = env->GetArrayLength(b_array);
  size_t buf_len = MATH_MIN(len, arr_len) - off;
  jbyte* arr = env->GetByteArrayElements(b_array, nullptr);
  char buf[buf_len];
  size_t ret;
  if(timeout == -1) {
    ret = input_stream->read(buf, buf_len);
  } else {
    ret = input_stream->read(buf, buf_len, std::chrono::milliseconds(timeout));
  }
  for (size_t idx = 0; idx < buf_len; idx++) {
    arr[idx + off] = buf[idx];
  }
  env->SetByteArrayRegion(b_array, 0, arr_len, arr);
  return ret;
}

jint Java_net_jterminal_input_NativeTerminalInput__1peekInput(JNIEnv* env, jobject, jlong ptr, jobject ref) {
  if(ptr == 0) {
    return 0;
  }
  auto input_stream = TO_INPUT_STREAM(ptr);
  InputEvent input_event;
  size_t ret = input_stream->peekInput(&input_event);
  JReference reference(ref);
  jobject obj = convert(env, input_event);
  reference.set(env, obj);
  return ret;
}

jobject Java_net_jterminal_input_NativeTerminalInput__1readInput(JNIEnv* env, jobject, jlong ptr, jlong timeout) {
  if(ptr == 0) {
    return nullptr;
  }
  auto input_stream = TO_INPUT_STREAM(ptr);
  InputEvent input_event;
  if(timeout == -1) {
    input_stream->readInput(&input_event);
  } else {
    if(!input_stream->readInput(&input_event, std::chrono::milliseconds(timeout))) {
      return nullptr;
    }
  }
  return convert(env, input_event);
}

void Java_net_jterminal_input_NativeTerminalInput__1reset(JNIEnv* env, jobject, jlong ptr) {
  if(ptr == 0) {
    return;
  }
  auto input_stream = TO_INPUT_STREAM(ptr);
  input_stream->reset();
}




