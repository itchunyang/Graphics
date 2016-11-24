package com.example.paintcanvas.view.path;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by luchunyang on 2016/11/22.
 * 这是一个搜索的动效图，通过分析可以得到它应该有四种状态，分别如下:
 *
 * 状态	    概述
 * 初始状态	初始状态，没有任何动效，只显示一个搜索标志 :mag:
 * 准备搜索	放大镜图标逐渐变化为一个点
 * 正在搜索	围绕这一个圆环运动，并且线段长度会周期性变化
 * 准备结束	从一个点逐渐变化成为放大镜图标
 * 效果看图search.gif
 *
 *
 *
 */

public class SearchView extends View {

    private Paint mPaint ;

    // View 宽高
    private int mWidth;
    private int mHeight;

    //放大镜与外部圆环
    private Path path_search;
    private Path path_circle;
    private PathMeasure measure;

    //各个过程的动画
    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;
    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue = 0;

    // 用于控制动画状态转换
    private Handler mAnimatorHandler;
    // 判断是否已经搜索结束
    private boolean isOver = false;
    private int count = 0;

    // 这个视图拥有的状态
    public static enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }
    // 默认的动效周期 2s
    private int defaultDuration = 2000;
    // 当前的状态(非常重要)
    private State mCurrentState = State.NONE;

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
        }
    };
    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // getHandle发消息通知动画状态更新
            mAnimatorHandler.sendEmptyMessage(0);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


    public SearchView(Context context) {
        this(context,null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setBackgroundColor(Color.BLUE);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(8);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        path_search = new Path();
        path_circle = new Path();
        measure = new PathMeasure();

        //放大镜圆环
        RectF oval1 = new RectF(-50,-50,50,50);
        path_search.addArc(oval1,45,359.9f);

        //外部圆环
        RectF oval2 = new RectF(-100,-100,100,100);
        path_circle.addArc(oval2,45,-359.9f);

        //放大镜把手位置
        float[] pos = new float[2];
        measure.setPath(path_circle,false);
        measure.getPosTan(0,pos,null);

        //放大镜把手
        path_search.lineTo(pos[0],pos[1]);

        mStartingAnimator = ValueAnimator.ofFloat(0,1).setDuration(defaultDuration);
        mSearchingAnimator = ValueAnimator.ofFloat(0,1).setDuration(defaultDuration);
        mEndingAnimator = ValueAnimator.ofFloat(1,0).setDuration(defaultDuration);
        mStartingAnimator.addUpdateListener(mUpdateListener);
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addUpdateListener(mUpdateListener);
        mStartingAnimator.addListener(mAnimatorListener);
        mSearchingAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);

        mAnimatorHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (mCurrentState){
                    case STARTING:
                        isOver = false;
                        mCurrentState = State.SEARCHING;
                        mStartingAnimator.removeAllListeners();
                        mSearchingAnimator.start();
                        break;

                    case SEARCHING:
                        if (!isOver) {  // 如果搜索未结束 则继续执行搜索动画
                            mSearchingAnimator.start();
                            Log.e("Update", "RESTART");

                            count++;
                            if (count>2){       // count大于2则进入结束状态
                                isOver = true;
                            }
                        } else {        // 如果搜索已经结束 则进入结束动画
                            mCurrentState = State.ENDING;
                            mEndingAnimator.start();
                        }
                        break;

                    case ENDING:
                        // 从结束动画转变为无状态
                        mCurrentState = State.NONE;
                        break;
                }
            }
        };

        // 进入开始动画
        mCurrentState = State.STARTING;
        mStartingAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearch(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }


    public void drawSearch(Canvas canvas){

        canvas.translate(mWidth/2,mHeight/2);
        canvas.drawColor(Color.parseColor("#0082D7"));
        switch (mCurrentState){
            case NONE:
                canvas.drawPath(path_search,mPaint);
                break;

            case STARTING:
                measure.setPath(path_search,false);
                Path dst = new Path();
                measure.getSegment(measure.getLength()* mAnimatorValue,measure.getLength(),dst,true);
                canvas.drawPath(dst,mPaint);
                break;

            case SEARCHING:
                measure.setPath(path_circle,false);
                Path dst2 = new Path();
                float stop = measure.getLength() * mAnimatorValue;
                float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * 200f));
                measure.getSegment(start,stop,dst2,true);
                canvas.drawPath(dst2, mPaint);
                break;

            case ENDING:
                measure.setPath(path_search,false);
                Path dst3 = new Path();
                System.out.println(measure.getLength() * mAnimatorValue);
                measure.getSegment(measure.getLength() * mAnimatorValue,measure.getLength(),dst3,true);
                canvas.drawPath(dst3, mPaint);
                break;
        }
    }
}
