package com.example.paintcanvas.view.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.example.paintcanvas.R;

/**
 * Created by luchunyang on 16/8/18.
 */
public class LightBookView extends View {

    private Bitmap src ;
    private Bitmap dst;

    public LightBookView(Context context) {
        this(context,null);
    }

    public LightBookView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LightBookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dst = BitmapFactory.decodeResource(getResources(), R.drawable.book_bg);

        //这个图片的上方中间有一些位置是白色半透明的填充。其他位置都是透明的
        src = BitmapFactory.decodeResource(getResources(), R.drawable.book_light);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int layerID = canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(dst,0,0,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(src,0,0,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }
}
