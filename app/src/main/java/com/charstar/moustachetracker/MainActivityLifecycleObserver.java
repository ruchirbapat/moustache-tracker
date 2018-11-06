// Credits to https://github.com/qichuan/firebase_ml_snapchat_filter/blob/master/app/src/main/java/com/zqc/ml/snapchat/MainActivityLifecycleObserver.kt
// For his Kotlin code on handling lifecycle events
// I have, however, changed it into Java and adapted it for my project

package com.charstar.moustachetracker;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.otaliastudios.cameraview.CameraView;

public class MainActivityLifecycleObserver implements LifecycleObserver {

    private CameraView camView;

    MainActivityLifecycleObserver(CameraView cam_view) {
        camView = cam_view;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        camView.start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        camView.stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        camView.destroy();
    }

}