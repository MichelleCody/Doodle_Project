package com.example.michelle.finalattempt;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Michelle on 3/9/16.
 */
public class DoodleView extends View {

    private Paint _paintDoodle = new Paint();
    private Path _path = new Path();
    private int alpha = 10;
    private boolean erase=false;
    ArrayList<Paint> _paintList = new ArrayList<Paint>();
    ArrayList<Path> _pathList = new ArrayList<Path>();
    ArrayList<Paint> _undonePaint = new ArrayList<Paint>();
    ArrayList<Path> _undonePath = new ArrayList<Path>();

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
        _paintDoodle.setStrokeWidth(10);
        _paintDoodle.setAlpha(alpha);
        _paintDoodle.setStrokeJoin(Paint.Join.ROUND);
        _paintDoodle.setStrokeCap(Paint.Cap.ROUND);
    }

    public void changeColor(int color) {
        _paintDoodle.setColor(color);
        _paintDoodle.setAlpha(alpha);
        //_paintDoodle.setColor(Color.parseColor(color));
    }

    public void clear() {
        _pathList.clear();
        _paintList.clear();
        invalidate();
    }

    public void undo() {
        //get last path and paint and store both
        if (!_pathList.isEmpty()) {
            Path lastPath = _pathList.get(_pathList.size() - 1);
            _undonePath.add(lastPath);
            Paint lastPaint = _paintList.get(_paintList.size() - 1);
            _undonePaint.add(lastPaint);
//          Toast toast = Toast.makeText(getContext(), lastPath.toString() + " " + lastPaint.toString(), Toast.LENGTH_SHORT);
//          toast.show();
            //remove the last one now
            _pathList.remove(_pathList.size() - 1);
            _paintList.remove(_paintList.size() - 1);
            invalidate();
        }
    }

    public void changeBrushStroke(int size) {
        _paintDoodle.setStrokeWidth(size);
    }

    public void changeOpacity(int size) {
        alpha = (int) (size * 2.55);
        _paintDoodle.setAlpha(alpha);
    }

    public void redo() {
        //TODO: put a check to see if there are NO undos!!
        if (!_undonePaint.isEmpty()) {
            //get added paths and paints
            Path addPath = _undonePath.get(_undonePath.size() - 1);
            Paint addPaint = _undonePaint.get(_undonePaint.size() - 1);
            _pathList.add(addPath);
            _paintList.add(addPaint);

            _undonePath.remove(_undonePath.size() - 1);
            _undonePaint.remove(_undonePaint.size() - 1);
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i;
        for(i = 0; i < _pathList.size(); i++) {
            canvas.drawPath(_pathList.get(i), _paintList.get(i));
        }
        canvas.drawPath(_path, _paintDoodle);
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

                RectF dot = new RectF();
                _path.computeBounds(dot, false);

                if(dot.isEmpty()) {
                    makeDot(touchX,touchY);
                }

                _pathList.add(_path);
                Paint p = new Paint();
                p.set(_paintDoodle);
                _paintList.add(p);
                _path = new Path();
                break;
        }

        invalidate();
        return true;
    }

    private void makeDot(float x, float y) {
        _path.moveTo(x,y);
        _path.lineTo(x+1,y+1);
    }



}
