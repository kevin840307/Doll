

#ifndef DOLL_COM_MNDT_GHOST_DOLL_LOGINWAITACTIVITY_H
#include<jni.h>
#define DOLL_COM_MNDT_GHOST_DOLL_LOGINWAITACTIVITY_H
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     demo_example_hellojni_HelloActivity
 * Method:    stringFromJNI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jint JNICALL
Java_com_mndt_ghost_doll_EncryptionString_EncryptionString_fnNumberEncry
  (JNIEnv *, jobject, jstring);

int fnGetEncryNum(JNIEnv *jniEnv, jstring jsStr);

/*
 * Class:     demo_example_hellojni_HelloActivity
 * Method:    unimplementedStringFromJNI
 * Signature: ()Ljava/lang/String;

JNIEXPORT jstring JNICALL Java_demo_example_hellojni_HelloActivity_unimplementedStringFromJNI
  (JNIEnv *, jobject); */

#ifdef __cplusplus
}
#endif
#endif
