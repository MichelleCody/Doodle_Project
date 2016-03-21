package com.example.michelle.finalattempt;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Button;
import android.view.View.OnClickListener;

import java.util.ArrayList;

/**
 * Created by Michelle on 3/9/16.
 */
public class DoodleView extends View {

    private Paint _paintDoodle = new Paint();
    private Path _path = new Path();
    private boolean erase=false;
    ArrayList<Paint> _paintList = new ArrayList<Paint>();
    ArrayList<Path> _pathList = new ArrayList<Path>();

    public DoodleView(Context context) {
        super(context);
        init(null, 0);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        _paintDoodle.setColor(Color.BLACK);
        _paintDoodle.setAntiAlias(true);
        _paintDoodle.setStyle(Paint.Style.STROKE);
        _paintDoodle.setStrokeJoin(Paint.Join.ROUND);
        _paintDoodle.setStrokeCap(Paint.Cap.ROUND);
    }

    public void changeColorRed() {
        _paintDoodle.setColor(Color.RED);
    }

    public void clear() {
        _pathList.clear();
        _paintList.clear();
        invalidate();
    }

    /*public void setErase(boolean isErase){
        erase=isErase;
        if(erase) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            drawPaint.setXfermode(null);
        }
    }*/


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i;
        for(i = 0; i < _pathList.size(); i++) {
            canvas.drawPath(_pathList.get(i), _paintList.get(i));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                _path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                //canvas.drawPath(_path, _paintDoodle);
                //_path.reset();
                _pathList.add(_path);
                Paint p = new Paint();
                p.set(_paintDoodle);
                _paintList.add(p);
                break;
        }

        invalidate();
        return true;
    }



}
