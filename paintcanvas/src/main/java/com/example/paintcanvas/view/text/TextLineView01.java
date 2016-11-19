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
public class TextLineView01 extends View {

    public TextLineView01(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextLineView01(Context context) {
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
        //基准点坐标
        int baseLineX = 0;
        int baseLineY = 200;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(100);

        //Paint.FontMetricsInt与FontMetrics只是得到的值一个是int  一个是float
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float ascent = baseLineY + metrics.ascent;
        float descent = baseLineY + metrics.descent;
        float top = baseLineY + metrics.top;
        float bottom = baseLineY + metrics.bottom;

        System.out.println("ascent="+metrics.ascent+" descent="+metrics.descent+" top="+metrics.top+" bottom="+metrics.bottom);

        //基线
        canvas.drawLine(baseLineX,baseLineY,800,baseLineY,paint);

        //top线
        paint.setColor(Color.parseColor("#cd00cd"));
        canvas.drawLine(baseLineX,top,800,top,paint);

        //ascent
        paint.setColor(Color.parseColor("#4682b4"));
        canvas.drawLine(baseLineX,ascent,800,ascent,paint);

        //descent
        paint.setColor(Color.parseColor("#4682b4"));
        canvas.drawLine(baseLineX,descent,800,descent,paint);

        //bottom
        paint.setColor(Color.parseColor("#cd00cd"));
        canvas.drawLine(baseLineX,bottom,800,bottom,paint);

        paint.setColor(Color.GREEN);
        canvas.drawText("luchunyang java",baseLineX,baseLineY,paint);
    }
}
