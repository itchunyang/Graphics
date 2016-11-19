package com.example.paintcanvas.view.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.paintcanvas.R;

/**
 * Created by luchunyang on 16/8/18.
 */
public class EraserView01 extends View {
    private Bitmap src ;
    private Bitmap dst;
    private Paint paint;
    private Path path;
    private float prevX,prevY;

    public EraserView01(Context context) {
        this(context,null);
    }

    public EraserView01(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EraserView01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        src = BitmapFactory.decodeResource(getResources(), R.drawable.han);
        dst  = Bitmap.createBitmap(src.getWidth(),src.getHeight(), Bitmap.Config.ARGB_8888);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int layerID = canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);

        //先把手指轨迹画到目标Bitmap上
        Canvas c = new Canvas(dst);
        c.drawPath(path,paint);

        //然后把目标图像画到画布上
        canvas.drawBitmap(dst,0,0,paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(src,0,0,paint);

        paint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(),event.getY());
                prevX = event.getX();
                prevY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX = (prevX + event.getX())/2;
                float endY = (prevY + event.getY())/2;
                path.quadTo(prevX,prevY,endX,endY);
                prevX = event.getX();
                prevY = event.getY();
                break;
        }

        invalidate();
        return super.onTouchEvent(event);
    }
}
