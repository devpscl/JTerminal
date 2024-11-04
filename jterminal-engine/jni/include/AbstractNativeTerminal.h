/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class net_jterminal_instance_AbstractNativeTerminal */

#ifndef _Included_net_jterminal_instance_AbstractNativeTerminal
#define _Included_net_jterminal_instance_AbstractNativeTerminal
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _create
 * Signature: (BI)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1create
  (JNIEnv *, jobject, jbyte, jint);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1shutdown
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _joinFutureClose
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1joinFutureClose
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _isEnabled
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1isEnabled
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _setFlags
 * Signature: (B)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1setFlags
  (JNIEnv *, jobject, jbyte);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _getFlags
 * Signature: ()B
 */
JNIEXPORT jbyte JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1getFlags
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1clear
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _update
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1update
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _reset
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1reset
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _beep
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1beep
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _setBuffer
 * Signature: (B)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1setBuffer
  (JNIEnv *, jobject, jbyte);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _getBuffer
 * Signature: ()B
 */
JNIEXPORT jbyte JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1getBuffer
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _setTitle
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1setTitle
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _getTitle
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1getTitle
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _setDim
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1setDim
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _getDim
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1getDim
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _setCursor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1setCursor
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _requestCursor
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1requestCursor
  (JNIEnv *, jobject);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _setCursorFlags
 * Signature: (B)V
 */
JNIEXPORT void JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1setCursorFlags
  (JNIEnv *, jobject, jbyte);

/*
 * Class:     net_jterminal_instance_AbstractNativeTerminal
 * Method:    _getCursorFlags
 * Signature: ()B
 */
JNIEXPORT jbyte JNICALL Java_net_jterminal_instance_AbstractNativeTerminal__1getCursorFlags
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
