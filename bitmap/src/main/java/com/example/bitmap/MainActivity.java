package com.example.bitmap;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    //初始化一些需要的图片
    private void init() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        boolean init = sp.getBoolean("init",false);

        if(!init){
            for (int i = 0; i < ImageUtil.image.length; i++) {
                try {
                    InputStream is = getResources().openRawResource(ImageUtil.id[i]);
                    FileOutputStream fos = new FileOutputStream(SDCARD+"/"+ImageUtil.image[i]);

                    byte [] data = new byte[1024];
                    int len;

                    while ((len = is.read(data)) != -1){
                        fos.write(data,0,len);
                    }

                    is.close();
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            sp.edit().putBoolean("init",true);
        }

        iv = (ImageView) findViewById(R.id.iv);
    }

    //decodeFile-->decodeStream
    public void decodeFile(View view) {
        Bitmap bitmap = BitmapFactory.decodeFile(SDCARD+"/"+ImageUtil.image[ImageUtil.AV_BODUOYEJIEYI]);
        iv.setImageBitmap(bitmap);
    }

    //decodeResource-->decodeResourceStream-->decodeStream
    public void decodeResource(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.chongtain_683x1024jpg);
        iv.setImageBitmap(bitmap);
    }

    public void decodeByte(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.jinxiaomao_684x1024);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] data = bos.toByteArray();

        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        iv.setImageBitmap(bitmap);
    }

    //decodeResourceStream-->decodeStream
    public void decodeResourceStream(View view) {
        InputStream is = getResources().openRawResource(+R.drawable.lienhui_580x870);
        Bitmap bitmap = BitmapFactory.decodeResourceStream(getResources(),null,is,null,null);
        iv.setImageBitmap(bitmap);
    }

    //最底层的方法,其他方法(decodeByte除外)都是最终调用此方法
    //decodeStream直接拿的图片来读取字节码了， 不会根据机器的各种分辨率来自动适应
    public void decodeStream(View view) {
        try {
            FileInputStream is = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + ImageUtil.image[ImageUtil.WANGLIDANNI_645x949]);
            //InputStream is = getResources().openRawResource(R.raw.boduo_439x590);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            iv.setImageBitmap(bitmap);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*********************************  BitmapFactory.Options 测试  *****************************************/

    /**
     *一张图片占用多少内存?这里的图片占用内存是指在Navtive中占用的内存,Bitmap对象在不使用时,我们应该先调用 recycle() ，然后才它设置为null.这样才能释放native部分的内存
     * 一张图片（BitMap）占用的 内存 = 图片长度(像素) * 图片宽度(像素) * 单位像素占用的字节数
     *
     * 单位像素占用的字节数---> 由BitmapFactory.Options 的 inPreferredConfig 变量决定。
     * ALPHA_8:此时图片只有alpha值，没有RGB值，一个像素占用一个字节
     * ARGB_4444:一个像素占用2个字节，alpha(A)值，Red（R）值，Green(G)值，Blue（B）值各占4个bites
     * ARGB_8888:一个像素占用4个字节，alpha(A)值，Red（R）值，Green(G)值，Blue（B）值各占8个bites
     * RGB_565:一个像素占用2个字节，没有alpha(A)值，即不支持透明和半透明，Red（R）值占5个bites,Green(G)值 占6个bites,Blue（B）值占5个bites
     */
    public void originBitmap(View view) {
        try {
            FileInputStream is = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + ImageUtil.image[ImageUtil.JUANDUOBIN_800x1140]);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            iv.setImageBitmap(bitmap);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  Option 参数类：
     *  public boolean inJustDecodeBounds//如果设置为true，不获取图片，不分配内存，但会返回图片的高度宽度信息。
     *  public int inSampleSize//采样率
     *  public int outWidth//获取图片的宽度值
     *  public int outHeight//获取图片的高度值
     *  public int inDensity//用于位图的像素压缩比(Bitmap的像素密度)
     *  public int inTargetDensity//表示的是目标Bitmap即将被画到屏幕上的像素密度（每英寸有多少个像素）// //Bitmap最终的像素密度(注意，inDensity，inTargetDensity影响图片的缩放度)
     *  public byte[] inTempStorage //解码时的临时空间，建议16*1024
     *  public boolean inScaled//是否支持缩放，默认为true，当设置了这个，Bitmap将会以inTargetDensity的值进行缩放
     *  public boolean inDither //如果为true,解码器尝试抖动解码
     *  public Bitmap.Config inPreferredConfig //设置解码器
     *  public String outMimeType //设置解码图像
     *  public boolean inPurgeable//当存储Pixel的内存空间在系统内存不足时是否可以被回收
     *  public boolean inInputShareable //inPurgeable为true情况下才生效，是否可以共享一个InputStream
     *  public boolean inPreferQualityOverSpeed  //为true则优先保证Bitmap质量其次是解码速度
     *  public boolean inMutable //配置Bitmap是否可以更改，比如：在Bitmap上隔几个像素加一条线段
     *  public int inScreenDensity //当前屏幕的像素密度
     *  public Bitmap inBitmap; 这个参数用来实现Bitmap内存的复用
     */

    public void justDecodeBounds(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            FileInputStream is = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + ImageUtil.image[ImageUtil.JUANDUOBIN_800x1140]);
            BitmapFactory.decodeStream(is,null,options);
            Log.i(TAG, "justDecodeBounds: width=" + options.outWidth+",height="+options.outHeight);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void simpleSize(View view) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = false;
        option.inSampleSize = 2;
        try {
            InputStream is = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + ImageUtil.image[ImageUtil.JUANDUOBIN_800x1140]);
            Bitmap bitmap = BitmapFactory.decodeStream(is,null,option);
            Log.i(TAG, "simpleSize: width="+bitmap.getWidth()+",height="+bitmap.getHeight());
            iv.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void simpleSize1(View view) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;

        try {
            InputStream is = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + ImageUtil.image[ImageUtil.JUANDUOBIN_800x1140]);
            BitmapFactory.decodeStream(is,null,option);
            option.inSampleSize = calculateInSimpleSize(option,400,400);

            option.inJustDecodeBounds = false;
            is = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + ImageUtil.image[ImageUtil.JUANDUOBIN_800x1140]);
            Bitmap bitmap = BitmapFactory.decodeStream(is,null,option);
            System.out.println("-->"+bitmap.getByteCount());
            Log.i(TAG, "simpleSize="+option.inSampleSize+",width="+bitmap.getWidth()+",height="+bitmap.getHeight());
            iv.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * simpleSize 必须是2的倍数
     * simpleSize 有一个局限，就是只能对图片进行等比例的放大或者缩小，不能按任意大小进行缩放，不过这还是适用大部分的情况
     * 这种方式只需要把图片一次读取到内存就可以了.效率高
     */
    public int calculateInSimpleSize(BitmapFactory.Options option,int reqWidth,int reqHeight){
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


    public void scaleMetohd(View view) {
        //drawable-mdpi drawable-hdpi drawable-xhdpi等目录，每一个都会对应一个density。获取每一个目录的density
        TypedValue value = new TypedValue();
        getResources().openRawResource(+R.drawable.panpan_468x640, value);
        System.out.println("drawable density=" + value.density);//0 -->TypedValue.DENSITY_DEFAULT

        getResources().openRawResource(+R.drawable.white_658x877, value);
        System.out.println("drawable-mdpi density=" + value.density);//160

        getResources().openRawResource(+R.drawable.juanduobin_960x1440, value);
        System.out.println("drawable-hdpi density=" + value.density);//240

        getResources().openRawResource(+R.drawable.juanduobin_1000x1499, value);
        System.out.println("drawable-xhdpi density=" + value.density);//320

        getResources().openRawResource(+R.drawable.meinv_2400x600, value);
        System.out.println("drawable-xxhdpi density=" + value.density);//480

        /************************************************************************/

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.juanduobin_1000x1499);

        //运行的设备dpi=160,图片所在的xhdpi drawable dpi是320,所以图片读取到内存时要缩小一半.
        // xhdpi drawable : src width=1000 height=1499 , scale width=500 height=750
        Log.i(TAG, "xhdpi drawable : src width=1000 height=1499 , scale width="+bitmap.getWidth()+" height="+bitmap.getHeight());
        iv.setImageBitmap(bitmap);
    }

    public void scaleMetohd1(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        BitmapFactory.decodeResource(getResources(),R.drawable.lienhui_580x870,options);
        Log.i(TAG, "inTargetDensity = "+options.inTargetDensity);//160 表示的是目标Bitmap即将被画到屏幕上的像素密度
        Log.i(TAG, "inDensity = "+options.inDensity);//160 表示的是bitmap所使用的像素密度.如果这个值和options.inTargetDensity不一致，则会对图像进行缩放
        Log.i(TAG, "inScreenDensity = "+options.inScreenDensity);//0
        Log.i(TAG, "inScaled = "+options.inScaled);//true

        Log.i(TAG, "-----------------------------------------------------------------------");
        options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(getResources(),R.drawable.juanduobin_960x1440,options);
        Log.i(TAG, "inTargetDensity = "+options.inTargetDensity);//160
        Log.i(TAG, "inDensity = "+options.inDensity);//240
        Log.i(TAG, "inScreenDensity = "+options.inScreenDensity);//0
        Log.i(TAG, "inScaled = "+options.inScaled);//true
    }

    /**
     * inScaled属性
     * 如果inScaled设置为false，则不进行缩放，解码后图片大小为720x720; 否则请往下看。
     * 如果inScaled设置为true或者不设置，则根据inDensity和inTargetDensity计算缩放系数。
     *
     * 从内存和性能的角度考虑，我们可以考虑混合使用这些方法来得到一个最好的结果.设置inSampleSize，inScaled, inDensity, inTargetDensity 属性）。
     * 首先设置inSampleSize比希望得到的图像尺寸的2的某次方大（如：希望得到一个原图1/4大小的图像，则设置inSampleSize的值为2，这些就会先得到原图1/2大小的图像）。
     * 然后通过设置inDensity, inTargetDensity属性来精确需要得到图像的尺寸，并使用过滤器来处理图像（让图像变得更好看）。
     */
    public void scaleMetohd2(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inScaled = true;
//        options.inDensity = 160;//如果不设置,则会根据图片所在目录的density来确定.
        options.inTargetDensity = 320;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.panpan_468x640,options);
        Log.i(TAG, "width="+bitmap.getWidth()+" height="+bitmap.getHeight());//width=936 height=1280
        System.out.println(bitmap.getByteCount());//4792320
        iv.setImageBitmap(bitmap);
    }

    public void scaleMetohd3(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.juanduobin_1000x1499);
        //这个是准确的缩放！但是每次 createBitmap ，都会分配新的内存，带来资源的 消耗
        Bitmap bitmap = Bitmap.createScaledBitmap(src,400,800,false);
        iv.setImageBitmap(bitmap);
    }

    public void scaleMetohd4(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.juanduobin_800x1140);
        Matrix matrix = new Matrix();
        //设置Matrix以(px,py)为轴心进行缩放，sx、sy为X、Y方向上的缩放比例
        matrix.postScale(1.0f,0.5f);

        Bitmap bitmap = Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,true);
        iv.setImageBitmap(bitmap);
    }

    public void compress(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cuixuanjing_936x1404);
        try {
            FileOutputStream fos = new FileOutputStream(SDCARD+"/"+"abcd.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copy(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.lienhui_580x870);
        //isMutable 如果是true，那么产生的图片是可以改变的(比如，他的像素可以被修改)
        Bitmap bitmap = src.copy(Bitmap.Config.ARGB_8888,false);
        if(bitmap == src){
            Log.i(TAG, "copy: bitmap == src");
        }else{
            src.recycle();
        }

        iv.setImageBitmap(bitmap);
    }


    public void matrix(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.juanduobin_800x1140);

        Matrix matrix = new Matrix();
        matrix.postRotate(30);
        matrix.postScale(0.5f,0.5f);

        /**
         * surce：用来剪裁的图片源;
         * x：剪裁x方向的起始位置;
         * y：剪裁y方向的起始位置;
         * width：剪裁的宽度;
         * height：剪裁的高度;
         * filter参数为true可以进行滤波处理，有助于改善新图像质量
         *
         * x+width<=bitmap.width
         * 先按照 坐标及width height裁剪出一个图片,然后再matrix变换!!!
         */
        Bitmap bitmap = Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,false);
//        Bitmap bitmap = Bitmap.createBitmap(src,0,0,400,400,matrix,false);
        if(bitmap == src){
            System.out.println("bitmap == src");
        }
        Log.i(TAG, "matrix: width="+bitmap.getWidth()+" height="+bitmap.getHeight());//width=273 height=273
        iv.setImageBitmap(bitmap);
    }

    public void density(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lienhui_580x870);
        //指定位图的密度,如果不进行设置，默认是设备的Density,如果使用默认的density，那么当把一个bitmap显示到一个ImageView上的时候就不会对其进行缩放
//        bitmap.setDensity(120);
        bitmap.setDensity(320);
        iv.setImageBitmap(bitmap);
        //原图不改变大小,但是可以缩放
        Log.i(TAG, "drawBitmap: width="+bitmap.getWidth()+" height="+bitmap.getHeight());//580 870
    }

    public void drawBitmap(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.twins_580x821);
        Bitmap dst = Bitmap.createBitmap(src.getWidth(),src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);

        //第一个rect代表要绘制的的bitmap区域，第二个代表是要将bitmap绘制在屏幕的什么地方.铺满！！
        Rect regionSrc = new Rect(0, 0, 580, 821);
        Rect regionDst = new Rect(50, 50, src.getWidth(), src.getHeight());

        //铺满dst区域
//        Rect regionSrc = new Rect(0, 0, 580/2, 821/2);
//        Rect regionDst = new Rect(50, 50, src.getWidth(), src.getHeight());

        canvas.drawBitmap(src,regionSrc,regionDst,new Paint(Paint.ANTI_ALIAS_FLAG));
        iv.setImageBitmap(dst);
    }


    public void drawBitmap1(View view) {
        Bitmap src = BitmapFactory.decodeResource(getResources(),R.drawable.twins_580x821);
        Bitmap dst = Bitmap.createBitmap(src.getWidth(),src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        canvas.drawColor(Color.parseColor("#cccccc"));

        Matrix matrix = new Matrix();
        matrix.setRotate(15);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);

        //不加旋转会有锯齿出现
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(src,matrix,new Paint(Paint.ANTI_ALIAS_FLAG));
        iv.setImageBitmap(dst);
    }

    public Drawable bitmapToDrawable(Bitmap bitmap){
        BitmapDrawable drawable = new BitmapDrawable(getResources(),bitmap);
        return drawable;
    }

    public Bitmap drawableToBitmap(Drawable drawable){
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式,判断是否有alpha
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565;

        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        Canvas canvas = new Canvas(bitmap);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);

        return bitmap;
    }

    //截图
    public void captureScreen(View view){
        View contentView = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(contentView.getWidth(),contentView.getHeight(), Bitmap.Config.ARGB_8888);
        contentView.draw(new Canvas(bitmap));


        //ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            FileOutputStream fos = new FileOutputStream(SDCARD+"/capture.png");
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
