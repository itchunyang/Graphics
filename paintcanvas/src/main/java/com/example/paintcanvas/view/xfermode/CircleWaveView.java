package com.example.paintcanvas.view.xfermode;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.paintcanvas.R;

/**
 * Created by luchunyang on 16/8/18.
 */
public class CircleWaveView extends View {
    private Paint paint;
    private int mItemWaveLength = 0;
    private Bitmap src,dst;
    private int dx = 0;

    public CircleWaveView(Context context) {
        this(context,null);
    }

    public CircleWaveView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        src = BitmapFactory.decodeResource(getResources(), R.drawable.circle_shape);
        dst = BitmapFactory.decodeResource(getResources(),R.drawable.wave_bg);
        mItemWaveLength = dst.getWidth();

        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //先画圆
        canvas.drawBitmap(src,0,0,paint);

        int layerID = canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(dst,new Rect(dx,0,dx+src.getWidth(),src.getHeight()),new Rect(0,0,src.getWidth(),src.getHeight()),paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(src,0,0,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }

    public void startAnim(){
        ValueAnimator animator = ValueAnimator.ofInt(0,mItemWaveLength);
        animator.setDuration(4000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int)animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}
