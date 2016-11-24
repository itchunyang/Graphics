package com.example.paintcanvas.view.path;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by luchunyang on 16/8/17.
 */
public class PathActivity extends Activity {
    private View[] views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        views = new View[]{new ArrowPathView(this),new HeartPathView(this),new HandPathView(this),new WavePathView(this),new SearchView(this),new CirclePathView(this)};

        int tag = getIntent().getIntExtra("tag",0);
        setContentView(views[tag]);
    }
}
