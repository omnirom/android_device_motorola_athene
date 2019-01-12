/*
 * Copyright (c) 2015-2016 The CyanogenMod Project
 * Copyright (c) 2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gestures.settings.device;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import android.util.Log;

import com.gestures.settings.device.FileUtils;
import com.gestures.settings.device.Constants;

import android.content.Context;
import android.util.Log;

public class DisplayColors {
    private static final String TAG = "Gestures-DisplayColors";

    private final Context mContext;
    private boolean mBurnInProtectionEnabled;

    // Display node
    public static final String DISPLAY_BURNIN_NODE = "/sys/devices/platform/kcal_ctrl.0/kcal";
    public static final String DISPLAY_BURNIN_ENABLED = "180 180 180";
    public static final String DISPLAY_BURNIN_DISABLED = "256 256 256";

    // Display key
    public static final String DISPLAY_BURNIN_KEY = "display_burnin";

    public DisplayColors(Context context) {
        mContext = context;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        loadPreferences(sharedPrefs);
    }

    public static boolean isBurnInProtectionEnabled() {
        return FileUtils.readOneLine(DISPLAY_BURNIN_NODE).equals(DISPLAY_BURNIN_ENABLED);
    }

    public void loadPreferences(SharedPreferences sharedPreferences) {
        mBurnInProtectionEnabled = sharedPreferences.getBoolean(DISPLAY_BURNIN_KEY, false);
        enableBurnInProtection(mBurnInProtectionEnabled);
    }

    public static void enableBurnInProtection(boolean enabled) {
        FileUtils.writeLine(DISPLAY_BURNIN_NODE,
                enabled ? DISPLAY_BURNIN_ENABLED : DISPLAY_BURNIN_DISABLED);
    }
}
