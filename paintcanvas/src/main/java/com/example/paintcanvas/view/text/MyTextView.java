package com.example.paintcanvas.view.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luchunyang on 2016/11/21.
 */

public class MyTextView extends View {

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#CCCCCC"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = "abcdefghijkáêЁ!";
        int baseLineX = 100;
        int baseLineY = 100;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#00F5FF"));
        paint.setTextSize(45);

        //绘制基线
        canvas.drawLine(baseLineX, baseLineY, baseLineX + paint.measureText(text), baseLineY, paint);
        canvas.drawLine(baseLineX, baseLineY, baseLineX, baseLineY - 60, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(text, baseLineX, baseLineY, paint);


        //top：可绘制的最高高度所在线 bottom：可绘制的最低高度所在线
        //ascent ：系统建议的，绘制单个字符时，字符应当的最高高度所在线  descent：系统建议的，绘制单个字符时，字符应当的最低高度所在线
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        System.out.println("ascent=" + metrics.ascent + " descent=" + metrics.descent + " top=" + metrics.top + " bottom=" + metrics.bottom+" leading="+metrics.leading);
        //ascent=-32 descent=9 top=-37 bottom=10 leading=0

        int ascent = baseLineY + metrics.ascent;
        int descent = baseLineY + metrics.descent;
        int top = baseLineY + metrics.top;
        int bottom = baseLineY + metrics.bottom;

        paint.setColor(Color.BLUE);
        //ascent和descent线
        canvas.drawLine(baseLineX,descent,baseLineX+800,descent,paint);
        canvas.drawLine(baseLineX,ascent,baseLineX+800,ascent,paint);

        //top 和 bottom线
        paint.setColor(Color.RED);
        canvas.drawLine(baseLineX,top,baseLineX+800,top,paint);
        canvas.drawLine(baseLineX,bottom,baseLineX+800,bottom,paint);

        /************************** 绘制最小矩形 ******************************/

        /**
         * 注意 getTextBounds() 得到的宽度总是比 measureText() 得到的宽度要小一点。
         * 原因看width.png
         */

        String message = "最小矩形abcj";
        baseLineY = 200;
        paint.setColor(Color.BLUE);
        Rect min = new Rect();

        paint.getTextBounds(message,0,message.length(),min);
//        System.out.println(min.left + " " + min.top +" " + min.right + " " + min.bottom);
//        System.out.println("measureText="+paint.measureText(message));
        //获取包裹字符串的最小矩形.是以0,0为baseline来获取的.所以真实的位置要+baselineY！！！
        min.top = min.top+baseLineY;
        min.bottom = min.bottom+baseLineY;
        min.left = min.left+baseLineX;
        min.right = min.right+baseLineX;
        canvas.drawRect(min,paint);

        paint.setColor(Color.RED);
        canvas.drawText(message,baseLineX,baseLineY,paint);


        /************************** 已知中线,获取baseLine ******************************/

        int centerY = 300;
        paint.setColor(Color.BLACK);
        canvas.drawLine(0,centerY,getWidth(),centerY,paint);
        paint.setColor(Color.WHITE);
        //看一下图片FontMetricsInt.jpg   baseline=centerY+A-fm.bottom;
        baseLineY = centerY + (metrics.bottom - metrics.top)/2 - metrics.bottom;
        canvas.drawText("已知中线,获取baseLine",baseLineX,baseLineY,paint);


        /************************** 已知矩形,获取baseLine ******************************/

        Rect target = new Rect(baseLineX,350,baseLineX+550,350 + 80);
        paint.setColor(Color.GRAY);
        canvas.drawRect(target,paint);
        baseLineY = (target.top + target.bottom)/2 + (metrics.bottom - metrics.top)/2 - metrics.bottom;
        paint.setColor(Color.YELLOW);
        paint.setTextAlign(Paint.Align.CENTER);
        //根据上面的公式,先找出centerY
        canvas.drawText("已知矩形,获取baseLine",target.centerX(),baseLineY,paint);
    }
}
