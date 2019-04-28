#
# Copyright (C) 2016 The CyanogenMod Project
#           (C) 2017 The LineageOS Project
#           (C) 2017 The OmniROM Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

LOCAL_PATH := device/motorola/athene
BUILD_TOP := $(shell pwd)

BOARD_VENDOR := motorola-qcom

TARGET_SPECIFIC_HEADER_PATH := $(LOCAL_PATH)/include

# AIDs and CAPS
TARGET_FS_CONFIG_GEN := \
    $(LOCAL_PATH)/fs_config/mot_aids.txt \
    $(LOCAL_PATH)/fs_config/file_caps.txt

# Asserts
TARGET_OTA_ASSERT_DEVICE := athene,athene_f,xt1621,xt1622,xt1625,xt1626,xt1640,xt1641,xt1642,xt1643,xt1644

# Platform
TARGET_BOARD_PLATFORM := msm8952
TARGET_CPU_CORTEX_A53 := true

# Bootloader
TARGET_BOOTLOADER_BOARD_NAME := MSM8952
TARGET_NO_BOOTLOADER := true

# Architecture
TARGET_ARCH := arm
TARGET_ARCH_VARIANT := armv7-a-neon
TARGET_CPU_ABI := armeabi-v7a
TARGET_CPU_ABI2 := armeabi
TARGET_CPU_VARIANT := cortex-a53
TARGET_CPU_SMP := true
ARCH_ARM_HAVE_TLS_REGISTER := true
#TARGET_GLOBAL_CFLAGS += -mfpu=neon -mfloat-abi=softfp
#TARGET_GLOBAL_CPPFLAGS += -mfpu=neon -mfloat-abi=softfp
TARGET_ARCH_VARIANT_OVERRIDE := true

# Kernel
BOARD_KERNEL_CMDLINE := console=ttyHSL0,115200,n8 androidboot.console=ttyHSL0 androidboot.hardware=qcom msm_rtb.filter=0x237 ehci-hcd.park=3 androidboot.bootdevice=7824900.sdhci lpm_levels.sleep_disabled=1 earlyprintk androidboot.selinux=enforcing
BOARD_KERNEL_BASE := 0x80000000
BOARD_KERNEL_PAGESIZE := 2048
BOARD_KERNEL_IMAGE_NAME := zImage
BOARD_KERNEL_SEPARATED_DT := true
TARGET_CUSTOM_DTBTOOL := dtbTool_custom
BOARD_DTBTOOL_ARGS := --force-v3 --motorola 1
BOARD_MKBOOTIMG_ARGS := --ramdisk_offset 0x01000000 --tags_offset 0x00000100
TARGET_KERNEL_ARCH := arm
TARGET_KERNEL_HEADER_ARCH := arm
TARGET_KERNEL_CONFIG := athene_defconfig
TARGET_KERNEL_SOURCE := kernel/motorola/msm8952
KERNEL_TOOLCHAIN := $(BUILD_TOP)/prebuilts/gcc/$(HOST_OS)-x86/arm/arm-eabi-4.8/bin
TARGET_KERNEL_CROSS_COMPILE_PREFIX := arm-eabi-

# Audio
AUDIO_FEATURE_ENABLED_KPI_OPTIMIZE := true
AUDIO_FEATURE_ENABLED_MULTI_VOICE_SESSIONS := true
AUDIO_FEATURE_ENABLED_NEW_SAMPLE_RATE := true
BOARD_USES_ALSA_AUDIO := true
#USE_CUSTOM_AUDIO_POLICY := 1
TARGET_USES_QCOM_MM_AUDIO := true
TARGET_QCOM_AUDIO_VARIANT := caf-msm8952
TARGET_ENABLE_QC_AV_ENHANCEMENTS := true
TARGET_USE_DEVICE_AUDIO_EFFECTS_CONF := true
BOARD_USES_GENERIC_AUDIO := true
#USE_LEGACY_LOCAL_AUDIO_HAL := true

# Bionic
TARGET_NEEDS_LEGACY_MUTEX_HANDLE := true

# Bluetooth
BOARD_BLUETOOTH_BDROID_BUILDCFG_INCLUDE_DIR := $(LOCAL_PATH)/bluetooth
BOARD_HAVE_BLUETOOTH := true
BOARD_HAVE_BLUETOOTH_QCOM := true
BLUETOOTH_HCI_USE_MCT := true
TARGET_QCOM_BLUETOOTH_VARIANT := caf-msm8952
QCOM_BT_READ_ADDR_FROM_PROP := true

# Camera
USE_DEVICE_SPECIFIC_CAMERA := true
TARGET_USES_NON_TREBLE_CAMERA := true
TARGET_CAMERASERVICE_CLOSES_NATIVE_HANDLES := true

# Charger
HEALTHD_USE_BATTERY_INFO := true

# Enable dex-preopt to speed up boot time
WITH_DEXPREOPT := true
WITH_DEXPREOPT_BOOT_IMG_AND_SYSTEM_SERVER_ONLY := true

# Display
NUM_FRAMEBUFFER_SURFACE_BUFFERS := 3
TARGET_USES_C2D_COMPOSITION := true
TARGET_USES_ION := true
TARGET_USES_NEW_ION_API := true
TARGET_FORCE_HWC_FOR_VIRTUAL_DISPLAYS := true
TARGET_NEEDS_HWC_ONFIRSTREF := true
SF_VSYNC_EVENT_PHASE_OFFSET_NS := 2000000
VSYNC_EVENT_PHASE_OFFSET_NS := 6000000

MAX_EGL_CACHE_KEY_SIZE := 12*1024
MAX_EGL_CACHE_SIZE := 2048*1024

OVERRIDE_RS_DRIVER := libRSDriver_adreno.so
TARGET_QCOM_DISPLAY_VARIANT := caf-msm8952

# Filesystem
BOARD_BOOTIMAGE_PARTITION_SIZE := 16777216        # 16384 * 1024 mmcblk0p28
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 16777216    # 16384 * 1024 mmcblk0p29
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 2600255488    # 2539312 * 1024 mmcblk0p47
BOARD_PERSISTIMAGE_PARTITION_SIZE := 28819456     # 28144 * 1024 mmcblk0p30
BOARD_USERDATAIMAGE_PARTITION_SIZE := 27258650624 # 26619776 * 1024 mmcblk0p48
BOARD_CACHEIMAGE_FILE_SYSTEM_TYPE := ext4
BOARD_CACHEIMAGE_PARTITION_SIZE := 260014080      # 253920 * 1024 mmcblk0p46

# FM
BOARD_HAVE_QCOM_FM := true
BOARD_HAVE_FM_RADIO := true
TARGET_QCOM_NO_FM_FIRMWARE := true
BOARD_DISABLE_FMRADIO_LIBJNI := true

# Init
TARGET_INIT_VENDOR_LIB := libinit_athene
TARGET_RECOVERY_DEVICE_MODULES := libinit_athene

# GPS
USE_DEVICE_SPECIFIC_GPS := true
USE_DEVICE_SPECIFIC_LOC_API := true
TARGET_NO_RPC := true

# Partitions
BOARD_FLASH_BLOCK_SIZE := 131072

# Power
TARGET_POWERHAL_VARIANT := qcom

# CPU
ENABLE_CPUSETS := true

# Lights
TARGET_PROVIDES_LIBLIGHT := true

# Properties
TARGET_SYSTEM_PROP += device/motorola/athene/system.prop

# Crypto
TARGET_HW_DISK_ENCRYPTION := true
TARGET_CRYPTFS_HW_PATH := $(LOCAL_PATH)/cryptfs_hw

#Enable peripheral manager
TARGET_PER_MGR_ENABLED := true

# Qualcomm support
BOARD_USES_QCOM_HARDWARE := true
BOARD_USES_QC_TIME_SERVICES := true
TARGET_USES_QCOM_BSP := true

# sdclang/LLVM
TARGET_USE_SDCLANG := true

# Media
TARGET_USES_MEDIA_EXTENSIONS := true
TARGET_QCOM_MEDIA_VARIANT := caf-msm8952

# Keymaster
TARGET_PROVIDES_KEYMASTER := true
TARGET_KEYMASTER_WAIT_FOR_QSEE := true

# CNE and DPM
TARGET_LDPRELOAD := libNimsWrap.so
BOARD_USES_QCNE := true

# Sensors
USE_SENSOR_MULTI_HAL := true

# Recovery
BOARD_HAS_NO_SELECT_BUTTON := true
TARGET_RECOVERY_FSTAB := $(LOCAL_PATH)/rootdir/etc/recovery.fstab
TARGET_USERIMAGES_USE_EXT4 := true
TARGET_USERIMAGES_USE_F2FS := true

# RIL
BOARD_PROVIDES_LIBRIL := true

# Offline charging
BOARD_USE_CUSTOM_RECOVERY_FONT := \"roboto_23x41.h\"

# SDClang
TARGET_USE_SDCLANG := true

# SELinux
include device/qcom/sepolicy/sepolicy.mk
# MUST NOT USE LOCAL_PATH
BOARD_SEPOLICY_DIRS += \
    device/motorola/athene/sepolicy
include vendor/omni/sepolicy/sepolicy.mk

DEVICE_MANIFEST_FILE := $(LOCAL_PATH)/configs/manifest.xml

# Wifi
BOARD_HAS_QCOM_WLAN := true
BOARD_HOSTAPD_DRIVER := NL80211
BOARD_HOSTAPD_PRIVATE_LIB := lib_driver_cmd_qcwcn
BOARD_WLAN_DEVICE := qcwcn
BOARD_WPA_SUPPLICANT_DRIVER := NL80211
BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_qcwcn
WIFI_DRIVER_FW_PATH_AP := "ap"
WIFI_DRIVER_FW_PATH_STA := "sta"
WIFI_NEEDS_FST_DRIVER := true
WPA_SUPPLICANT_VERSION := VER_0_8_X
