package com.example.paintcanvas.view.layer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luchunyang on 16/8/21.
 */
public class LayerView03 extends View {

    private Paint mPaint;

    public LayerView03(Context context) {
        this(context,null);
    }

    public LayerView03(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(50,50,50,paint);
        /** [Sa * Da, Sc * Da] */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setColor(Color.YELLOW);
        canvas.drawRect(new RectF(50,50,150,150),paint);
    }
}
