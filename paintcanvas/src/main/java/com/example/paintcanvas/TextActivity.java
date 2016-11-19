package com.example.paintcanvas;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.paintcanvas.view.text.TextLineView01;
import com.example.paintcanvas.view.text.TextLineView02;
import com.example.paintcanvas.view.text.TextLineView03;
import com.example.paintcanvas.view.text.TextLineView04;

/**
 * Created by luchunyang on 16/8/17.
 */
public class TextActivity extends Activity {
    private View[] views ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = new View[]{new TextLineView01(this),new TextLineView02(this),new TextLineView03(this),new TextLineView04(this)};

        int tag = getIntent().getIntExtra("tag",0);
        setContentView(views[tag]);
    }
}
