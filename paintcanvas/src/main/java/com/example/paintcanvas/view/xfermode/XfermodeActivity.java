package com.example.paintcanvas.view.xfermode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by luchunyang on 16/8/18.
 * 图像混合 Xfermode子类:
 * AvoidXfermode, PixelXorXfermode和PorterDuffXfermode
 */
public class XfermodeActivity extends Activity {

    private View[] views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        views = new View[]{new AvoidXfermodeView(this),new PorterDuffXfermodeView(this),new ImageMaskView(this),new LightBookView(this),
        new EraserView01(this),new EraserView02(this),new TextWaveView(this),new CircleWaveView(this)};

        int tag = getIntent().getIntExtra("tag",0);
        setContentView(views[tag]);
    }
}
