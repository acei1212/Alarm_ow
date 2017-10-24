package com.example.jack.alarm_ow;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class PlayAlarmAty extends Activity {


    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_alarm_aty);

        mp = MediaPlayer.create(this, R.raw.abc);
        mp.setLooping(true); //重覆播放
        mp.start();


    }


    @Override
    protected void onStop() {
        super.onStop();
        mp.stop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }

}
