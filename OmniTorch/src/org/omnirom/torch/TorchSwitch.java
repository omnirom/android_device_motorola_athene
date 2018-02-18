/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.omnirom.torch;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.os.UserHandle;

public class TorchSwitch extends BroadcastReceiver {
    private static final String TAG = "TorchSwitch";
    public static final String TOGGLE_FLASHLIGHT = "org.omnirom.torch.TOGGLE_FLASHLIGHT";
    public static final String START_FLASHLIGHT = "org.omnirom.torch.START_FLASHLIGHT";
    public static final String TORCH_STATE_CHANGED = "org.omnirom.torch.TORCH_STATE_CHANGED";

    private SharedPreferences mPrefs;

    @Override
    public void onReceive(Context context, Intent receivingIntent) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (receivingIntent.getAction().equals(TOGGLE_FLASHLIGHT)) {
            // bright setting can come from intent or from prefs depending on
            // on what send the broadcast
            boolean bright = receivingIntent.getBooleanExtra("bright",
                    mPrefs.getBoolean(SettingsActivity.KEY_BRIGHT, false));
            boolean strobe = receivingIntent.getBooleanExtra("strobe",
                    mPrefs.getBoolean(SettingsActivity.KEY_STROBE, false));
            boolean sos = receivingIntent.getBooleanExtra("sos",
                    mPrefs.getBoolean(SettingsActivity.KEY_SOS, false));
            int period = receivingIntent.getIntExtra("period",
                    mPrefs.getInt(SettingsActivity.KEY_STROBE_FREQ, 5));

            Intent i = new Intent(context, TorchService.class);
            if (isTorchServiceRunning(context)) {
                context.stopServiceAsUser(i, UserHandle.CURRENT);
            } else {
                i.putExtra("bright", bright);
                i.putExtra("strobe", strobe);
                i.putExtra("period", period);
                i.putExtra("sos", sos);
                i.putExtra("activity", false);
                context.startServiceAsUser(i, UserHandle.CURRENT);
            }
        } else if (receivingIntent.getAction().equals(START_FLASHLIGHT)) {
            // bright setting can come from intent or from prefs depending on
            // on what send the broadcast
            boolean bright = receivingIntent.getBooleanExtra("bright",
                    mPrefs.getBoolean(SettingsActivity.KEY_BRIGHT, false));
            boolean strobe = receivingIntent.getBooleanExtra("strobe",
                    mPrefs.getBoolean(SettingsActivity.KEY_STROBE, false));
            boolean sos = receivingIntent.getBooleanExtra("sos",
                    mPrefs.getBoolean(SettingsActivity.KEY_SOS, false));
            int period = receivingIntent.getIntExtra("period",
                    mPrefs.getInt(SettingsActivity.KEY_STROBE_FREQ, 5));

            Intent i = new Intent(context, TorchService.class);
            if (!isTorchServiceRunning(context)) {
                i.putExtra("bright", bright);
                i.putExtra("strobe", strobe);
                i.putExtra("period", period);
                i.putExtra("sos", sos);
                i.putExtra("activity", true);
                context.startServiceAsUser(i, UserHandle.CURRENT);
            }
        }
    }

    public static boolean isTorchServiceRunning(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> svcList = am
                .getRunningServices(100);

        if (!(svcList.size() > 0)) {
            return false;
        }
        for (RunningServiceInfo serviceInfo : svcList) {
            ComponentName serviceName = serviceInfo.service;
            if (serviceName.getClassName().endsWith(".TorchService")
                    || serviceName.getClassName().endsWith(".RootTorchService")) {
                return true;
            }
        }
        return false;
    }
}
