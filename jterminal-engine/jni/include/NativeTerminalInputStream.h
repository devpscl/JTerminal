/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class net_jterminal_io_NativeTerminalInputStream */

#ifndef _Included_net_jterminal_io_NativeTerminalInputStream
#define _Included_net_jterminal_io_NativeTerminalInputStream
#ifdef __cplusplus
extern "C" {
#endif
#undef net_jterminal_io_NativeTerminalInputStream_MAX_SKIP_BUFFER_SIZE
#define net_jterminal_io_NativeTerminalInputStream_MAX_SKIP_BUFFER_SIZE 2048L
#undef net_jterminal_io_NativeTerminalInputStream_DEFAULT_BUFFER_SIZE
#define net_jterminal_io_NativeTerminalInputStream_DEFAULT_BUFFER_SIZE 8192L
#undef net_jterminal_io_NativeTerminalInputStream_MAX_BUFFER_SIZE
#define net_jterminal_io_NativeTerminalInputStream_MAX_BUFFER_SIZE 2147483639L
/*
 * Class:     net_jterminal_io_NativeTerminalInputStream
 * Method:    _read
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_net_jterminal_io_NativeTerminalInputStream__1read__J
  (JNIEnv *, jobject, jlong);

/*
 * Class:     net_jterminal_io_NativeTerminalInputStream
 * Method:    _avail
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_net_jterminal_io_NativeTerminalInputStream__1avail
  (JNIEnv *, jobject, jlong);

/*
 * Class:     net_jterminal_io_NativeTerminalInputStream
 * Method:    _peek
 * Signature: (J[BII)I
 */
JNIEXPORT jint JNICALL Java_net_jterminal_io_NativeTerminalInputStream__1peek
  (JNIEnv *, jobject, jlong, jbyteArray, jint, jint);

/*
 * Class:     net_jterminal_io_NativeTerminalInputStream
 * Method:    _read
 * Signature: (J[BIIJ)I
 */
JNIEXPORT jint JNICALL Java_net_jterminal_io_NativeTerminalInputStream__1read__J_3BIIJ
  (JNIEnv *, jobject, jlong, jbyteArray, jint, jint, jlong);

/*
 * Class:     net_jterminal_io_NativeTerminalInputStream
 * Method:    _peekInput
 * Signature: (JLnet/jterminal/util/Ref;)I
 */
JNIEXPORT jint JNICALL Java_net_jterminal_io_NativeTerminalInputStream__1peekInput
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     net_jterminal_io_NativeTerminalInputStream
 * Method:    _readInput
 * Signature: (JJ)Lnet/jterminal/input/InputEvent;
 */
JNIEXPORT jobject JNICALL Java_net_jterminal_io_NativeTerminalInputStream__1readInput
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     net_jterminal_io_NativeTerminalInputStream
 * Method:    _reset
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_io_NativeTerminalInputStream__1reset
  (JNIEnv *, jobject, jlong);

/*
 * Class:     net_jterminal_io_NativeTerminalInputStream
 * Method:    _close
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_io_NativeTerminalInputStream__1close
  (JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif
