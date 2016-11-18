package com.itchunyang.graphics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * xml对应的是shape标签
 * [shape]
 *      rectangle矩形
 *      oval椭圆形
 *      line线形
 *      ring环形
 *
 *        ring(环形) innerRadius、thickness、innerRadiusRatio、thicknessRatio
 *                      API < 19上需要设置useLevel为false
 *                      android:innerRadius         尺寸，内环的半径。
 *                      android:thickness           尺寸，环的厚度
 *                      android:innerRadiusRatio    浮点型，以环的宽度比率来表示内环的半径，
 *                      例如，如果android:innerRadiusRatio，表示内环半径等于环的宽度除以5，这个值是可以被覆盖的，默认为9.
 *                      android:thicknessRatio      浮点型，以环的宽度比率来表示环的厚度，例如，如果android:thicknessRatio="2"，
 *                      那么环的厚度就等于环的宽度除以2。这个值是可以被android:thickness覆盖的，默认值是3.
 *                      android:useLevel            boolean值，如果当做是LevelListDrawable使用时值为true，否则为false.
 *
 * [solid]
 *      填充色
 *
 * [corners] 用于设置四个拐角的半径
 *
 * [stroke]描边 dashWidth定义每个虚线段的长度,dashGap定义了两个虚线段之间的距离
 *
 * [padding]用于设置drawable的padding
 * [gradient]
 *          type:
 *              Linear(线性渐变) 支持属性 startColor centerColor endColor angle(默认=0,渐变是从左向右进行的,angle可以改变角度,必须是45的倍数)
 *              radial(辐射渐变) angle
 *                              android:centerX="float" 渐变中心x的相对位置,范围0~1,0.5代表父控件的x的中间
 *                              android:centerY="float" 渐变中心x的相对位置,范围0~1,0.5代表父控件的Y的中间
 *                              android:gradientRadius 圆形半径
 *              sweep(梯度渐变)  表示颜色围绕着中心点360度顺时针旋转的,起点是3点位置
 *                              android:centerX="float" 渐变中心x的相对位置,范围0~1,0.5代表父控件的x的中间
 *                              android:centerY="float" 渐变中心x的相对位置,范围0~1,0.5代表父控件的Y的中间
 *
 */
public class GradientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);
    }
}
