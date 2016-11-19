package com.example.paintcanvas.view.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by luchunyang on 16/8/17.
 */
public class HandPathView extends View {

    private Path mPath = new Path();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mPreX,mPreY;

    public HandPathView(Context context) {
        this(context, null);
    }

    public HandPathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(),event.getY());
                mPreX = event.getX();
                mPreY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //直接这么画会有明显的折现痕迹。需要优化.将手指倒数第二个点做维护控制点
                //A-->B-->C 将A和B的中间作为起点，B和C的中间作为终点。B作为控制点。可以看drawable里的图.到结束时A->p0和p1-->C没有画进去。不过距离很少。可以忽略
                //mPath.lineTo(event.getX(),event.getY());

                float endX = (mPreX + event.getX())/2;
                float endY = (mPreY + event.getY())/2;

                mPath.quadTo(mPreX,mPreY,endX,endY);

                mPreX = event.getX();
                mPreY = event.getY();
                postInvalidate();
                break;
        }
        return true;
    }

    public void reset(){
        mPath.reset();
        invalidate();
    }
}
