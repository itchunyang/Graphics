package com.example.paintcanvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LayerActivity extends AppCompatActivity {

    private ImageView iv;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer);

        iv = (ImageView) findViewById(R.id.iv);
    }

    public void layer(View view) {
        switch (view.getId()){
            case R.id.layer01:
                layer01();
                break;
            case R.id.layer02:
                layer02();
                break;
            case R.id.layer03:
                layer03();
                break;
        }
    }

    void layer01(){
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.tbag);
        Bitmap empty = Bitmap.createBitmap(400,400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(200,200,200,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src,0,0,paint);
        iv.setImageBitmap(empty);
    }

    void layer02(){
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.tbag);
        Bitmap empty = Bitmap.createBitmap(400,400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(200,200,200,paint);

        /** [Sa * Da, Sa * Dc] */ //DST_IN
        /** [Sa * Da, Sc * Da] */ //SRC_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        /**
         * This is copied, and is applied to the offscreen when restore() is called.
         * saveLayer()如果传入Paint不为null的话,当restoreToCount被调用后,paint的alpha,ColorFilter和Xfermode
         * 这三个属性会被使用!
         *
         * 例如本例子:
         * saveLayer之前设置了xfermode,那么等canvas.restoreToCount后,这个Layer和屏幕合成时采用xfermode规则,否则,则直接覆盖!!!
         * 下面的xfermode无论是 DST_IN 还是 SRC_IN 最终结果都是空白的,但是如果
         * canvas.drawBitmap(src,0,0,paint);这个paint是new Paint()新建的,则结果会不一样
         */

        int layerID = canvas.saveLayer(0,0,400,400,paint,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(src,0,0,paint);
        canvas.restoreToCount(layerID);

        paint.setColor(Color.YELLOW);
        canvas.drawCircle(200,200,50,paint);

        iv.setImageBitmap(empty);
    }

    void layer03(){
        Bitmap empty = Bitmap.createBitmap(400,400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(empty);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(200,200,200,paint);

        paint.setAlpha(140);

        // This is copied, and is applied to the offscreen when restore() is called.
        int id = canvas.saveLayer(0,0,400,400,paint,Canvas.ALL_SAVE_FLAG);

        paint.setColor(Color.BLUE);
        canvas.drawCircle(200,200,100,paint);

        paint.setColor(Color.YELLOW);
        canvas.drawCircle(200,200,50,paint);
        canvas.restoreToCount(id);

        iv.setImageBitmap(empty);
    }

}
