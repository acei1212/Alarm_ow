package com.example.jack.alarm_ow;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by student on 2017/10/27.
 */

public class Cover extends AppCompatActivity{ //封面畫面

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cover);
        Runnable rab = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
        new Handler().postDelayed(rab,2000); //2秒後跳主選單
    }

    @Override
    public void onBackPressed() {

    }
}
