/*
 * Copyright (C) 2016 The CyanogenMod Project
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

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Constants {

    // Swap keys
    public static final String FP_HOME_KEY = "fp_home";

    // Wakeup key
    public static final String FP_HOME_WAKEUP_KEY = "fp_home_wakeup";

    // Display key
    public static final String DISPLAY_BURNIN_KEY = "display_burnin";

    // Swap nodes
    public static final String FP_HOME_NODE = "/sys/homebutton/enable";

    // Wakeup node
    public static final String FP_HOME_WAKEUP_NODE = "/sys/homebutton/enable_wakeup";

    // Display node
    public static final String DISPLAY_BURNIN_NODE = "/sys/devices/platform/kcal_ctrl.0/kcal";
    public static final String DISPLAY_BURNIN_ENABLED = "180 180 180";
    public static final String DISPLAY_BURNIN_DISABLED = "256 256 256";

    // Holds <preference_key> -> <proc_node> mapping
    public static final Map<String, String> sBooleanNodePreferenceMap = new HashMap<>();

    // Holds <preference_key> -> <default_values> mapping
    public static final Map<String, Object> sNodeDefaultMap = new HashMap<>();

    public static final String[] sButtonPrefKeys = {
        FP_HOME_KEY,
        FP_HOME_WAKEUP_KEY,
        DISPLAY_BURNIN_KEY
    };

    static {
        sBooleanNodePreferenceMap.put(FP_HOME_KEY, FP_HOME_NODE);
        sBooleanNodePreferenceMap.put(FP_HOME_WAKEUP_KEY, FP_HOME_WAKEUP_NODE);
        sBooleanNodePreferenceMap.put(DISPLAY_BURNIN_KEY, DISPLAY_BURNIN_NODE);
        sNodeDefaultMap.put(FP_HOME_KEY, false);
        sNodeDefaultMap.put(FP_HOME_WAKEUP_KEY, false);
        sNodeDefaultMap.put(DISPLAY_BURNIN_KEY, false);
    }

    public static boolean isPreferenceEnabled(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, (Boolean) sNodeDefaultMap.get(key));
    }
}
