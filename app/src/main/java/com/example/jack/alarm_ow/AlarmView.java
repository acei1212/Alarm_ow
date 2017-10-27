package com.example.jack.alarm_ow;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Jack on 2017/10/22.
 */

public class AlarmView extends LinearLayout {
    private ListView listView;
    private ArrayAdapter<AlarmData> adapter;
    private static final String KEY_ALARM_LIST = "alarmlist";
    private AlarmManager alarmManager;

    public AlarmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AlarmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmView(Context context) {
        super(context);
        init();
    }

    private void init() {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(), android.R.layout.simple_expandable_list_item_1);
        listView.setAdapter(adapter);
        adapter.add(new AlarmData(System.currentTimeMillis()));
        readSaveAlarmList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//長按清單
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("操作選項").setItems(new CharSequence[]{"刪除"}
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        deleteAlarm(position);
                                        break;
                                }


                            }
                        }).setNegativeButton("取消", null).show();
                return true;
            }
        });


    }

    private void deleteAlarm(int position) { //刪除鬧鐘選項
        AlarmData ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList(); //保存刪除後記錄
        alarmManager.cancel(PendingIntent.getBroadcast(getContext(),
                ad.getId(),
                new Intent(getContext(),
                        AlarmReceiver.class), 0));

    }

    public void addAlarm() {   //新增鬧鐘
        Calendar c = Calendar.getInstance();

        new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        Calendar currentTime = Calendar.getInstance();

                        if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                            //設定若 新增時間 > 現在時間為正常，< 現在時間便多加1天)
                        }

                        AlarmData ad = new AlarmData(calendar.getTimeInMillis());
                        adapter.add(ad);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                ad.getTime(),
                                5 * 60 * 1000,
                                PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));

                        saveAlarmList();


                        String tmps = hourOfDay+":"+minute;
                        Toast.makeText(getContext(), "設定鬧鐘時間為" + tmps , Toast.LENGTH_SHORT).show();

                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void saveAlarmList() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            sb.append(adapter.getItem(i).getTime()).append(","); //間格中加","
        }

        if (sb.length() > 1) { //避免清單歸零時，跳出error

            String content = sb.toString().substring(0, sb.length() - 1); //去掉最後一個","
            editor.putString(KEY_ALARM_LIST, content);

            System.out.println(content);

        } else {
            editor.putString(KEY_ALARM_LIST, null);
        }

        editor.commit();
    }

    private void readSaveAlarmList() {
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST, null);

        if (content != null) {
            String[] timeString = content.split(",");
            for (String string : timeString) {
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

        public int getId() {
            return (int) (getTime() / 1000 / 60);
        }


        public String getTimeLabel() {
            return timeLabel;
        }

        private String timeLabel = "";
        private long time = 0;
        private Calendar date;
    }


}
