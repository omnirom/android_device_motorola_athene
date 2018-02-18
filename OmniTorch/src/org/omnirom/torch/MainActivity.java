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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private static final String TAG = "TorchActivity";

    private TorchWidgetProvider mWidgetProvider;
    private ImageView mButtonOnView;
    private boolean mTorchOn;
    private Context mContext;
    private SharedPreferences mPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get preferences
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        if (mPrefs.getBoolean(SettingsActivity.KEY_KEEP_SCREEN_ON, false)) {
            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        // Fullscreen mode
        if (mPrefs.getBoolean(SettingsActivity.KEY_FULLSCREEN, false)) {
            win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.mainnew);
        mContext = this.getApplicationContext();
        mButtonOnView = (ImageView) findViewById(R.id.buttoOnImage);

        mButtonOnView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                createIntent();
            }
        });

        mWidgetProvider = TorchWidgetProvider.getInstance();
        mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs,
                    String key) {
                updatePrefs(prefs, key);
            }
        };
        mPrefs.registerOnSharedPreferenceChangeListener(mPrefsListener);

    }

    private void createIntent() {
        Intent intent = new Intent(TorchSwitch.TOGGLE_FLASHLIGHT);
        intent.putExtra("strobe",
                mPrefs.getBoolean(SettingsActivity.KEY_STROBE, false));
        intent.putExtra("period",
                mPrefs.getInt(SettingsActivity.KEY_STROBE_FREQ, 5));
        intent.putExtra("bright",
                mPrefs.getBoolean(SettingsActivity.KEY_BRIGHT, false));
        intent.putExtra("sos",
                mPrefs.getBoolean(SettingsActivity.KEY_SOS, false));
        intent.putExtra("activity", false);
        mContext.sendBroadcast(intent);
    }

    private void createSosIntent() {
        if (mTorchOn) {
            // stop it first
            createIntent();
        }
        Intent intent = new Intent(TorchSwitch.TOGGLE_FLASHLIGHT);
        intent.putExtra("strobe",
                mPrefs.getBoolean(SettingsActivity.KEY_STROBE, false));
        intent.putExtra("period",
                mPrefs.getInt(SettingsActivity.KEY_STROBE_FREQ, 5));
        intent.putExtra("bright",
                mPrefs.getBoolean(SettingsActivity.KEY_BRIGHT, false));
        intent.putExtra("sos", true);
        intent.putExtra("activity", false);
        mContext.sendBroadcast(intent);
    }

    public void onPause() {
        updateWidget();
        mContext.unregisterReceiver(mStateReceiver);
        super.onPause();
    }

    public void onResume() {
        mTorchOn = TorchSwitch.isTorchServiceRunning(this);
        mContext.registerReceiver(mStateReceiver, new IntentFilter(
                TorchSwitch.TORCH_STATE_CHANGED));
        updateWidget();
        updateBigButtonState();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_sos:
            createSosIntent();
            return true;
        case R.id.action_about:
            this.openAboutDialog();
            return true;
        case R.id.action_settings:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityIfNeeded(intent, -1);
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void openAboutDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View view = li.inflate(R.layout.aboutview, null);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(this.getString(R.string.about_title))
                .setView(view)
                .setNegativeButton(this.getString(R.string.about_close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                            }
                        }).show();
    }

    public void updateWidget() {
        mWidgetProvider.updateAllStates(mContext);
    }

    private void updateBigButtonState() {
        mButtonOnView.setImageResource(mTorchOn ? R.drawable.button_off
                : R.drawable.button_on);
    }

    private BroadcastReceiver mStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TorchSwitch.TORCH_STATE_CHANGED)) {
                mTorchOn = intent.getIntExtra("state", 0) != 0;
                updateBigButtonState();
            }
        }
    };

    public void updatePrefs(SharedPreferences prefs, String key) {
        final Window win = getWindow();
        if (prefs.getBoolean(SettingsActivity.KEY_KEEP_SCREEN_ON, false)) {
            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (prefs.getBoolean(SettingsActivity.KEY_FULLSCREEN, false)) {
            win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            win.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
