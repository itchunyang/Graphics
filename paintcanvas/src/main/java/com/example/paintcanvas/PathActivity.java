package com.example.paintcanvas;

import android.app.Activity;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;

import com.example.paintcanvas.view.path.ArrowPathView;
import com.example.paintcanvas.view.path.HandPathView;
import com.example.paintcanvas.view.path.HeartPathView;
import com.example.paintcanvas.view.path.WavePathView;

/**
 * Created by luchunyang on 16/8/17.
 */
public class PathActivity extends Activity {
    private View[] views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        views = new View[]{new ArrowPathView(this),new HeartPathView(this),new HandPathView(this),new WavePathView(this)};

        int tag = getIntent().getIntExtra("tag",0);
        setContentView(views[tag]);
    }
}
