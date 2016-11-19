package com.example.paintcanvas.view.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by luchunyang on 16/8/17.
 */
public class WavePathView extends View {

    private Path path;
    private Paint paint;
    private int mItemWaveLength = 800;//波长
    private int dx = 0;
    private int dy = 0;

    public WavePathView(Context context) {
        this(context,null);
    }

    public WavePathView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WavePathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        path = new Path();
        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画一条波浪线,实现方法1
//        path.moveTo(100,300);
//        path.quadTo(200,200,300,300);
//        path.quadTo(400,400,500,300);

        //dx1 控制点坐标 表示相对于上一个终点x坐标的位移值。可负。
        //dy1 控制点坐标 表示相对于上一个终点y坐标的位移值。可负。
        //dx2 终点坐标   表示相对于上一个终点x坐标的位移值。可负。
        //dy2 终点坐标   表示相对于上一个终点y坐标的位移值。可负。
        //实现方法2
//        paint.setColor(Color.BLUE);
//        path.moveTo(100,300);
//        path.rQuadTo(100,-100,200,0);
//        path.rQuadTo(100,100,200,0);


        /***********************************************************************************/

        path.reset();
        int originY = 300;
        int halfWaveLen = mItemWaveLength/2;
        //把起始位置左移一个波长
        //path.moveTo(0,originY);

        //加dx是为了水平动画效果 dy是为了垂直移动
        path.moveTo(-mItemWaveLength+dx,originY+dy);

        dy-=1;
        //dy+=1;


        for (int i = -mItemWaveLength; i < getWidth() + mItemWaveLength; i+=mItemWaveLength) {
            //画的是一个波长中的前半个波
            path.rQuadTo(halfWaveLen/2,-100,halfWaveLen,0);

            //后半个波
            path.rQuadTo(halfWaveLen/2,100,halfWaveLen,0);
        }

        path.lineTo(getWidth(),getHeight());
        path.lineTo(0,getHeight());
        path.close();

        canvas.drawPath(path,paint);
    }

    public void startAnim(){
        ValueAnimator animator = ValueAnimator.ofInt(0,mItemWaveLength);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}
