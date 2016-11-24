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

/**
 * Created by luchunyang on 2016/11/23.
 */

public class CirclePathView extends View {

    private Path mPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private Path mDst;
    private float mAnimatorValue;

    public CirclePathView(Context context) {
        this(context,null);
    }

    public CirclePathView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPathMeasure = new PathMeasure();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);
        mPath = new Path();
        mPath.addCircle(400,400,100, Path.Direction.CW);
        mPathMeasure.setPath(mPath,false);
        mDst = new Path();

        final ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animator.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(1400);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDst.reset();

        float stop = mPathMeasure.getLength() * mAnimatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * 320f));

//        mPathMeasure.getSegment(0,stop,mDst,true);
        mPathMeasure.getSegment(start,stop,mDst,true);
        canvas.drawPath(mDst,mPaint);
    }
}
