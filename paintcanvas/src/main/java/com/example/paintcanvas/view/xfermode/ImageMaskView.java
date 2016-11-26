package com.example.paintcanvas.view.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import com.example.paintcanvas.R;

/**
 * Created by luchunyang on 16/8/18.
 *
 * 我们想仅仅留下人物，取消上面的文字和白色背景该咋办呢？这就要用到了遮罩图片了。
 * 仔细观察遮罩图片,它是和目标图片一样大的!想显示的地方 alpha为1  其余的为0
 */
public class ImageMaskView extends View {

    Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.src);
    Bitmap dst = BitmapFactory.decodeResource(getResources(),R.drawable.dst);
    private Paint mPaint ;

    public ImageMaskView(Context context) {
        super(context);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int layerID = canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);
        // 先绘制dst目标图
        canvas.drawBitmap(dst, 180, 200, mPaint);

//        // 设置混合模式   （只在源图像和目标图像相交的地方绘制目标图像） /** [Sa * Da, Sa * Dc] */
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        // 再绘制src源图
        canvas.drawBitmap(src, 180, 200, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);

        // 还原画布
        canvas.restoreToCount(layerID);

    }
}
