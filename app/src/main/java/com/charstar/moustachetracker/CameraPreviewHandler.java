package com.charstar.moustachetracker;

import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreviewHandler extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Camera cameraObject;

    public CameraPreviewHandler(MainActivity context, Camera cameraDevice) {
        super(context);
        cameraObject = cameraDevice;

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            cameraObject.setPreviewDisplay(holder);
            cameraObject.setDisplayOrientation(90);
            cameraObject.startPreview();
        } catch (IOException e) {
            System.out.println("could not set camera preview!");
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) { return; };

    // TODO: Implement something if the surface is for example needing to be rotated
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        // TODO: Send shit to the screen
        // ...

        return; // For now
    }

}
