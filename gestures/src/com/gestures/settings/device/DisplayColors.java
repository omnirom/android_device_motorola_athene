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
    private final UpdatedStateNotifier mUpdatedStateNotifier;
    private boolean mBurnInProtectionEnabled;

    public DisplayColors(Context context, UpdatedStateNotifier updatedStateNotifier) {
        mContext = context;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        loadPreferences(sharedPrefs);
        sharedPrefs.registerOnSharedPreferenceChangeListener(mPrefListener);
        mUpdatedStateNotifier = updatedStateNotifier;
    }

    public boolean isBurnInProtectionEnabled() {
        return mBurnInProtectionEnabled;
    }

    public void loadPreferences(SharedPreferences sharedPreferences) {
        mBurnInProtectionEnabled = sharedPreferences.getBoolean(Constants.DISPLAY_BURNIN_KEY, false);
        enableBurnInProtection(mBurnInProtectionEnabled);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            boolean updated = true;

            if (Constants.DISPLAY_BURNIN_KEY.equals(key)) {
                mBurnInProtectionEnabled = sharedPreferences.getBoolean(Constants.DISPLAY_BURNIN_KEY, false);
                enableBurnInProtection(mBurnInProtectionEnabled);
            } else {
                updated = false;
            }

            if (updated) {
                mUpdatedStateNotifier.updateState();
            }
        }
    };

    public void enableBurnInProtection(boolean enabled) {
        if (enabled) {
            FileUtils.writeLine(Constants.DISPLAY_BURNIN_NODE, Constants.DISPLAY_BURNIN_ENABLED);
        } else {
            FileUtils.writeLine(Constants.DISPLAY_BURNIN_NODE, Constants.DISPLAY_BURNIN_DISABLED);
        }
    }
}
