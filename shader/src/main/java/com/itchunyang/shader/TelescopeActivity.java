package com.itchunyang.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TelescopeActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TelescopeView(this));

    }

    private class TelescopeView extends View{

        int x = -1, y = -1;
        public TelescopeView(Context context) {
            super(context);
        }

        public TelescopeView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if(bitmap == null){
                Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.juanduobin);
                bitmap = Bitmap.createScaledBitmap(src,getWidth(),getHeight(),true);
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

                paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setShader(shader);
            }

            if(x!=-1 && y != -1)
                canvas.drawCircle(x,y,200,paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getX();
                    y = (int) event.getY();
                    postInvalidate();
                    return true;//注意要返回true
                case MotionEvent.ACTION_MOVE:
                    x = (int) event.getX();
                    y = (int) event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    x = -1;
                    y = -1;
                    break;
            }
            postInvalidate();
            return super.onTouchEvent(event);
        }
    }
}
