package com.example.paintcanvas.view.text;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by luchunyang on 16/8/17.
 *
 */

public class TextActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new MyTextView(this));
    }
}
