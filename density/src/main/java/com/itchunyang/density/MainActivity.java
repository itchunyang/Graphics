package com.itchunyang.density;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

/**
 * http://www.open-open.com/lib/view/open1462929484471.html
 * 1.实际密度与系统密度
 *      实际密度就是我们自己算出来的密度,这个密度代表了屏幕真实的细腻程度。例如小米pad的真是密度是324.说明屏幕每英寸324个像素
 * android为不同设备设定了dpi 120、160、240、320、480,一般的厂商会根据自己的真实的dpi找到最接近的android推荐的密度,作为自己的系统密度
 * 例如440dpi和他最近的就是480dpi。但是现在的很多厂商不一定会选择这些值作为系统dpi,例如小米note这样的xxhdpi的设备,他的dpi并不是480,而是它的实际dpi440
 *
 * 2.系统首先匹配的是最适合自己屏幕密度的图片，如果没有，会把高分辨率目录里的图片拿来显示，如果还没有指定图片，才会寻找低分辨率对应目录的图片进行显示
 *
 * 3.分辨率 = 像素 ÷ 尺寸
 * 分辨率就是密度，就是一个单位面积内能储存的像素多少。你把图片放大，图片只是容积变大，但是密度还是不变。但是原来图片放大后，原有颜色信息量（像素）不变，容积变大，像素又不会凭空捏造，所以放大后白色像素就会补充进去，这样图片就变模糊了
 *
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getDensity(View view) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        /**
         * 小米PAD 实际分辨率 1536x2048  7.9英寸
         *
         * 计算出来的精确dpi = 2560 / 7.9 = 324
         *
         * densityDpi=320(屏幕密度DPI)
         * density=2.0(屏幕密度)
         * scaledDensity=2.0(字体的放大系数)
         * xdpi=325.12 //宽度方向上的dpi
         * ydpi=325.12
         */
        Log.i(TAG, "density=" + metrics.density + " densityDpi=" + metrics.densityDpi + " scaledDensity=" + metrics.scaledDensity +" xdpi=" + metrics.xdpi+" ydpi="+ metrics.ydpi);
        Log.i(TAG, "getDensity: "+metrics.widthPixels+" " + metrics.heightPixels);
    }

    public void drawableDensity(View view) {
        //drawable-mdpi drawable-hdpi drawable-xhdpi等目录，每一个都会对应一个density。获取每一个目录的density
        //这个类是工具类,作为一个动态容器，它存放一些数据值，这些值主要是resource中的值。
        //我们来理解一下:resource中到底有哪些值？layout、drawable、string、style、anim、dimens、menu、colors、ids这些值一些和屏幕适配有直接的关系。
        TypedValue typeValue = new TypedValue();

        getResources().openRawResource(+R.drawable.img_567x850,typeValue);
        Log.i(TAG, "drawable density=" + typeValue.density);//0 -->TypedValue.DENSITY_DEFAULT

        getResources().openRawResource(+R.drawable.img_555x948,typeValue);
        Log.i(TAG, "drawable-mdpi density=" + typeValue.density);//160

        getResources().openRawResource(+R.drawable.img_640x1094,typeValue);
        Log.i(TAG, "drawable-hdpi density=" + typeValue.density);//240

        getResources().openRawResource(+R.drawable.img_860x1290,typeValue);
        Log.i(TAG, "drawable-xhdpi density=" + typeValue.density);//320

        getResources().openRawResource(+R.drawable.img_1024x1535,typeValue);
        Log.i(TAG, "drawable-xxhdpi density=" + typeValue.density);//480
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
    }


    /******************************************************/
    //同一张图片，放在不同的drawable目录下（从drawable-lpdi到drawable-xxhpdi）在同一手机上占用的内存越来越小。仔细想想

    public void loadMDPI(View view) {
        //width=1110,height=1896
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_555x948);
        popBitmap(bitmap);
    }

    public void loadHDPI(View view){
        //width=853,height=1459
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_640x1094);
        popBitmap(bitmap);
    }

    public void loadXHDPI(View view) {
        //width=860,height=1290
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_860x1290);
        popBitmap(bitmap);
    }

    public void loadXXHDPI(View view) {
        //width=683,height=1023
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_1024x1535);
        popBitmap(bitmap);
    }

    public void loadWhich(View view) {
        //width=860,height=1290 通过分辨率可以看出来,hdpi和xhdpi 同时有目标图片时,会优先加载和自己分辨率接近的
        //因为小米pad的dpi 是320  而xhdpi也是320 所以不需要缩放!
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_860x1290);
        popBitmap(bitmap);
    }




}
