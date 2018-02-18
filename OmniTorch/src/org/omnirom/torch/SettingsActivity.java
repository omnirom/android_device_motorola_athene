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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import android.content.Context;

public class SettingsActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    private static final String TAG = "TorchSettings";

    public static final String KEY_BRIGHT = "bright";
    public static final String KEY_STROBE = "strobe";
    public static final String KEY_STROBE_FREQ = "strobe_freq";
    public static final String KEY_SOS = "sos";
    public static final String KEY_FULLSCREEN = "fullscreen";
    public static final String KEY_SETTINGS = "settings";
    public static final String KEY_KEEP_SCREEN_ON = "keep_screen_on";

    private StrobeFreqPreference mStrobeFrequency;
    private CheckBoxPreference mBrightPref;
    private CheckBoxPreference mStrobePref;
    private SharedPreferences mPreferences;

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.settings);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mBrightPref = (CheckBoxPreference) findPreference(KEY_BRIGHT);
        int brightValue = getResources().getInteger(R.integer.valueHigh);
        if (brightValue == -1) {
            PreferenceCategory category = (PreferenceCategory) findPreference(KEY_SETTINGS);
            category.removePreference(mBrightPref);
        }
        mStrobePref = (CheckBoxPreference) findPreference(KEY_STROBE);
        mStrobeFrequency = (StrobeFreqPreference) findPreference(KEY_STROBE_FREQ);
        mStrobeFrequency.setEnabled(mPreferences.getBoolean(KEY_STROBE, false));

        updateEnablement();

        // keeps 'Strobe frequency' option available
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (key.equals(KEY_STROBE)) {
            mStrobeFrequency.setEnabled(sharedPreferences.getBoolean(
                    KEY_STROBE, false));
        }
        if (key.equals(KEY_SOS)) {
            updateEnablement();
            mStrobePref.setChecked(false);
            mBrightPref.setChecked(false);
        }
    }

    private void updateEnablement() {
        mStrobePref.setEnabled(!mPreferences.getBoolean(KEY_SOS, false));
        mStrobeFrequency.setEnabled(!mPreferences.getBoolean(KEY_SOS, false));
        mBrightPref.setEnabled(!mPreferences.getBoolean(KEY_SOS, false));
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (preference == mBrightPref) {
            if (mBrightPref.isChecked()
                    && !mPreferences.getBoolean("bright_warn_check", false)) {
                openBrightDialog();
                mPreferences.edit().putBoolean("bright_warn_check", true)
                        .commit();
            }
            return true;
        }
        return false;
    }

    private void openBrightDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View view = li.inflate(R.layout.brightwarn, null);
        new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.warning_label))
                .setView(view)
                .setNegativeButton(
                        this.getString(R.string.brightwarn_negative),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                                mBrightPref.setChecked(false);
                            }
                        })
                .setNeutralButton(this.getString(R.string.brightwarn_accept),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                            }
                        }).show();
    }
}
