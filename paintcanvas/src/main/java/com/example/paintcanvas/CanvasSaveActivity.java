package com.example.paintcanvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * int saveFlags：
 *      public static final int MATRIX_SAVE_FLAG = 0x01;//需要还原Matrix
 *      public static final int CLIP_SAVE_FLAG = 0x02;//需要还原Clip
 *      public static final int HAS_ALPHA_LAYER_SAVE_FLAG = 0x04;// 图层的 clip 标记,
 *      public static final int FULL_COLOR_LAYER_SAVE_FLAG = 0x08;// 图层的 color 标记,
 *      public static final int CLIP_TO_LAYER_SAVE_FLAG = 0x10;// 图层的 clip 标记,在saveLayer 和 saveLayerAlpha Android强烈建议必须加上他
 *      public static final int ALL_SAVE_FLAG = 0x1F; //还原所有 一般情况都是使用这个
 */

public class CanvasSaveActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_save);
        iv = (ImageView) findViewById(R.id.iv);
    }

    /**
     * canvas save()作用是用来保存画布的状态和取出保存的状态的
     * Saves the current matrix and clip onto a private stack.
     *
     * 操作之前调用canvas.save()来保存画布当前的状态，当操作之后取出之前保存过的状态，这样就不会对其他的元素进行影响
     *
     *
     * canvas.save().会把当前canvas的状态保存进栈。这个仅仅是保存状态而已！画图还是在原来的canvas上
     * 而canvas.saveLayer()后，画图却是在她上面！
     */
    public void save(View view) {
        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.RED);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);

        canvas.save();
        canvas.translate(200, 200);
        canvas.drawRect(0, 0, 100, 100, paint);
        canvas.restore();

        canvas.drawRect(0, 0, 100, 100, paint);
        iv.setImageBitmap(bitmap);

    }

    public void save1(View view) {
        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.DKGRAY);
        System.out.println("save id = " + canvas.save());//保存的画布大小为全屏幕大小

        canvas.clipRect(new Rect(100, 100, 500, 500));
        canvas.drawColor(Color.BLUE);//保存画布大小为Rect(100, 100, 500, 500)
        int id = canvas.save();
        System.out.println("save id = " + id);

        canvas.clipRect(new Rect(200, 200, 400, 400));
        canvas.drawColor(Color.GREEN);//保存画布大小为Rect(200,200,400,400)
        System.out.println("save id = " + canvas.save());

//        canvas.restore();
//        canvas.drawColor(Color.YELLOW);

        //回到任何一个save()方法调用之前的状态
        canvas.restoreToCount(id);
        canvas.drawColor(Color.YELLOW);

        iv.setImageBitmap(bitmap);
    }

    public void save2(View view) {
        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.DKGRAY);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);//保存的是Matrix 信息
        canvas.clipRect(100, 100, 200, 200);//进行的是CLIP操作。
        canvas.drawColor(Color.RED);
        canvas.restore();

        canvas.drawColor(Color.BLUE);//restore并没有恢复clip之前的信息,因为没有保存!

        iv.setImageBitmap(bitmap);
    }

    /****************************************************************/
    public void saveLayer(View view) {
        //没有使用saveLayer
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.tbag);
        Bitmap empty = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);
        canvas.drawColor(Color.DKGRAY);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(200,200,200,paint);

        /** [Sa * Da, Sc * Da] */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src,0,0,paint);
        iv.setImageBitmap(empty);
    }

    public void saveLayer1(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.tbag);
        Bitmap empty = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);
        canvas.drawColor(Color.DKGRAY);

        //Paint 不保存
        int layer = canvas.saveLayer(0,0,600,600,null,Canvas.ALL_SAVE_FLAG);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(200,200,200,paint);
        /** [Sa * Da, Sc * Da] */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src,0,0,paint);

        //如果不使用restoreToCount/restore 新的图层不会显示到屏幕上
//        canvas.restore();
        canvas.restoreToCount(layer);

        iv.setImageBitmap(empty);
    }

    public void saveLayer2(View view) {
        Bitmap empty = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(200,200,200,paint);
        /** [Sa * Da, Sc * Da] */   /** [Sa * Da, Sa * Dc] */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        /**
         * Paint 保存
         * This is copied, and is applied to the offscreen when restore() is called.
         * saveLayer()如果传入Paint不为null的话,当restoreToCount被调用后,paint的alpha,ColorFilter和Xfermode这三个属性会被使用!
         *
         * 例如本例子:
         * saveLayer之前设置了xfermode,那么等canvas.restoreToCount后,这个Layer和屏幕合成时采用xfermode规则,否则,则直接覆盖!!!
         */
        int layer = canvas.saveLayer(0,0,600,600,paint,Canvas.ALL_SAVE_FLAG);

        //下面这两句代码也是按照SRC_IN 规则合成的
        canvas.drawColor(Color.YELLOW);
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.tbag);
        canvas.drawBitmap(src,0,0,paint);

        //如果canvas.saveLayer 保存了Paint,并且该Paint设置了SRC_IN,那么这个layer 和 屏幕合成时也采用SRC_IN规则!
        canvas.restoreToCount(layer);

        iv.setImageBitmap(empty);
    }

    public void saveLayer3(View view) {
        Bitmap empty = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(300,300,300,paint);

        paint.setAlpha(140);

        // This is copied, and is applied to the offscreen when restore() is called.
        int id = canvas.saveLayer(0,0,600,600,paint,Canvas.ALL_SAVE_FLAG);

        //saveLayerAlpha 该方法可以开启一个带有透明度的图层，上面绘制的图像都会带有透明度，这样在需要绘制有透明度的图形时比较方便。

        paint.setColor(Color.BLUE);
        canvas.drawCircle(300,300,150,paint);

        paint.setColor(Color.YELLOW);
        canvas.drawCircle(300,300,50,paint);
        canvas.restoreToCount(id);

        iv.setImageBitmap(empty);
    }


    /**
     * http://blog.csdn.net/cquwentao/article/details/51423371
     * FLAG
     * MATRIX_SAVE_FLAG
     *      只保存图层的matrix矩阵
     *      适用方法:save，saveLayer
     *
     * CLIP_SAVE_FLAG
     *      只保存大小信息
     *      适用方法:save，saveLayer
     *
     * HAS_ALPHA_LAYER_SAVE_FLAG
     *      表明该图层有透明度，和下面的标识冲突，都设置时以下面的标志为准
     *      为layer添加一个透明通道，这样一来没有绘制的地方就是透明的，覆盖到上一个layer的时候，就会显示出上一层的图像
     *      适用方法:saveLayer
     *
     * FULL_COLOR_LAYER_SAVE_FLAG
     *      完全保留该图层颜色（和上一图层合并时，先清空上一图层的重叠区域!然后保留该图层的颜色）
     *      会完全展示当前layer的图像，清除掉上一层的重合图像
     *      适用方法:saveLayer
     *
     * CLIP_TO_LAYER_SAVE_FLAG
     *      创建图层时，会把canvas（所有图层）裁剪到参数指定的范围，如果省略这个flag将导致图层开销巨大（实际上图层没有裁剪，与原图层一样大）
     *      这个标识的作用是将canvas裁剪到指定的大小
     *
     * ALL_SAVE_FLAG
     *      保存所有信息
     *      适用方法:save，saveLayer
     *
     */

    public void saveLayerFlag(View view) {
        Bitmap empty = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);
        canvas.drawColor(Color.DKGRAY);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        canvas.saveLayer(0,0,500,500,null,Canvas.MATRIX_SAVE_FLAG|Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,100,paint);
        canvas.restore();

        paint.setColor(Color.BLUE);
        canvas.drawRect(0,0,200,200,paint);
        iv.setImageBitmap(empty);
    }

    public void saveLayerFlag1(View view) {
        /**
         * 先将底色绘制为红色，然后开启新图层，再绘制为绿色，最后将canvas绘制为黑色，
         * 为什么最后不是全屏黑色呢，这里明明restore了，这是因为使用了CLIP_TO_LAYER_SAVE_FLAG标志，
         * 这样一来，canvas被裁剪了，并且无法回复了。这样也就减少了处理的区域，增加了性能。
         */
        Bitmap empty = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);
        canvas.drawColor(Color.RED);

        canvas.saveLayer(200,200,500,500,null,Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        canvas.drawColor(Color.GREEN);
        canvas.restore();
        canvas.drawColor(Color.BLACK);

        iv.setImageBitmap(empty);
    }
}
