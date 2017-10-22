package com.example.jack.alarm_ow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Date;

/**
 * Created by Jack on 2017/10/22.
 */

public class AlarmView extends LinearLayout {
    private Button fab;
    private ListView listView;
    private ArrayAdapter<AlarmData>adapter;


    public AlarmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AlarmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlarmView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(),android.R.layout.simple_expandable_list_item_1);
        listView.setAdapter(adapter);
        adapter.add(new AlarmData(System.currentTimeMillis()));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
            }
        });
    }
    private void addAlarm(){

    }
    private static class AlarmData{
        public AlarmData(long time){
            this.time = time;

            date = new Date(time);
            timeLabel = date.getHours()+":"+date.getMinutes();
        }

        public long getTime() {
            return time;
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }

        public String getTimeLabel(){
            return timeLabel;
        }

        private String timeLabel="";
        private long time = 0;
        private Date date;
    }


}
