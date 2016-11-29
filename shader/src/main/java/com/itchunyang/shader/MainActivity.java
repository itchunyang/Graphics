package com.itchunyang.shader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Shader类专门用来渲染图像以及一些几何图形
 * Shader类包括了5个直接子类 BitmapShader、ComposeShader、LinearGradient、RadialGradient、SweepGradient
 * BitmapShader用于图像渲染；ComposeShader用于混合渲染；LinearGradient用于线性渲染；RadialGradient用于环形渲染；而SweepGradient则用于梯度渲染
 *
 */
public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
    }

    public void linearGradient(View view) {
        /**
         * 参数x0表示渐变的起始点x坐标；参数y0表示渐变的起始点y坐标；参数x1表示渐变的终点x坐标；参数y1表示渐变的终点y坐标；
         * 参数colors表示渐变的颜色数组；
         * 参数positions用来指定颜色数组的相对位置；
         * 参数tile表示平铺方式。
         *      CLAMP的作用是如果渲染器超出原始边界范围，则会复制边缘颜色对超出范围的区域进行着色。
         *      REPEAT的作用是在横向和纵向上以平铺的形式重复渲染位图。
         *      MIRROR的作用是在横向和纵向上以镜像的方式重复渲染位图。
         */

        int[]colors = {Color.RED,Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE};
        float[]position = {0,0.1f,0.5f,0.7f,0.8f};//表示渐变的相对区域,取值0~1
//        LinearGradient linearGradient = new LinearGradient(10,200,200,200, Color.RED,Color.BLUE, Shader.TileMode.REPEAT);
        LinearGradient linearGradient = new LinearGradient(10,200,200,200, colors,position, Shader.TileMode.REPEAT);

        Bitmap bitmap = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setShader(linearGradient);
        canvas.drawRect(0,0,600,600,paint);

        iv.setImageBitmap(bitmap);
    }

    //环形渲染
    public void radialGradient(View view) {
        // 第一个,第二个参数表示渐变圆中心坐标
        // 第三个参数表示半径
        // 第四个,第五个,第六个与线性渲染相同
        RadialGradient radialGradient = new RadialGradient(300,300,300,Color.RED,Color.BLUE, Shader.TileMode.MIRROR);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(radialGradient);

        Bitmap bitmap = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

//        canvas.drawCircle(300,300,300,paint);
        canvas.drawRect(0,0,600,600,paint);

        iv.setImageBitmap(bitmap);
    }

    public void sweepGradient(View view) {
        SweepGradient sweepGradient = new SweepGradient(300,300,Color.RED,Color.BLUE);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(sweepGradient);

        Bitmap bitmap = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

//        canvas.drawCircle(300,300,300,paint);
        canvas.drawRect(0,0,600,600,paint);

        iv.setImageBitmap(bitmap);
    }

    public void composeShader(View view) {
        Shader shader01 = new RadialGradient(200, 200, 200, Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
        Shader shader02 = new SweepGradient(200, 200, new int[] { Color.GREEN, Color.WHITE, Color.GREEN }, null);
        //shader02 = new LinearGradient(10,10,200,200,Color.BLUE,Color.YELLOW, Shader.TileMode.MIRROR);

        ComposeShader composeShader = new ComposeShader(shader01,shader02, PorterDuff.Mode.DARKEN);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(composeShader);
        Bitmap bitmap = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, 400, 400, paint);
        iv.setImageBitmap(bitmap);
    }

    public void bitmapShader(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.panpan);
        BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.MIRROR);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(shader);

        Bitmap bitmap = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.DKGRAY);

        /**
         * shader不是从矩形的原点开始绘制,而是从Bitmap(控件)的原点开始绘制
         * 其实这正说明了一个问题：无论你利用绘图函数绘多大一块，在哪绘制，与Shader无关。
         * 因为Shader总是在控件的左上角开始，而你绘制的部分只是显示出来的部分而已。没有绘制的部分虽然已经生成，但只是不会显示出来罢了。
         */
        canvas.drawCircle(300,300,300,paint);
        iv.setImageBitmap(bitmap);
    }

    public void bitmapShaderMatrix(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.panpan);
        BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.MIRROR);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Matrix matrix = new Matrix();
        matrix.setTranslate(50,50);
        matrix.postRotate(15);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);

        Bitmap bitmap = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.DKGRAY);

        /**
         * shader不是从矩形的原点开始绘制,而是从Bitmap(控件)的原点开始绘制
         * 其实这正说明了一个问题：无论你利用绘图函数绘多大一块，在哪绘制，与Shader无关。
         * 因为Shader总是在控件的左上角开始，而你绘制的部分只是显示出来的部分而已。没有绘制的部分虽然已经生成，但只是不会显示出来罢了。
         */

        canvas.drawRect(0,0,600,600,paint);
        iv.setImageBitmap(bitmap);
    }

    public void telescope(View view) {
        startActivity(new Intent(this,TelescopeActivity.class));
    }
}
