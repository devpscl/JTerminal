#ifndef JNI_HELPER_H
#define JNI_HELPER_H
#define PGK_UTIL_REF        "net/jterminal/util/Ref"
#define PGK_INPUT_KEYBOARD  "net/jterminal/input/KeyboardInputEvent"
#define PGK_INPUT_MOUSE     "net/jterminal/input/MouseInputEvent"
#define PGK_INPUT_WINDOW    "net/jterminal/input/WindowInputEvent"
#define PGK_INPUT_EVENT     "net/jterminal/input/InputEvent"

#include <jni.h>
#include <cstring>

#include "../../include/terminal.h"

jobject convert(JNIEnv* env, jterminal::InputEvent& event) {
  std::string raw_str = event.raw;
  std::size_t raw_len = raw_str.length();
  const char* raw_cstr = raw_str.c_str();

  jbyte raw_bytes[raw_len];
  memcpy(raw_bytes, raw_cstr, raw_len);
  jbyteArray raw_byte_array = env->NewByteArray(raw_len);
  env->SetByteArrayRegion(raw_byte_array, 0, raw_len, raw_bytes);

  jobject ret = nullptr;
  if(event.type == jterminal::InputType::Keyboard) {
    jchar input = event.keyboard.wide_char;
    jint key = event.keyboard.key;
    jbyte state = event.keyboard.state;
    jclass clazz = env->FindClass(PGK_INPUT_KEYBOARD);
    jmethodID method = env->GetMethodID(clazz, "<init>", "([BCIB)V");
    ret = env->NewObject(clazz, method, raw_byte_array, input, key, state);
  }
  else if(event.type == jterminal::InputType::Mouse) {
    jbyte button = event.mouse.button;
    jbyte action = event.mouse.action;
    jint x = event.mouse.position.x;
    jint y = event.mouse.position.y;
    jclass clazz = env->FindClass(PGK_INPUT_MOUSE);
    jmethodID method = env->GetMethodID(clazz, "<init>", "([BBBII)V");
    ret = env->NewObject(clazz, method, raw_byte_array, button, action, x, y);
  }
  else if(event.type == jterminal::InputType::Window) {
    jint old_width = event.window.old_size.width;
    jint old_height = event.window.old_size.height;
    jint new_width = event.window.new_size.width;
    jint new_height = event.window.new_size.height;
    jclass clazz = env->FindClass(PGK_INPUT_WINDOW);
    jmethodID method = env->GetMethodID(clazz, "<init>", "([BIIII)V");
    ret = env->NewObject(clazz, method, raw_byte_array, old_width, old_height, new_width, new_height);
  }
  else {
    jclass clazz = env->FindClass(PGK_INPUT_EVENT);
    jmethodID method = env->GetMethodID(clazz, "<init>", "([B)V");
    ret = env->NewObject(clazz, method, raw_byte_array);
  }
  env->DeleteLocalRef(raw_byte_array);
  return ret;
}

class JReference {
  jobject source;

public:
  explicit JReference(jobject source) {
    this->source = source;
  }

  void set(JNIEnv* env, jobject value) {
    jclass clazz = env->FindClass(PGK_UTIL_REF);
    jmethodID method = env->GetMethodID(clazz, "set", "(Ljava/lang/Object;)V");
    env->CallVoidMethod(source, method, value);
  }

  jobject get(JNIEnv* env) {
    jclass clazz = env->FindClass(PGK_UTIL_REF);
    jmethodID method = env->GetMethodID(clazz, "get", "()Ljava/lang/Object;");
    return env->CallObjectMethod(source, method);
  }

};

#endif //JNI_HELPER_H
