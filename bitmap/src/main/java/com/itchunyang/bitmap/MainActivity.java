package com.itchunyang.bitmap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.itchunyang.bitmap.IMG.IMG_640x963;
import static com.itchunyang.bitmap.IMG.names;

public class MainActivity extends AppCompatActivity {

    private String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //decodeFile->decodeStream
    public void decodeFile(View view) {
        popBitmap(BitmapFactory.decodeFile(SDCARD + File.separator + names[IMG.IMG_860x1290]));
    }

    //decodeResource->decodeResourceStream->decodeStream
    public void decodeResource(View view) {
        popBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_658x877));
    }

    public void decodeByte(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_658x877);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, bos);
        byte[] data = bos.toByteArray();

        popBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    //decodeResourceStream->decodeStream
    public void decodeResourceStream(View view) {
        InputStream is = getResources().openRawResource(R.raw.img_700x1030);
        popBitmap(BitmapFactory.decodeResourceStream(getResources(), null, is, null, null));
    }

    //最底层的方法,其他方法(decodeByte除外)都是最终调用此方法
    //decodeStream直接拿图片来读取字节码,不会根据机器的分辨率来自适应
    public void decodeStream(View view) {
        try {
            popBitmap(BitmapFactory.decodeStream(new FileInputStream(SDCARD + File.separator + IMG.names[IMG_640x963])));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //copy images to the sdcard
    private void init() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        boolean init = sp.getBoolean("init", false);
        if (!init) {
            byte[] data = new byte[1024];

            for (int i = 0; i < IMG.ids.length; i++) {
                try {
                    InputStream is = getResources().openRawResource(IMG.ids[i]);
                    FileOutputStream fos = new FileOutputStream(SDCARD + "/" + names[i]);
                    int len;
                    while ((len = is.read(data)) != -1) {
                        fos.write(data, 0, len);
                    }
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sp.edit().putBoolean("init", true);
        }
    }

    private void popBitmap(final Bitmap bitmap) {
        Log.i(TAG, "popBitmap: [width=" + bitmap.getWidth() + ",height=" + bitmap.getHeight() + "]");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(bitmap);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                bitmap.recycle();
                System.gc();
            }
        });
        dialog.getWindow().setContentView(iv);

//        PopupWindow p = new PopupWindow(iv, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        p.setContentView(iv);
//        p.setFocusable(true);
//        p.setOutsideTouchable(true);
//        p.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//        p.showAtLocation(findViewById(R.id.activity_main), Gravity.CENTER,0,0);
//        p.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                bitmap.recycle();
//                System.gc();
//            }
//        });
    }

    /************************  BitmapFactory.Options 测试  ************************/

    /**
     * 一张图片占用多少内存?这里的图片占用内存是指在Navtive中占用的内存,Bitmap对象在不使用时,我们应该先调用 recycle() ，然后才它设置为null.这样才能释放native部分的内存
     * 一张图片（BitMap）占用的 内存 = 图片长度(像素) * 图片宽度(像素) * 单位像素占用的字节数
     * <p>
     * 单位像素占用的字节数---> 由BitmapFactory.Options 的 inPreferredConfig 变量决定。
     * ALPHA_8:此时图片只有alpha值，没有RGB值，一个像素占用一个字节
     * ARGB_4444:一个像素占用2个字节，alpha(A)值，Red（R）值，Green(G)值，Blue（B）值各占4个bites
     * ARGB_8888:一个像素占用4个字节，alpha(A)值，Red（R）值，Green(G)值，Blue（B）值各占8个bites
     * RGB_565:一个像素占用2个字节，没有alpha(A)值，即不支持透明和半透明，Red（R）值占5个bites,Green(G)值 占6个bites,Blue（B）值占5个bites
     *
     * 同样的尺寸，png格式的图片要比jpg图片大很多。（png中有透明通道，而jpg中没有，此外png是无损压缩的，而jpg是有损压缩的，所以png中存储的信息会很多，体积自然就大了
     */
    public void origin(View view) {
        try {
            FileInputStream fis = new FileInputStream(SDCARD + File.separator + IMG.names[IMG.IMG_700x1030]);
            popBitmap(BitmapFactory.decodeStream(fis));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Option 参数类：
     * public boolean inJustDecodeBounds//如果设置为true，不获取图片，不分配内存，但会返回图片的高度宽度信息。
     * public int inSampleSize//采样率
     * public int outWidth//获取图片的宽度值
     * public int outHeight//获取图片的高度值
     * public int inDensity//用于位图的像素压缩比(Bitmap的像素密度)
     * public int inTargetDensity//表示的是目标Bitmap即将被画到屏幕上的像素密度（每英寸有多少个像素）// //Bitmap最终的像素密度(注意，inDensity，inTargetDensity影响图片的缩放度)
     * public byte[] inTempStorage //解码时的临时空间，建议16*1024
     * public boolean inScaled//是否支持缩放，默认为true，当设置了这个，Bitmap将会以inTargetDensity的值进行缩放
     * public boolean inDither //如果为true,解码器尝试抖动解码
     * public Bitmap.Config inPreferredConfig //设置解码器
     * public String outMimeType //设置解码图像
     * public boolean inPurgeable//当存储Pixel的内存空间在系统内存不足时是否可以被回收
     * public boolean inInputShareable //inPurgeable为true情况下才生效，是否可以共享一个InputStream
     * public boolean inPreferQualityOverSpeed  //为true则优先保证Bitmap质量其次是解码速度
     * public boolean inMutable //配置Bitmap是否可以更改，比如：在Bitmap上隔几个像素加一条线段
     * public int inScreenDensity //当前屏幕的像素密度
     * public Bitmap inBitmap; 这个参数用来实现Bitmap内存的复用
     */
    public void getBounds(View view) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeResource(getResources(), R.drawable.meinv_2400x1600, op);
            // width=2400 height=1600
            Log.i(TAG, "getBounds: width=" + op.outWidth + " height=" + op.outHeight);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.meinv_2400x1600);
            //1600 1067  Bitmap被加载进来了,就会缩放的。
            Log.i(TAG, "getBounds: " + bitmap.getWidth() + " " + bitmap.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void simpleSize(View view) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = 2;

        try {
            FileInputStream fis = new FileInputStream(SDCARD + File.separator + IMG.names[IMG.IMG_700x1030]);
            Bitmap bitmap = BitmapFactory.decodeStream(fis, null, option);//350 515
            popBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void simpleSize1(View view) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;

        try {
            FileInputStream fis = new FileInputStream(SDCARD + File.separator + IMG.names[IMG.IMG_700x1030]);
            BitmapFactory.decodeStream(fis,null,option);
            option.inSampleSize = calculateInSimpleSize(option,400,400);
            option.inJustDecodeBounds = false;

            fis = new FileInputStream(SDCARD + File.separator + IMG.names[IMG.IMG_700x1030]);
            Bitmap bitmap = BitmapFactory.decodeStream(fis,null,option);
            Log.i(TAG, "simpleSize1: byteCount="+bitmap.getByteCount() +" inSampleSize="+option.inSampleSize);

            popBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * simpleSize 必须是2的倍数
     * simpleSize 有一个局限，就是只能对图片进行等比例的放大或者缩小，不能按任意大小进行缩放，不过这还是适用大部分的情况
     * 这种方式只需要把图片一次读取到内存就可以了.效率高
     */
    private int calculateInSimpleSize(BitmapFactory.Options option,int reqWidth,int reqHeight) {
        final int srcWidth = option.outWidth;
        final int srcHeight = option.outHeight;
        int simpleSize = 1;

        if(srcHeight > reqHeight || srcWidth > reqWidth){
            while ((srcWidth / simpleSize) > reqWidth && (srcHeight / simpleSize) > reqHeight){
                simpleSize *= 2;
            }
        }

        return simpleSize;
    }

    public void scale(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.meinv_2400x1600,options);
        Log.i(TAG, "inTargetDensity: "+options.inTargetDensity);//320目标屏幕像素dpi
        Log.i(TAG, "inDensity: "+options.inDensity);//480 bitmap所使用的像素密度
        Log.i(TAG, "inScreenDensity: "+options.inScreenDensity);//0
        Log.i(TAG, "inScaled: "+options.inScaled);//true

        //这里若options.inJustDecodeBounds 为true，则 outWidth 表示没有经过Scale的Bitmap的原始宽高（即我们通过图片编辑软件看到的宽高），否则则为加载到内存后，真实的Bitmap宽高（经过Scale之后的宽高）。
        //width=1600,height=1067
        Log.i(TAG, "outWidth="+options.outWidth +" height="+options.outHeight);
        popBitmap(bitmap);
    }

    public void scale1(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inTargetDensity = getResources().getDisplayMetrics().densityDpi;
        options.inDensity = 320;
        options.inScaled = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.meinv_2400x1600,options);
        //width=2400,height=1600  不缩放 因为inTargetDensity = inDensity
        Log.i(TAG, "width="+bitmap.getWidth()+" height="+bitmap.getHeight());
        popBitmap(bitmap);
    }

    public void scale2(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inTargetDensity = getResources().getDisplayMetrics().densityDpi;//320
        options.inDensity = 640;
//        options.inScaled = false;
        options.inScaled = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.meinv_2400x1600,options);
        //width=1200,height=800
        Log.i(TAG, "width="+bitmap.getWidth()+" height="+bitmap.getHeight());
        popBitmap(bitmap);
    }

    public void scale3(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.meinv_2400x1600);
        Bitmap bitmap = Bitmap.createScaledBitmap(src,600,600,false);
        popBitmap(bitmap);
    }

    public void create(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.img_1024x1536);

        Matrix matrix = new Matrix();
        matrix.postRotate(30);
//        matrix.postScale(1.0f,0.5f);
        /**
         *  surce 用来剪裁的图片源;
         *  x：剪裁x方向的起始位置;
         *  y：剪裁y方向的起始位置
         *  width：剪裁的宽度
         *  height：剪裁的高度;
         *  filter: true 可以进行滤波处理,有助于改善图像质量
         *  必须满足条 x+width<=bitmap.width()
         */

        //先裁剪再做matrix变换
        Bitmap bitmap = Bitmap.createBitmap(src,0,0,800,800,matrix,true);
        popBitmap(bitmap);
    }

    public void compress(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.img_1024x1536);
        try {
            FileOutputStream fos = new FileOutputStream(SDCARD+File.separator+"img_1024x1536.png");
            boolean ok = src.compress(CompressFormat.PNG,100,fos);
            Log.i(TAG, "compress: "+ok);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void copy(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.img_1024x1536);
        Bitmap bitmap = src.copy(Bitmap.Config.ARGB_8888,true);
        if(src == bitmap)
            Log.i(TAG, "copy: bitmap = src");
        else
            src.recycle();
        popBitmap(bitmap);
    }

    public void drawBitmap(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.img_640x1266);
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(),src.getHeight(), Bitmap.Config.ARGB_8888);

        //把原图的某一区域铺满到目标的某一区域!
        Rect srcRect = new Rect(0,0,640,1266);
        Rect dstRect = new Rect(150,150,bitmap.getWidth(),bitmap.getHeight());

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.DKGRAY);
        canvas.drawBitmap(src,srcRect,dstRect,new Paint(Paint.ANTI_ALIAS_FLAG));

        popBitmap(bitmap);
    }

    public void drawBitmap1(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.img_640x1266);
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(),src.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.DKGRAY);

        Matrix matrix = new Matrix();
        matrix.setRotate(15);

        //设置Matrix以(px,py)为轴心进行缩放，sx、sy为X、Y方向上的缩放比例
        matrix.postScale(1.0f,2.0f);

        //不加旋转会有锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(src,matrix,new Paint(Paint.ANTI_ALIAS_FLAG));
        popBitmap(bitmap);
    }

    public void bitmapToDrawable(Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(getResources(),bitmap);
    }

    public void drawableToBitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);

        popBitmap(bitmap);
    }
}
