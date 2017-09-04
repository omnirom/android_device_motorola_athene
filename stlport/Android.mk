# http://b/15193147
# Whitelist devices that are still allowed to use stlport. This will prevent any
# new devices from making the same mistakes.
STLPORT_WHITELIST := \
    athene

ifneq (,$(filter $(TARGET_DEVICE),$(STLPORT_WHITELIST)))

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libstlport
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_MODULE_SUFFIX := .so
LOCAL_SRC_FILES := $(TARGET_DEVICE)/$(LOCAL_MODULE).so

include $(BUILD_PREBUILT)

endif
