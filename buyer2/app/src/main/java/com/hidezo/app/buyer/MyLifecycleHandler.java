package com.hidezo.app.buyer;

import android.app.Activity;
import android.app.Application;
//import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by dezamisystem2 on 2017/01/28.
 *
 */

class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "##MyLifecycle";

    private int resumed;
    private int paused;
    private int started;
    private int stopped;
    private boolean shouldSendLaunchLogs = true;

    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        if (shouldSendLaunchLogs) {
            // send logs
            shouldSendLaunchLogs = false;
        }
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        ++resumed;
        if (shouldSendLaunchLogs) {
            // send logs
            shouldSendLaunchLogs = false;
        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        ++paused;
        Log.d(TAG, "application is in foreground: " + (resumed > paused));
        if (shouldSendLaunchLogs) {
            // send logs
            shouldSendLaunchLogs = false;
        }
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        ++started;
        if (shouldSendLaunchLogs) {
            // send logs
            shouldSendLaunchLogs = false;
        }
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        // ここでアクテビティの表示処理が完了する
        ++stopped;
        Log.d(TAG, "application is visible: " + (started > stopped));
        if (shouldSendLaunchLogs) {
            // send logs
            shouldSendLaunchLogs = false;
        }
    }

//    @Override
//    public void onTrimMemory(int level) {
//        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
//            shouldSendLaunchLogs = true;
//        }
//    }

}
