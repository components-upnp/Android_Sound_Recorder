package com.example.mkostiuk.android_sound_recorder.main;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mkostiuk.android_sound_recorder.R;
import com.example.mkostiuk.android_sound_recorder.upnp.ServiceUpnp;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.io.File;

public class App extends AppCompatActivity {

    private ServiceUpnp service;
    private Button buttonRecord, buttonStopRecord;
    private MediaRecorder recorder;

    public void init() {
        buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonStopRecord = (Button) findViewById(R.id.buttonStopRecord);
        activate(buttonRecord);
        deactivate(buttonStopRecord);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        init();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        File dir;
        System.err.println(Build.BRAND);
        if (Build.BRAND.toString().equals("htc_europe"))
            dir = new File("/mnt/emmc/SoundRecorder/");
        else
            dir = new File(Environment.getExternalStorageDirectory().getPath() + "/SoundRecorder/");

        while (!dir.exists()) {
            dir.mkdir();
            dir.setReadable(true);
            dir.setExecutable(true);
            dir.setWritable(true);
        }



        service = new ServiceUpnp();




        getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
                service.getService(),
                Context.BIND_AUTO_CREATE);


    }

    public void record(View view) {
        activate(buttonStopRecord);
        deactivate(buttonRecord);
    }

    public void stopRecord(View view) {
        activate(buttonRecord);
        deactivate(buttonStopRecord);
    }

    public void activate(Button ... buttons) {
        for (Button b : buttons)
            b.setClickable(true);
    }

    public void deactivate(Button ... buttons) {
        for (Button b : buttons)
            b.setClickable(false);
    }
}
