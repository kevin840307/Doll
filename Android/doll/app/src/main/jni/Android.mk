LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := jniLib      #Android 加載的Lib名稱
#LOCAL_SRC_FILES := EncryptionString.c   #你所載入需要的C/C++檔案
LOCAL_SRC_FILES := $(wildcard $(LOCAL_PATH)/*.cpp)                          #載入當前資料夾所有C/C++檔案
LOCAL_SRC_FILES += $(wildcard $(LOCAL_PATH)/../../../../../../source/*.cpp) #載入所有資料夾所有C/C++檔案

include $(BUILD_SHARED_LIBRARY)