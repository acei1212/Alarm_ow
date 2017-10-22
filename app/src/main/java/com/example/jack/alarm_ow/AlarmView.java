package com.example.jack.alarm_ow;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Jack on 2017/10/22.
 */

public class AlarmView extends LinearLayout {
    private FloatingActionButton fab;
    private ListView listView;
    private ArrayAdapter<AlarmData> adapter;
    private  static final String KEY_ALARM_LIST = "alarmlist";

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

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(), android.R.layout.simple_expandable_list_item_1);
        listView.setAdapter(adapter);
        adapter.add(new AlarmData(System.currentTimeMillis()));
        readSaveAlarmList();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
            }
        });
    }


    private void addAlarm() {   //新增鬧鐘
        Calendar c = Calendar.getInstance();

        new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        Calendar currentTime = Calendar.getInstance();

                        if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                            //設定若 新增時間 > 現在時間為正常，< 現在時間便多加1天)
                        }

                        adapter.add(new AlarmData(calendar.getTimeInMillis()));
                        //取值至adapter
                        saveAlarmList();

                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void saveAlarmList() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(),Context.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            sb.append(adapter.getItem(i).getTime()).append(","); //間格中加","
        }

        String content = sb.toString().substring(0,sb.length()-1); //去掉最後一個","
        editor.putString(KEY_ALARM_LIST,content);

        System.out.println(content);

        editor.commit();
    }

    private void readSaveAlarmList(){
    SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST,null);

        if (content != null){
            String[] timeString = content.split(",");
            for(String string : timeString){
                adapter.add(new AlarmData(Long.parseLong(string)));
            }
        }


    }



    private static class AlarmData {  //鬧鐘格式
        public AlarmData(long time) {
            this.time = time;

            date = Calendar.getInstance();
            date.setTimeInMillis(time);

            timeLabel = String.format("%d月%d日 %d:%d",
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));

        }

        public long getTime() {
            return time;
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }

        public String getTimeLabel() {
            return timeLabel;
        }

        private String timeLabel = "";
        private long time = 0;
        private Calendar date;
    }


}
