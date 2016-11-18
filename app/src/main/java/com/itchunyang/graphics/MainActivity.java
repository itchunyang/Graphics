package com.itchunyang.graphics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void color(View view) {
        ColorDrawable drawable = new ColorDrawable(Color.BLUE);
        iv.setBackground(drawable);
    }


    /**
     * xml里面的shape标签对应的是GradientDrawable 而不是ShapeDrawable
     */
    public void shape(View view) {
        Shape shape;
        shape = new OvalShape();
        shape = new RectShape();

        //外部矩形弧度,每两位代表一个圆角
        float[] outerR = {12, 12, 12, 12, 12, 12, 60, 30};
        //内部矩形弧度
        float[] innerR = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
        //inset 指定外部矩形4条边与内部矩形四条边的矩形,没有设置为null
        shape = new RoundRectShape(outerR, new RectF(30, 40, 50, 60), innerR);

        shape = new ArcShape(30, 260);

        Path path = new Path();
        path.moveTo(50, 0);
        path.lineTo(0, 50);
        path.lineTo(50, 100);
        path.lineTo(100, 50);
        path.lineTo(50, 0);

        //stdWidth 标准的宽度。onResize()源码,控件的实际大小/你希望的大小 = scale,path的坐标要乘以这个scale为最终布局
//        shape = new PathShape(path, 100, 150);

        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.getPaint().setColor(Color.RED);
        iv.setBackground(drawable);
    }

    public void levelList(View view) {
        LevelListDrawable drawable = new LevelListDrawable();
        drawable.addLevel(0, 10, ContextCompat.getDrawable(this, R.drawable.panpan1));
        drawable.addLevel(20, 30, ContextCompat.getDrawable(this, R.drawable.panpan2));
        drawable.addLevel(31, 40, ContextCompat.getDrawable(this, R.drawable.panpan3));
        iv.setImageDrawable(drawable);
        //xml level-list
//        iv.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.level_list_drawable));

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv = (ImageView) v;
                int level = iv.getDrawable().getLevel() + 5;
                //iv.setImageLevel();也是一样的
                iv.getDrawable().setLevel(level);
            }
        });

    }

    /**
     * 每个Drawable都位于不同的层
     *
     * @param view
     */
    public void layer(View view) {
        Drawable[] layers = new Drawable[3];
        layers[0] = new ColorDrawable(Color.RED);
        layers[1] = new ColorDrawable(Color.GREEN);
        layers[2] = new ColorDrawable(Color.BLUE);
        LayerDrawable drawable = new LayerDrawable(layers);

        //设置layers[]索引的上下左右的外边距
        drawable.setLayerInset(0, 5, 5, 5, 5);
        drawable.setLayerInset(1, 10, 10, 10, 10);
        drawable.setLayerInset(2, 15, 15, 15, 15);
        iv.setImageDrawable(drawable);
        //xml对应 layer-list
//        iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.layer_list_drawable));
    }

    public void scale(View view) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.panpan3);

        //scaleWidth代表缩小了原来的百分比。 1f代表缩没了
        ScaleDrawable scaleDrawable = new ScaleDrawable(drawable, Gravity.LEFT | Gravity.TOP, 0f, 0f);
        iv.setImageDrawable(scaleDrawable);

        //xml对应scale
//        iv.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.scale_drawable));

        //ScaleDrawable的draw()方法判断getLeve() != 0才绘制。
        iv.getDrawable().setLevel(1);
    }

    public void bitmap(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.white_girl);
        BitmapDrawable drawable = new BitmapDrawable(getResources(),bitmap);
        iv.setImageDrawable(drawable);

        //xml对应bitmap
//        iv.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bitmap_drawable));
    }

    /**
     * 表示把一个Drawable嵌入到另外的一个Drawable,并且在内部留一些间距
     * 当控件需要的背景比实际的边框小的时候,比较合适使用InsetDrawable
     * 没什么大用 LayerDrawable也可以实现
     */
    public void inset(View view) {
        //insetTop 图像距离上边的距离
        InsetDrawable drawable = new InsetDrawable(ContextCompat.getDrawable(this,R.drawable.white_girl),60,60,60,60);
        iv.setImageDrawable(drawable);

        //xml对应的是inset
//        iv.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inset_drawable));
    }

    public void rotate(View view) {
        //pivotX 百分比。  旋转的中心在图片X轴的百分比. level最大为10000,对应的度数是你设置的
        RotateDrawable drawable = (RotateDrawable) ContextCompat.getDrawable(this,R.drawable.rotate_drawable);
        iv.setImageDrawable(drawable);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int level = iv.getDrawable().getLevel();

                if(level >= 10000)
                    return;

                iv.getDrawable().setLevel( level + 100);
                handler.postDelayed(this,10);
            }
        },300);
    }

    public void clip(View view) {
        //level 值范围在[0,10000],android:gravity 指定截取时的对齐方式
        //android:clipOrientation：指定截取的方向，可设置为水平截取或垂直截取
        ClipDrawable drawable = new ClipDrawable(ContextCompat.getDrawable(this,R.drawable.white_girl),Gravity.CENTER,ClipDrawable.VERTICAL);
        iv.setImageDrawable(drawable);

        //xml对应的是clip
//        iv.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.clip_drawable));
        iv.setImageLevel(1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int level = iv.getDrawable().getLevel();
                if(level > 10000)
                    return;
                iv.setImageLevel(level + 100);
                handler.postDelayed(this,10);
            }
        },50);
    }

    //一个TransitionDrawable可以实现两个Drawable资源之间淡入淡出的效果
    public void transition(View view) {
        Drawable[] layers = new Drawable[2];
        layers[0] = ContextCompat.getDrawable(this,R.drawable.panpan3);
        layers[1] = ContextCompat.getDrawable(this,R.drawable.white_girl);
        final TransitionDrawable drawable = new TransitionDrawable(layers);
        iv.setImageDrawable(drawable);
        drawable.startTransition(2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //切换回去
                drawable.reverseTransition(2000);
            }
        },2100);

        //xml对应transition
//        iv.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.transition_drawable));
//        TransitionDrawable transitionDrawable = (TransitionDrawable) iv.getDrawable();
//        transitionDrawable.startTransition(3000);
    }

    public void animation(View view) {
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(ContextCompat.getDrawable(this,R.drawable.panpan2),2000);
        animationDrawable.addFrame(ContextCompat.getDrawable(this,R.drawable.panpan3),2000);
        animationDrawable.addFrame(ContextCompat.getDrawable(this,R.drawable.panpan4),2000);
        animationDrawable.setOneShot(false);//true 这个动画只会循环一次并且停留在最后
        iv.setImageDrawable(animationDrawable);
        animationDrawable.start();

        //xml对应animation-list
//        iv.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.animation_drawable));
//        AnimationDrawable drawable = (AnimationDrawable) iv.getDrawable();
//        drawable.start();
    }

    public void sateList(View view) {
        //定义类了不同状态下与之对应的图片资源
//        StateListDrawable drawable = new StateListDrawable();

        //xml对应的是selector
    }


    public void gradient(View view) {
        startActivity(new Intent(this,GradientActivity.class));
    }
}
