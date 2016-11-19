package com.example.paintcanvas.view.xfermode;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.paintcanvas.R;

/**
 * Created by luchunyang on 16/8/18.
 * AvoidXfermode如果想在高于API 16的机子上进行测试，必须现在应用或手机设置中关闭硬件加速
 */
public class AvoidXfermodeView extends View {

    public AvoidXfermodeView(Context context) {
        this(context,null);
    }

    public AvoidXfermodeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AvoidXfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //如果paint没有设置xformode，则直接覆盖上去，如果设置了xformode，则按照xformode的规则来更新。对于AvoidXfermode而言，这个规则就是先把目标区域中的颜色先清空，再把目标替换上
    //xformode 只会在在两个区域相交的地方起作用！！！！
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sea);
//        int layerID = canvas.saveLayer(0,0,bitmap.getWidth(),bitmap.getHeight(),paint, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bitmap,0,0,paint);

        /**
         * 在TARGET模式下，android会去判断我们的画布上是否存在opColor的地方。如果有，则把该区域染上一层我们paint的颜色。否则不染色。
         *      tolerance容差值表示画布上的像素和我们定义的opColor之间的差别是多少才去染色。
         * 在AVOID模式下，与TARGET模式正好相反。表示与我们指定的opColor是否不一样。
         */
        AvoidXfermode avoidXfermode = new AvoidXfermode(0xffffffff,100, AvoidXfermode.Mode.TARGET);
        paint.setColor(Color.RED);
        paint.setXfermode(avoidXfermode);
        //canvas.drawRect(0,0,bitmap.getWidth(),bitmap.getHeight(),paint);

        //也可以用另外一张图片替换第一张图上上的白色位置
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.panpan),null,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),paint);

//        canvas.restoreToCount(layerID);
    }
}
