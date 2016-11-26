package com.itchunyang.shader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
    }

    public void start(View view) {
        //裁剪画布
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Drawable drawable = ContextCompat.getDrawable(this,R.drawable.tbag);
        //这个四参数指的是drawable将在被绘制在canvas的哪个矩形区域内。会缩放到目标区域
        drawable.setBounds(0,0,500,500);//不设置bounds不会显示
        drawable.draw(canvas);

        iv.setImageBitmap(bitmap);
    }
}
