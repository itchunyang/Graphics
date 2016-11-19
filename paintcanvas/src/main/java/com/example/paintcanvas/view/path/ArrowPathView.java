package com.example.paintcanvas.view.path;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;

import com.example.paintcanvas.R;

/**
 * boolean getPosTan (float distance, float[] pos, float[] tan)  是用来判断 Path 的趋势的
 * distance	距离 Path 起点的长度
 * pos	该点的坐标值
 * tan	该点的正切值
 */

public class ArrowPathView extends View {

    private float currVal = 0;//记录当前位置,[0,1]映射整个长度
    private float[] pos;//当前点的实际位置
    private float[] tan;//当前点的tan x,y值
    private Bitmap bitmap;//箭头图片
    private Matrix matrix;
    private Paint paint;
    private Path path;

    public ArrowPathView(Context context) {
        super(context);

        pos = new float[2];
        tan = new float[2];
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        matrix = new Matrix();

        path = new Path();
        path.addCircle(300,300,200, Path.Direction.CCW);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path,paint);
        PathMeasure measure = new PathMeasure(path,false);

        currVal += 0.005;
        if(currVal >=1)
            currVal = 0;

        //不断的增加截取path的长度,坐标保存在pos里,
        measure.getPosTan(measure.getLength() * currVal,pos,tan);

        //atan2是根据正切数值计算出该角度的大小。得到的单位是弧度。
        float degrees = (float) (Math.atan2(tan[1],tan[0]) * 180.0/Math.PI);
        matrix.reset();
        matrix.postRotate(degrees + 180,bitmap.getWidth()/2,bitmap.getHeight()/2);//旋转图片
        matrix.postTranslate(pos[0] - bitmap.getWidth()/2,pos[1] - bitmap.getHeight()/2);//将图片绘制中心调整到与当前点重合
        canvas.drawBitmap(bitmap,matrix,paint);

        invalidate();

    }
}
