LOCAL_PATH := $(call my-dir)  
include $(CLEAR_VARS)  
OPENCV_LIB_TYPE:=STATIC
ifeq ("$(wildcard $(OPENCV_MK_PATH))","")  
#try to load OpenCV.mk from default install location  
include E:/Android/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk 
else  
include $(OPENCV_MK_PATH)  
endif  
#OpenCV_INSTALL_MODULES:=on
#OPENCV_CAMERA_MODULES:=off
LOCAL_MODULE    := ImgFun  
LOCAL_SRC_FILES := ImgFun.cpp  
#LOCAL_LDLIBS    += -lllog
include $(BUILD_SHARED_LIBRARY)
#include $(BUILD_STATIC_LIBRARY)

#include $(INCLUDE_WHOLE_STATIC_LIBRARY)

#LOCAL_C_INCLUDES += ${NDKROOT}/sources/cxx-stl/stlport/stlport 

#include $(CLEAR_VARS)
#LOCAL_MODULE    := ImgFun
#LOCAL_STATIC_LIBRARIES := libImgFun1
#include $(BUILD_SHARED_LIBRARY)