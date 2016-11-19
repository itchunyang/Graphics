package com.example.paintcanvas.view.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by luchunyang on 16/8/17.
 */
public class HeartPathView extends View {

    private int PATH_WIDTH = 2;
    private int[] START_POINT = {300,270};
    private int[] BOTTOM_POINT = {300,400};
    private int[] LEFT_CONTROL_POINT = {450,200};
    private int[] RIGHT_CONTROL_POINT = {150,200};

    private Paint mPaint;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private float[] curPos;

    public HeartPathView(Context context) {
        this(context,null);
    }

    public HeartPathView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeartPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

        mPath = new Path();
        mPath.moveTo(START_POINT[0],START_POINT[1]);
        mPath.quadTo(RIGHT_CONTROL_POINT[0],RIGHT_CONTROL_POINT[1],BOTTOM_POINT[0],BOTTOM_POINT[1]);
        mPath.quadTo(LEFT_CONTROL_POINT[0],LEFT_CONTROL_POINT[1],START_POINT[0],START_POINT[1]);

        mPathMeasure = new PathMeasure(mPath,true);
        curPos = new float[]{START_POINT[0],START_POINT[1]};

        startPathAnim(2000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mPath,mPaint);

        canvas.drawCircle(LEFT_CONTROL_POINT[0],LEFT_CONTROL_POINT[1],5,mPaint);
        canvas.drawCircle(RIGHT_CONTROL_POINT[0],RIGHT_CONTROL_POINT[1],5,mPaint);

        canvas.drawCircle(curPos[0],curPos[1],10,mPaint);
    }

    public void startPathAnim(long duration){
        ValueAnimator animator = ValueAnimator.ofFloat(0,mPathMeasure.getLength());
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value,curPos,null);
                postInvalidate();
            }
        });

        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        animator.start();
    }
}
