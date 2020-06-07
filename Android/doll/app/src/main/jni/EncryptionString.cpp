//
// Created by Ghost on 2017/10/28.
//

#include "com_mndt_ghost_doll_EncryptionString_EncryptionString.h"
#include <stdlib.h>

int fnGetEncryNum(JNIEnv* jniEnv, jstring jsStr) {
    char* pcEncryStr = NULL;
    jclass jcJavaStr = jniEnv->FindClass("java/lang/String");
    jstring jsCoding = jniEnv->NewStringUTF("utf-8");
    jmethodID jmJavaGetByte = jniEnv->GetMethodID(jcJavaStr, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray jbBuffer= (jbyteArray)jniEnv->CallObjectMethod(jsStr, jmJavaGetByte, jsCoding);
    jsize jSize = jniEnv->GetArrayLength(jbBuffer);
    jbyte* pjbStrByte = jniEnv->GetByteArrayElements(jbBuffer, JNI_FALSE);

    if (jSize > 0) {
        pcEncryStr = (char*)malloc(jSize + 1);
        memcpy(pcEncryStr, pjbStrByte, jSize);
        pcEncryStr[jSize] = 0;
    }

    int iSum = 0;
    for (int iIndex = 0; iIndex < jSize; iIndex++) {
        iSum +=(((pcEncryStr[iIndex] - 48 + 1) * (iIndex + 1) * 97) & 0xefcd);
    }

    jniEnv->ReleaseByteArrayElements(jbBuffer, pjbStrByte, 0);
    jniEnv->DeleteLocalRef(jsCoding);
    jniEnv->DeleteLocalRef(jcJavaStr);
    jniEnv->DeleteLocalRef(jbBuffer);
    delete[] pcEncryStr;
    return iSum;
}


JNIEXPORT jint JNICALL
Java_com_mndt_ghost_doll_EncryptionString_EncryptionString_fnNumberEncry(JNIEnv *jniEnv, jobject obj, jstring str) {
    return fnGetEncryNum(jniEnv, str);
}

