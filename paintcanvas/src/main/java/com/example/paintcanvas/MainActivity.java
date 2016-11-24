package com.example.paintcanvas;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.paintcanvas.view.path.PathActivity;
import com.example.paintcanvas.view.text.TextActivity;
import com.example.paintcanvas.view.xfermode.XfermodeActivity;

public class MainActivity extends AppCompatActivity {

    private Bitmap dst ;//画图结果
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void line(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿画笔
        paint.setAntiAlias(true);//也可以稍后设置抗锯齿
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);

        Canvas canvas = getCanvasWithDst(600,600);
        canvas.drawLine(0,0,300,300,paint);

        paint.setColor(Color.BLUE);
        float[] pts = {
                10, 50, 30, 170,//pts的长度必须为4的倍数，四个四个归成一组,代表一条直线
                10, 50, 160, 250,
                160, 250, 370, 380,
                10, 50, 90, 350
        };
        //过滤四个点,画接下来8个数据的直线(过滤的数量必须是四的整数倍 因为每四个数据代表一条直线)
        canvas.drawLines(pts,4,12,paint);

        iv.setImageBitmap(dst);
    }

    Canvas getCanvasWithDst(int width,int height){
        dst = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        return canvas;
    }

    public void rect(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(100);//设置alpha不透明度，范围为0~255

        Canvas canvas = getCanvasWithDst(600,600);
        canvas.drawRect(new Rect(25,25,225,225),paint);

        //Rect是使用int类型作为数值，RectF是使用float类型作为数值.精度不一样
        canvas.drawRect(new RectF(25.0f,275.0f,225.0f,475.0f),paint);

        //注意,如果重新设置颜色,那么前面设置的alpha会失效.为什么?
        //因为设置Color时,Color是包含Alapha R G B的.所以又把alpha重新设置了.
        paint.setColor(Color.BLUE);
        canvas.drawRect(new RectF(275,25,475,225),paint);
        iv.setImageBitmap(dst);
    }

    public void circle(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        Canvas canvas = getCanvasWithDst(600,600);
        canvas.drawCircle(300,300,200,paint);
        iv.setImageBitmap(dst);
    }

    public void roundRect(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        Canvas canvas = getCanvasWithDst(600,600);

        //rx：x方向上的圆角半径。
        //ry：y方向上的圆角半径。
        canvas.drawRoundRect(new RectF(20,20,220,220),20,20,paint);
        canvas.drawRoundRect(new RectF(20,270,220,470),60,60,paint);
        canvas.drawRoundRect(new RectF(270,20,470,220),100,100,paint);

        iv.setImageBitmap(dst);
    }

    public void point(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(8);
        Canvas canvas = getCanvasWithDst(600,600);

        //设置笔触风格。用笔在纸上戳一点，这个点的形状就是笔触。可以是圆形 矩形等
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(20,20,paint);

        float[] pts = {
                10, 50, 30, 170,//每两组代表一个点
                10, 50, 160, 250,
                370, 380,
                20, 150, 100, 300
        };
        paint.setColor(Color.BLUE);
        canvas.drawPoints(pts,2,6,paint);

        iv.setImageBitmap(dst);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void oval(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        Canvas canvas = getCanvasWithDst(600,600);
        RectF rectF = new RectF(100,100,500,400);

        canvas.drawRect(rectF,paint);
        paint.setColor(Color.BLUE);
        canvas.drawOval(rectF,paint);
        iv.setImageBitmap(dst);
    }

    public void arc(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        Canvas canvas = getCanvasWithDst(600,600);
        RectF rectF = new RectF(100,100,500,400);

        //oval：圆弧所在的椭圆对象。startAngle：圆弧的起始角度。sweepAngle：圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度.useCenter：是否显示半径连线
        canvas.drawArc(rectF,30,240,false,paint);

        //画轮廓
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        canvas.drawRect(rectF,paint);

        iv.setImageBitmap(dst);
    }

    public void text(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(20);
        paint.setStrokeWidth(3);
        Canvas canvas = getCanvasWithDst(600,600);

        //绘基准点
        canvas.drawPoint(300, 200, paint);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(1);
        //设置文本对齐方式.center表示字符在水平方向上以x中心向左右两边延伸，在垂直方向以y为起点向下延伸；left表示字符在水平方向上以x为起点向右延伸，在垂直方向上以y为起点向下延伸；right表示字符在水平方向上以x为起点向左延伸，在垂直方向上以y为起点向下延伸。
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Hello java", 300, 200, paint);
        paint.setTextAlign(Paint.Align.LEFT);


        paint.setUnderlineText(true);
        canvas.drawText("下划线文字",100,100,paint);


        paint.setUnderlineText(false);
        paint.setStrikeThruText(true);
        canvas.drawText("删除线文字",100,150,paint);
        paint.setStrikeThruText(false);


        //设置文本倾斜在水平方向的倾斜。这个值没有具体范围。推荐-0.25.负为右倾。默认0
        paint.setTextSkewX(-0.25f);
        canvas.drawText("倾斜文字",100,200,paint);


        //设置字体样式
        //Typeface create(String familyName,int style)直接通过指定字体名来加载系统中自带的文字样式
        //Typeface createFromAsset(AssetManager mgr,String path)通过从Asset中获取外部字体样式
        //Typeface createFromFile(String pat) 从路径创建
        //Typeface defaultFromStyle(int style) 创建默认字体
        //上述参数style 如下 NORMAL 正常体  BOLD粗体 ITALIC斜体 BOLD_ITALIC粗斜体
        Typeface typeface = Typeface.create("monospace",Typeface.NORMAL);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Hello java,光临我的博客", 200, 400, paint);


        iv.setImageBitmap(dst);
    }

    public void path(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#B23AEE"));
        paint.setStyle(Paint.Style.STROKE);
        Canvas canvas = getCanvasWithDst(600,600);


        Path path = new Path();
        path.moveTo(50,50);
        path.lineTo(150,50);
        path.rLineTo(0,100);//前进的距离40  60.并不是绝对坐标.同理,path.rMoveTo()代表移动多少个距离
        path.rLineTo(-100,0);
        path.close();//回到初始点形成封闭的曲线,否则不会画到原点
        canvas.drawPath(path,paint);


        path.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        path.addArc(new RectF(200,50,350,180),30,210);
        path.addCircle(500,150,70, Path.Direction.CCW);//Path.Direction.CCW逆时针 CW顺时针
        canvas.drawPath(path,paint);


        path.reset();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(50,250);
        path.lineTo(120,330);
        RectF rectF = new RectF(170,300,300,400);
        path.arcTo(rectF,30,160,false);
        canvas.drawPath(path,paint);
        paint.setColor(Color.RED);
        canvas.drawRect(rectF,paint);

        /**
         * 不带r的方法是基于原点的坐标系(偏移量)，rXxx方法是基于当前点坐标系(偏移量)
         * path.addArc 及 path.arcTo 区别
         * arrArc:添加一个椭圆到path中，和上一次的操作点无关
         * arcTo:添加一个椭圆到Path中，如果圆弧的起始点和上次操作点坐标不同(且forceMoveTo = false)，就直线连接两个点
         */

        //二阶贝塞尔曲线 起始点为path的起始点:(80,430) 控制点(160, 530)  结束点为(250,490)
        path.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.DKGRAY);
        path.moveTo(80,430);
        path.lineTo(160,530);
        path.lineTo(250,490);
        path.lineTo(260,420);
        canvas.drawPath(path,paint);

        path.reset();
        path.moveTo(80,430);
        path.quadTo(160,530,250,490);

        /**
         * 这里的dx、dy表示的是相对起始点的dx、dy的坐标差值
         * (dx2,dy2)不是相对于(dx1,dy1),也是相对于起点的
         * 控制点移动(80,100) 终止点移动(170,100)
         * */
        path.rQuadTo(80,100,170,-100);
        paint.setColor(Color.GREEN);
        canvas.drawPath(path,paint);

        //三阶贝塞尔曲线起始点(200,60)  结束点(560, 230).两个控制点(350, 100),(430, 270）
        path.reset();
        path.moveTo(80,430);
        path.cubicTo(160,530,250,490,260,420);
        paint.setColor(Color.YELLOW);
        canvas.drawPath(path,paint);


        iv.setImageBitmap(dst);
    }

    /************************ Text 详解 **************************/

    public void textPath(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#B23AEE"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(20);
        Canvas canvas = getCanvasWithDst(600,600);

        Path path = new Path();
        path.addArc(new RectF(100,100,300,250),30,150);
        canvas.drawPath(path,paint);
        canvas.drawTextOnPath("hello android.",path,50,20,paint);//50 距离起始点的距离,20 垂直方向的偏移

        path.reset();
        path.addCircle(350,350,200, Path.Direction.CCW);//逆时针
        canvas.drawPath(path,paint);
        canvas.drawTextOnPath("hello android.",path,50,20,paint);//50 距离起始点的距离,20 垂直方向的偏移

        iv.setImageBitmap(dst);
    }

    public void textLine(View view) {
        Intent intent = new Intent(this,TextActivity.class);
        startActivity(intent);
    }


    /************************ Path 详解 **************************/
    public void pathMeasure(View view) {

        //PathMeasure主要用来测量path，通过它，我们可以得到路径上特定的点的坐标等等

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        //非闭合矩形 边长250
        Path path = new Path();
        path.moveTo(50,50);
        path.lineTo(300,50);
        path.lineTo(300,300);
        path.lineTo(50,300);

        //forceClose : 是否需要强制闭合.不会改变原来Path的状态
        PathMeasure measure = new PathMeasure(path,true);
        System.out.println("PathMeasure len="+measure.getLength());//1000

        Canvas canvas = getCanvasWithDst(600,600);
        canvas.drawPath(path,paint);

        Path dstPath = new Path();
        dstPath.lineTo(500,30);
        //startD 开始截取位置距离Path七点的长度.stopD 结束截取位置巨鹿Path七点的长度.dst截取的Path会添加到dst中.
        //startWithMoveTo:如果 startWithMoveTo 为 true, 则被截取出来到Path片段保持原状，如果 startWithMoveTo 为 false，则会将截取出来的 Path 片段的起始点移动到 dst 的最后一个点，以保证 dst 的连续性。
        if(measure.getSegment(20,450,dstPath,true)){//是否截取成功
            paint.setColor(Color.BLUE);
            canvas.drawPath(dstPath,paint);
        }
        iv.setImageBitmap(dst);
    }

    public void pathMeasure1(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);

        Path path = new Path();
        path.addRect(new RectF(100,100,400,400), Path.Direction.CCW);
        path.addRect(new RectF(150,150,350,350), Path.Direction.CCW);
        Canvas canvas = getCanvasWithDst(600,600);
        canvas.drawPath(path,paint);

        PathMeasure measure = new PathMeasure(path,false);
        System.out.println("第一条 len="+measure.getLength());//1200,也就是最外围的矩形

        /**
         * 我们知道 Path 可以由多条曲线构成，但不论是 getLength , getgetSegment 或者是其它方法，都只会在其中第一条线段上运行
         * 而这个 nextContour 就是用于跳转到下一条曲线到方法，如果跳转成功，则返回 true
         */
        measure.nextContour();//跳转到下一个路径
        System.out.println("第二条 len="+measure.getLength());//800,也就是最里面的矩形

        iv.setImageBitmap(dst);

        /*
        1.曲线的顺序与 Path 中添加的顺序有关。
        2.getLength 获取到到是当前一条曲线分长度，而不是整个 Path 的长度。
        3.getLength 等方法是针对当前的曲线
        */

        /**
         * getPosTan 这个方法是用于得到路径上某一长度的位置以及该位置的正切值：
         * pos	该点的坐标值 pos[0] 是该点的x坐标 pos[1]是改点的y坐标
         * tan	该点的正切值
         */

        float[] pos = new float[2];
        float[] tan = new float[2];
        //注意该measure的是第二条线段!并且该线段是CCW 逆时针的!
        measure.getPosTan(250,pos,tan);
    }

    public void arrow(View view) {
        Intent intent = new Intent(this,PathActivity.class);
        intent.putExtra("tag",0);
        startActivity(intent);
    }

    public void heart(View view) {
        Intent intent = new Intent(this,PathActivity.class);
        intent.putExtra("tag",1);
        startActivity(intent);
    }

    public void hand(View view) {
        Intent intent = new Intent(this,PathActivity.class);
        intent.putExtra("tag",2);
        startActivity(intent);
    }

    public void wave(View view) {
        Intent intent = new Intent(this,PathActivity.class);
        intent.putExtra("tag",3);
        startActivity(intent);
    }

    public void search(View view){
        Intent intent = new Intent(this,PathActivity.class);
        intent.putExtra("tag",4);
        startActivity(intent);
    }

    public void circlePath(View view){
        Intent intent = new Intent(this,PathActivity.class);
        intent.putExtra("tag",5);
        startActivity(intent);
    }


    /************************ canvas 详解 **************************/
    public void translate(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        Rect rect = new Rect(0, 0, 200, 200);

        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(100, 100);
        canvas.drawRect(rect, paint);

        paint.setColor(Color.BLUE);
        rect.set(0, 0, 100, 100);
        canvas.drawRect(rect, paint);

        iv.setImageBitmap(bitmap);
    }

    public void rotate(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        Rect rect = new Rect(250, 250, 500, 400);

        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(rect, paint);

        //第一个构造函数直接输入旋转的度数，正数是顺时针旋转，负数指逆时针旋转，它的旋转中心点是原点（0，0）
        //第二个构造函数除了度数以外，还可以指定旋转的中心点坐标（px,py）
        canvas.rotate(30);
        paint.setColor(Color.BLUE);
        canvas.drawRect(rect, paint);
        canvas.drawLine(0,50,200,50,paint);


        iv.setImageBitmap(bitmap);
    }

    public void scale(View view) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        Rect rect = new Rect(100, 100, 500, 500);

        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(rect, paint);


        //float sx:水平方向伸缩的比例，假设原坐标轴的比例为n,不变时为1，在变更的X轴密度为n*sx;所以，sx为小数为缩小，sx为整数为放大
        //float sy:垂直方向伸缩的比例，同样，小数为缩小，整数为放大
        canvas.scale(0.5f, 1);
        paint.setColor(Color.BLUE);
        canvas.drawRect(rect, paint);


        iv.setImageBitmap(bitmap);
    }

    public void clip(View view) {
        //裁剪画布
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.GREEN);

        //裁剪画布,做个标记.最终合成时,只取裁剪出来的区域,其他的地方丢掉
        canvas.clipRect(100, 100, 350, 350);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.panpan),0,0,paint);
        paint.setColor(Color.RED);

        //这个坐标仍然是以原来canvas的左上角为原点
        canvas.drawCircle(0, 0, 210, paint);

        iv.setImageBitmap(bitmap);
    }


    /************************ Paint canvas 详解 **************************/

    public void canvasSave(View view){
        startActivity(new Intent(this,CanvasSaveActivity.class));
    }


    private int dx = 10, dy = 10, radius = 10;
    public void shadowLayer(View view) {
        int id = view.getId();
        if(id == R.id.btn_shadowLayer){
            dx = 10;
            dy = 10;
            radius = 10;
        }else if(id == R.id.btn_dx_plus)
            dx += 10;
        else if(id == R.id.btn_dy_plus)
            dy += 10;
        else if(id == R.id.btn_radius_plus)
            radius += 5;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        //radius为阴影的角度,dx dy为阴影在x轴和y轴上的距离
        paint.setShadowLayer(radius, dx, dy, Color.BLACK);
        //清楚阴影层
        //paint.clearShadowLayer();

        Canvas canvas = getCanvasWithDst(600,600);
        canvas.drawText("你对美好生活的向往,就是我奋斗的目标",30,70,paint);
        canvas.drawRect(100, 100, 300, 300, paint);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.panpan);
        canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),new Rect(120,320,320,560),paint);
        iv.setImageBitmap(dst);
    }
    public void avoidXfermode(View view) {
        Intent intent = new Intent(this,XfermodeActivity.class);
        intent.putExtra("tag",0);
        startActivity(intent);
    }

    public void porterDuffXfermode(View view) {
        Intent intent = new Intent(this,XfermodeActivity.class);
        intent.putExtra("tag",1);
        startActivity(intent);
    }

    public void imageMask(View view) {
        Intent intent = new Intent(this,XfermodeActivity.class);
        intent.putExtra("tag",2);
        startActivity(intent);
    }

    public void light(View view) {
        Intent intent = new Intent(this,XfermodeActivity.class);
        intent.putExtra("tag",3);
        startActivity(intent);
    }

    public void eraser(View view) {
        Intent intent = new Intent(this,XfermodeActivity.class);

        if(view.getId() == R.id.eraser_1)
            intent.putExtra("tag",4);
        else
            intent.putExtra("tag",5);

        startActivity(intent);
    }

    public void textWave(View view) {
        Intent intent = new Intent(this,XfermodeActivity.class);
        intent.putExtra("tag",6);
        startActivity(intent);
    }

    public void circleWave(View view) {
        Intent intent = new Intent(this,XfermodeActivity.class);
        intent.putExtra("tag",7);
        startActivity(intent);
    }



}
