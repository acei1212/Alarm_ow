package com.example.jack.alarm_ow;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayAlarmAty extends Activity {
    Button stop;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_alarm_aty);

        mp = MediaPlayer.create(this, R.raw.abc);
        mp.setLooping(true); //重覆播放
        mp.start();


    }

    public void clickstop(View v) {
        stop = (Button)findViewById(R.id.button);
        mp.stop();
        finish();
        mp.release();
    }


}
