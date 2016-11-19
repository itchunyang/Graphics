package com.example.paintcanvas.view.layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

/**
 * saveLayer可以为canvas创建一个新的透明图层，在新的图层上绘制，并不会直接绘制到屏幕上，而会在restore之后，绘制到上一个图层或者屏幕上（如果没有上一个图层）。
 * 为什么会需要一个新的图层，例如在处理xfermode的时候，原canvas上的图（包括背景）会影响src和dst的合成，这个时候，使用一个新的透明图层是一个很好的选择。
 * 又例如需要当前绘制的图形都带有一定的透明度，那么创建一个带有透明度的图层，也是一个方便的选择。
 */
public class LayerView02 extends View {

    private int width = 400;
    private int height = 400;
    private Bitmap src;
    private Bitmap dst;
    private Paint paint;

    public LayerView02(Context context) {
        super(context);

        src = makeSrc(width, height);
        dst = makeDst(width, height);
        paint = new Paint();
    }

    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w, h), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(0, 0, w, h, p);
        return bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN);

        //savaLayer后的操作只对新建的画布有效,也就是创建一个新的Layer到栈中，后续的Drawxxx的操作都发生在这个Layer上。layer退栈时，会把本层的图像绘制到上层或者canvas上
        //savaLayer会生产呢过一个新的透明的Bitmap，这个bitmap的大小就是我们指定的大小。
        int layerID = canvas.saveLayer(0, 0,width*2, height*2, paint, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(dst, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, width / 2, height / 2, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerID);

        layerID = canvas.saveLayerAlpha(new RectF(0,600,600,1200),70,Canvas.ALL_SAVE_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(300,900,200,paint);
        canvas.restoreToCount(layerID);

    }

}
