#ifdef TERMINAL_WIN
#include "../include/WindowsTerminalImpl.h"
#include <Windows.h>

JNIEXPORT void JNICALL Java_net_jterminal_windows_WindowsTerminalImpl_setWindowVisible
  (JNIEnv* env, jobject, jboolean state) {
  HWND console_window = GetConsoleWindow();
  if(console_window == nullptr) {
    return;
  }
  ShowWindow(console_window, state ? SW_SHOW : SW_HIDE);
}


JNIEXPORT jboolean JNICALL Java_net_jterminal_windows_WindowsTerminalImpl_isWindowVisible
  (JNIEnv* env, jobject) {
  HWND console_window = GetConsoleWindow();
  if(console_window == nullptr) {
    return false;
  }
  return IsWindowVisible(console_window);
}


JNIEXPORT void JNICALL Java_net_jterminal_windows_WindowsTerminalImpl_setGraphicUpdate
  (JNIEnv* env, jobject, jboolean state) {
  HWND console_window = GetConsoleWindow();
  if(state) {
    LockWindowUpdate(nullptr);
  } else {
    LockWindowUpdate(console_window);
  }
}

JNIEXPORT void JNICALL Java_net_jterminal_windows_WindowsTerminalImpl_moveWindow
  (JNIEnv* env, jobject, jint x, jint y, jint width, jint height) {
  HWND console_window = GetConsoleWindow();
  if(console_window == nullptr) {
    return;
  }
  MoveWindow(console_window, x, y, width, height, false);
}

JNIEXPORT jintArray JNICALL Java_net_jterminal_windows_WindowsTerminalImpl_getWindowPosDim
  (JNIEnv* env, jobject) {
  HWND console_window = GetConsoleWindow();
  if(console_window == nullptr) {
    return nullptr;
  }
  RECT rect;
  GetWindowRect(console_window, &rect);
  const jint buf[4]{rect.left, rect.top, (rect.right - rect.left), (rect.bottom - rect.top)};

  jintArray jArr = env->NewIntArray(4);
  env->SetIntArrayRegion(jArr, 0, 4, buf);
  return jArr;
}

JNIEXPORT jboolean JNICALL Java_net_jterminal_windows_WindowsTerminalImpl_isWindowOnFocus
  (JNIEnv* env, jobject) {
  HWND console_window = GetConsoleWindow();
  if(console_window == nullptr) {
    return false;
  }
  return GetForegroundWindow() == console_window;
}

#endif

