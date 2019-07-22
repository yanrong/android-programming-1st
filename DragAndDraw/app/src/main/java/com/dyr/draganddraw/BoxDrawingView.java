package com.dyr.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class BoxDrawingView extends View {
    public static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<Box>();
    private Paint mBoxPaint;
    private Paint mBackGroundPaint;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    //Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Paint the boxes a nice semitransparent red(ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        //paint the background off-white
        mBackGroundPaint = new Paint();
        mBackGroundPaint.setColor(0xfffeefe0);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //Fill the background
        canvas.drawPaint(mBackGroundPaint);

        for(Box box : mBoxes){
            float left      = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right     = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top       = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom    = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());
        Log.i(TAG, "Received event at x = " + curr.x + ",y = " + curr.y + ":");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "ACTION_DOWN");
                //clear the drawing in before
                if(!mBoxes.isEmpty()){
                    mBoxes.clear();
                    invalidate();
                }
                //reset drawing state
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE");
                if(mCurrentBox != null){
                    mCurrentBox.setCurrent(curr);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "ACTION_CANCEL");
                mCurrentBox = null;
                break;
        }

        return true;
    }
}
