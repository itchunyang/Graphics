package com.example.paintcanvas;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.paintcanvas.view.layer.LayerView01;
import com.example.paintcanvas.view.layer.LayerView02;
import com.example.paintcanvas.view.layer.LayerView03;
import com.example.paintcanvas.view.xfermode.AvoidXfermodeView;
import com.example.paintcanvas.view.xfermode.CircleWaveView;
import com.example.paintcanvas.view.xfermode.EraserView01;
import com.example.paintcanvas.view.xfermode.EraserView02;
import com.example.paintcanvas.view.xfermode.ImageMaskView;
import com.example.paintcanvas.view.xfermode.LightBookView;
import com.example.paintcanvas.view.xfermode.PorterDuffXfermodeView;
import com.example.paintcanvas.view.xfermode.TextWaveView;

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
        new EraserView01(this),new EraserView02(this),new TextWaveView(this),new CircleWaveView(this),new LayerView01(this),new LayerView02(this),new LayerView03(this)};

        int tag = getIntent().getIntExtra("tag",0);
        setContentView(views[tag]);
    }
}
