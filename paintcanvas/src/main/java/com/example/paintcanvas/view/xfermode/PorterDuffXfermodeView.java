package com.example.paintcanvas.view.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luchunyang on 16/8/18.
 */
public class PorterDuffXfermodeView extends View {

    private Bitmap src ;
    private Bitmap dst;
    private int width = 100;
    private int height = 100;

    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };

    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "SrcOver",
            "DstOver", "SrcIn", "DstIn", "SrcOut",
            "DstOut", "SrcATop", "DstATop", "Xor",
            "Darken", "Lighten", "Multiply", "Screen"
    };

    private static final int ROW_MAX = 4;   // number of samples per row

    public PorterDuffXfermodeView(Context context) {
        this(context,null);
    }

    public PorterDuffXfermodeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PorterDuffXfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        src = makeSrc(width,height);
        dst = makeDst(width,height);
    }

    private Bitmap makeDst(int width, int height) {
        Bitmap empty = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xffffcc44);
        canvas.drawOval(new RectF(0,0,width,height),p);
        return empty;
    }

    private Bitmap makeSrc(int width, int height) {
        Bitmap empty = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xff66aaff);//蓝色
        canvas.drawRect(new RectF(0,0,width,height),p);
        return empty;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawColor(Color.DKGRAY);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);

        //加这句，是为了最上面的文字显示出来！
        canvas.translate(0,30);

        int x=0;
        int y=0;

        for (int i = 0; i < sModes.length; i++) {
//            int layerID = canvas.saveLayer(0,0,width*2,height*2,paint, Canvas.ALL_SAVE_FLAG);
            int layerID = canvas.saveLayer(x,y,x+width*2,
                    y+height*2,paint, Canvas.ALL_SAVE_FLAG);

            canvas.translate(x,y);
            canvas.drawBitmap(dst,0,0,paint);
            paint.setXfermode(sModes[i]);
            canvas.drawBitmap(src,width/2,height/2,paint);
            paint.setXfermode(null);
            canvas.restoreToCount(layerID);

            canvas.drawText(sLabels[i],
                    x + width/2, y - paint.getTextSize()/2, paint);

            x += width*2+10;
            if((i % ROW_MAX) == ROW_MAX - 1){
                x = 0;
                y += height*2+30;
            }

        }
    }
}
