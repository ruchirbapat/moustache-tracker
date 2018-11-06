package com.charstar.moustachetracker;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager; // Might be able to remove this one later (depends)
import android.content.pm.PackageManager;


import android.widget.Toast;
import android.content.Context;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;
import com.otaliastudios.cameraview.Size;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int PERMISSION_REQUEST_CODE = 3;
    private final String[] permissionsNeeded = {android.Manifest.permission.CAMERA};
    private boolean gotPermission = false;
    private Overlay overlay;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meme); // TODO: change later
        FirebaseApp.initializeApp(this);

        CameraView camera = findViewById(R.id.camera_view);
        overlay = (Overlay)findViewById(R.id.overlay_view);

        camera.start();

        //Request camera permissions if not already granted
        if(true) {
            final Context context = getApplicationContext();
            CharSequence text = "GOT CAMERA PERMISSION";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();

            this.getLifecycle().addObserver(new MainActivityLifecycleObserver(camera));

            //Begin face processing
            camera.addFrameProcessor(new FrameProcessor() {
                @Override
                public void process(@NonNull Frame frame) {
                    byte[] data = frame.getData();
                    int rotation = frame.getRotation();
                    int format = frame.getFormat();

                    // High-accuracy landmark detection and face classification
                    FirebaseVisionFaceDetectorOptions options =
                            new FirebaseVisionFaceDetectorOptions.Builder()
                                    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                                    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                                    .setContourMode(FirebaseVisionFaceDetectorOptions.NO_CONTOURS)
                                    .setClassificationMode(FirebaseVisionFaceDetectorOptions.NO_CLASSIFICATIONS)
                                    .setMinFaceSize(0.35f)
                                    .build();

                    FirebaseVisionFaceDetector faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options);

                    FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                            .setWidth((int)(1280))
                            .setHeight((int)(720))  // image recognition
                            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                            .setRotation(frame.getRotation() / 90)
                            .build();

                    FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(frame.getData(), metadata);

                    Task<List<FirebaseVisionFace>> result =
                            faceDetector.detectInImage(image)
                                    .addOnSuccessListener(
                                            new OnSuccessListener<List<FirebaseVisionFace>>() {
                                                @Override
                                                public void onSuccess(List<FirebaseVisionFace> faces) {
                                                    System.out.println(faces.size());
                                                    for(FirebaseVisionFace face : faces) {
                                                        overlay.renderFace(face);
                                                    }
                                                }
                                            });

                }
            }
            );
        } else {
            Context context = getApplicationContext();
            CharSequence text = "CAMERA PERMISSIONS FAILED";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
        }


        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        // TODO: make sure the camera is properly closed when the app closes
    }

    private boolean handleCameraPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
        return gotPermission;
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (android.Manifest.permission.CAMERA == permissions[0] &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotPermission = true;
            } else {
                gotPermission = false;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
