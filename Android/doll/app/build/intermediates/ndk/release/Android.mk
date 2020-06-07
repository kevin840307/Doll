LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := jniLib
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	D:\Android\AndroidStudioProjects\doll\app\src\main\jni\Android.mk \
	D:\Android\AndroidStudioProjects\doll\app\src\main\jni\Application.mk \
	D:\Android\AndroidStudioProjects\doll\app\src\main\jni\EncryptionString.cpp \

LOCAL_C_INCLUDES += D:\Android\AndroidStudioProjects\doll\app\src\main\jni
LOCAL_C_INCLUDES += D:\Android\AndroidStudioProjects\doll\app\src\release\jni

include $(BUILD_SHARED_LIBRARY)
