package com.example.paintcanvas.view.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luchunyang on 16/8/17.
 */
public class TextLineView03 extends View {

    public static final String text = "同一个世界,同一个梦想,Android";

    public TextLineView03(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextLineView03(Context context) {
        super(context);
    }


    /**
     * ascent 绘制单个字符时，字符应当的最高高度所在线
     * descent 绘制单个字符时，字符应当的最低高度所在线
     * top 可绘制的最高高度所在线
     * bottom 可绘制的最低高度所在线
     * metrics.ascent = ascent线的y坐标 - baseline的y坐标
     * metrics.descent = descent的y坐标-baseline的y坐标
     * metrics.top = top线的y坐标 - baseline的y坐标
     * metrics.bottom = bottom线的y坐标 - baseline的y坐标
     */

    @Override
    protected void onDraw(Canvas canvas) {
        //根据给定左上角(left,top)。画出文字

        int left = 0;
        int top = 200;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(60);
        canvas.drawLine(left,top,800,top, paint);

        //计算baseline的位置
        //Paint.FontMetricsInt与FontMetrics只是得到的值一个是int  一个是float
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        int baseLineY = top - metrics.top;

        int bottom = baseLineY + metrics.bottom;
        canvas.drawLine(0,bottom,800,bottom,paint);

        paint.setColor(Color.BLUE);
        canvas.drawLine(0,baseLineY,800,baseLineY,paint);

        paint.setColor(Color.DKGRAY);
        canvas.drawText(text,0,baseLineY,paint);

        //也可以根据给出的中间线计算出baseline
        //baseline = center + (metrics.bottom - metrics.top)/2 - metrics.bottom

    }
}
