package com.example.paintcanvas.view.layer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;


/**
 * saveLayer()有两个函数
 *
 * 保存指定矩形区域的canvas内容
 * public int saveLayer(RectF bounds, Paint paint, int saveFlags)
 * public int saveLayer(float left, float top, float right, float bottom,Paint paint, int saveFlags)
 *      RectF bounds：要保存的区域的矩形。
 *      int saveFlags：
 *                      public static final int MATRIX_SAVE_FLAG = 0x01;//需要还原Matrix
 *                      public static final int CLIP_SAVE_FLAG = 0x02;//需要还原Clip
 *                      public static final int HAS_ALPHA_LAYER_SAVE_FLAG = 0x04;// 图层的 clip 标记,
 *                      public static final int FULL_COLOR_LAYER_SAVE_FLAG = 0x08;// 图层的 color 标记,
 *                      public static final int CLIP_TO_LAYER_SAVE_FLAG = 0x10;// 图层的 clip 标记,在saveLayer 和 saveLayerAlpha Android强烈建议必须加上他
 *                      public static final int ALL_SAVE_FLAG = 0x1F; //还原所有 一般情况都是使用这个
 */


public class LayerView01 extends View {

    private Paint paint;

    public LayerView01(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //全屏保存
        canvas.drawColor(Color.parseColor("#87ceeb"));
        int level_1 = canvas.save();//save返回的是之前的栈的 数目
        System.out.println("全屏大小="+level_1);

        //第一个裁剪矩形区域(100,100,800,800)
        canvas.clipRect(new Rect(100,100,800,800));
        canvas.drawColor(Color.parseColor("#836fff"));
        int level_2 = canvas.save();//保存的大小为(100,100,800,800)
        System.out.println("(100,100,800,800)="+level_2);

        //第二个裁剪矩形区域(200,200,700,700)
        canvas.clipRect(new Rect(200,200,700,700));
        canvas.drawColor(Color.BLUE);
        int level_3 = canvas.save();//保存的大小为(200,200,700,700)
        System.out.println("(200,200,700,700)="+level_3);

        //第三个裁剪矩形区域(300,300,600,600)
        canvas.clipRect(new Rect(300,300,600,600));
        canvas.drawColor(Color.RED);
        int level_4 = canvas.save();//保存的大小为(300,300,600,600)
        System.out.println("(300,300,600,600)="+level_4);

        //第四个裁剪矩形区域(400,400,500,500),但是状态没有保存!
        canvas.clipRect(new Rect(400,400,500,500));
        canvas.drawColor(Color.WHITE);

        //恢复到(300,300,600,600)
        canvas.restore();
        canvas.drawColor(Color.DKGRAY);

        //恢复到制定level的状态
//        canvas.restoreToCount(level_2);
//        canvas.drawColor(Color.YELLOW);

        //canvas.save().会把当前canvas的状态保存进栈。这个仅仅是保存状态而已！画图还是在原来的canvas上
        //而canvas.saveLayer()后，画图却是在她上面！
    }
}
