package com.charstar.moustachetracker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Toast;

import com.google.android.gms.vision.face.Landmark;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

public class Overlay extends SurfaceView implements SurfaceHolder.Callback {

    private FirebaseVisionFace face = null;

    private Paint greenBox;
    private Paint dot;

    // Load resources
    public Overlay (Context context) { super(context); init(this.getHolder()); }
    public Overlay (Context context, AttributeSet attrs) { super(context, attrs); init(this.getHolder()); }
    public Overlay (Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); init(this.getHolder()); }
    public Overlay (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); init(this.getHolder()); }

    public void init(SurfaceHolder holder) {
        setBackgroundColor(Color.TRANSPARENT);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        greenBox = new Paint();
        greenBox.setColor(Color.GREEN);
        greenBox.setStyle(Paint.Style.STROKE);
        dot = new Paint();
        dot.setColor(Color.GREEN);
    }

    public void renderFace(FirebaseVisionFace f) {
        //do calculations
        face = f;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("YAYYYY");
        canvas.drawColor(Color.TRANSPARENT);
//        canvas.drawColor(Color.BLUE);
        if(face != null) {
            for(int i = 0; i <= 11; i++) {
                FirebaseVisionFaceLandmark landmark = face.getLandmark(i);
                if(landmark != null) {
                    canvas.drawCircle(landmark.getPosition().getX(), landmark.getPosition().getY(), 5, dot);
                    //canvas.drawPoint(landmark.getPosition().getX(), landmark.getPosition().getY(), dot);
                }            }
            canvas.drawRect(face.getBoundingBox(), greenBox);
        }
        //canvas.drawLine(0.0F, 0.0F, 500.0F, 500.0F, p);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

}
