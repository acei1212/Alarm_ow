package com.example.jack.alarm_ow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TabHost;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent it = new Intent();
        it.setClass(MainActivity.this,Cover.class);//開啟時先放Cover
        startActivity(it);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("鬧鐘清單").setContent(R.id.tab1));
//        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("音效一覽").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("關於我").setContent(R.id.tab2));

        WebView web = (WebView) findViewById(R.id.webview);
        web.getSettings().setBuiltInZoomControls(true);
        web.loadUrl("file:///android_asset/aboutme.html");


    }


}
