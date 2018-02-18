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

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.UserHandle;
import android.os.Bundle;
import android.util.Log;

public class TorchService extends Service {

    private static final String TAG = "TorchService";

    private TimerTask mTorchTask;
    private Timer mTorchTimer;
    private WrapperTask mStrobeTask;
    private Timer mStrobeTimer;
    private Timer mSosTimer;
    private WrapperTask mSosTask;
    private Runnable mSosOnRunnable;
    private Runnable mSosOffRunnable;
    private int mSosCount;
    private NotificationManager mNotificationManager;
    private Notification.Builder mNotificationBuilder;
    private int mStrobePeriod;
    private boolean mBright;
    private boolean mSos;
    private boolean mStrobe;
    private Runnable mStrobeRunnable;
    private Context mContext;

    public void onCreate() {
        Log.d(TAG, "onCreate");
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) getSystemService(ns);
        mContext = getApplicationContext();

        mTorchTask = new TimerTask() {
            public void run() {
                FlashDevice.getInstance(mContext).setFlashMode(FlashDevice.ON,
                        mBright);
            }
        };
        mTorchTimer = new Timer();

        mStrobeRunnable = new Runnable() {
            private int mCounter = 4;

            public void run() {
                int flashMode = FlashDevice.ON;
                if (FlashDevice.getInstance(mContext).getFlashMode() == FlashDevice.STROBE) {
                    if (mCounter-- < 1) {
                        FlashDevice.getInstance(mContext).setFlashMode(
                                flashMode, mBright);
                    }
                } else {
                    FlashDevice.getInstance(mContext).setFlashMode(
                            FlashDevice.STROBE, mBright);
                    mCounter = 4;
                }
            }

        };
        mStrobeTask = new WrapperTask(mStrobeRunnable);
        mStrobeTimer = new Timer();

        mSosOnRunnable = new Runnable() {
            public void run() {
                FlashDevice.getInstance(mContext).setFlashMode(FlashDevice.ON,
                        mBright);
                mSosTask = new WrapperTask(mSosOffRunnable);
                int schedTime = 0;
                switch (mSosCount) {
                case 0:
                case 1:
                case 2:
                case 6:
                case 7:
                case 8:
                    schedTime = 200;
                    break;
                case 3:
                case 4:
                case 5:
                    schedTime = 600;
                    break;
                default:
                    return;
                }
                if (mSosTimer != null) {
                    mSosTimer.schedule(mSosTask, schedTime);
                }
            }
        };

        mSosOffRunnable = new Runnable() {
            public void run() {
                FlashDevice.getInstance(mContext).setFlashMode(FlashDevice.OFF,
                        mBright);
                mSosTask = new WrapperTask(mSosOnRunnable);
                mSosCount++;
                if (mSosCount == 9) {
                    mSosCount = 0;
                }
                if (mSosTimer != null) {
                    mSosTimer.schedule(mSosTask, mSosCount == 0 ? 2000 : 200);
                }
            }
        };

        mSosTask = new WrapperTask(mSosOnRunnable);
        mSosTimer = new Timer();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");
        if (intent == null) {
            stopSelf();
            return START_NOT_STICKY;
        }
        boolean startActivity = intent.getBooleanExtra("activity", false);
        mBright = intent.getBooleanExtra("bright", false);
        mStrobe = intent.getBooleanExtra("strobe", false);
        mSos = intent.getBooleanExtra("sos", false);

        int strobePeriod = intent.getIntExtra("period", 5);
        if (strobePeriod == 0) {
            strobePeriod = 1;
        }
        mStrobePeriod = (666 / strobePeriod) / 4;

        if (mSos) {
            mBright = true;
            mStrobe = false;
        }

        Log.d(TAG, "onStartCommand mBright = " + mBright + " mStrobe = "
                + mStrobe + " mStrobePeriod = " + mStrobePeriod + " mSos = "
                + mSos + " startActivity = " + startActivity);

        if (mSos) {
            mSosTimer.schedule(mSosTask, 0);
        } else if (mStrobe) {
            mStrobeTimer.schedule(mStrobeTask, 0, mStrobePeriod);
        } else {
            mTorchTimer.schedule(mTorchTask, 0, 100);
        }
        mNotificationBuilder = new Notification.Builder(this);
        mNotificationBuilder.setSmallIcon(R.drawable.ic_torch_on);
        mNotificationBuilder.setTicker(getString(R.string.not_torch_title));
        mNotificationBuilder
                .setContentTitle(getString(R.string.not_torch_title));

        Intent fullscreenIntent = new Intent(this, MainActivity.class);
        fullscreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (startActivity) {
            mNotificationBuilder.setFullScreenIntent(PendingIntent.getActivity(
                    this, 0, fullscreenIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT), true);
        }

        mNotificationBuilder.setContentIntent(PendingIntent.getActivity(this,
                0, fullscreenIntent, 0));
        mNotificationBuilder.setAutoCancel(false);
        mNotificationBuilder.setOngoing(true);

        PendingIntent turnOff = PendingIntent.getBroadcast(this, 0, new Intent(
                TorchSwitch.TOGGLE_FLASHLIGHT), 0);
        mNotificationBuilder.addAction(R.drawable.ic_torch_off,
                getString(R.string.not_torch_toggle), turnOff);

        Notification notification = mNotificationBuilder.build();
        mNotificationManager.notify(getString(R.string.app_name).hashCode(),
                notification);

        startForeground(getString(R.string.app_name).hashCode(), notification);
        updateState(true);
        return START_STICKY;
    }

    public void onDestroy() {
        mNotificationManager.cancelAll();
        stopForeground(true);
        mTorchTimer.cancel();
        mTorchTimer = null;
        mStrobeTimer.cancel();
        mStrobeTimer = null;
        mSosTimer.cancel();
        mSosTimer = null;
        FlashDevice.getInstance(mContext)
                .setFlashMode(FlashDevice.OFF, mBright);
        updateState(false);
    }

    private void updateState(boolean on) {
        Intent intent = new Intent(TorchSwitch.TORCH_STATE_CHANGED);
        intent.putExtra("state", on ? 1 : 0);
        sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    public class WrapperTask extends TimerTask {
        private final Runnable mTarget;

        public WrapperTask(Runnable target) {
            mTarget = target;
        }

        public void run() {
            mTarget.run();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
