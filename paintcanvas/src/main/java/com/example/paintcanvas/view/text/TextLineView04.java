package com.example.paintcanvas.view.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luchunyang on 16/8/17.
 */
public class TextLineView04 extends View {

    public static final String text = "同一个世界,同一个梦想,Android";

    public TextLineView04(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextLineView04(Context context) {
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
        //可定一个矩形,画出居中的text
        Rect target = new Rect(50,50,500,150);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(30);
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(target,paint);

        Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();

        int baseLine = (target.bottom + target.top - metricsInt.bottom - metricsInt.top)/2;
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text,target.centerX(),baseLine,paint);
    }
}
