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
public class TextLineView02 extends View {

    public static final String text = "同一个世界,同一个梦想,Android";
    public TextLineView02(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextLineView02(Context context) {
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

        int baseLineX = 0;
        int baseLineY = 200;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(60);

        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        int top = baseLineY + metrics.top;
        int bottom = baseLineY + metrics.bottom;

        //获取字符串的高度
        int height = bottom - top;

        //获取宽度
        int width = (int) paint.measureText(text);

        Rect rect = new Rect(baseLineX,top,baseLineX + width,bottom);
        paint.setColor(Color.GRAY);
        canvas.drawRect(rect,paint);

        Rect minRect = new Rect();
        //获取包裹字符串的最小矩形.是以0,0为baseline来获取的.所以真实的位置要+baselineY！！！
        paint.getTextBounds(text,0,text.length(),minRect);
        System.out.println(minRect.left+" "+minRect.top+" "+minRect.right+" "+minRect.bottom);
        paint.setColor(Color.RED);

        minRect.top = baseLineY + minRect.top;
        minRect.bottom = baseLineY + minRect.bottom;
        canvas.drawRect(minRect,paint);

        paint.setColor(Color.GREEN);
        canvas.drawText(text,baseLineX,baseLineY,paint);

        paint.setColor(Color.BLUE);
        canvas.drawLine(baseLineX,baseLineY,baseLineX + width,baseLineY,paint);
    }
}
