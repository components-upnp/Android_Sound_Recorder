package com.example.mkostiuk.android_sound_recorder.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mkostiuk.android_sound_recorder.R;
import com.example.mkostiuk.android_sound_recorder.upnp.ServiceUpnp;
import com.example.mkostiuk.android_sound_recorder.xml.GenerateurXml;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import xdroid.toaster.Toaster;

public class App extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 1;

    private ServiceUpnp service;
    private Button buttonRecord, buttonStopRecord;
    private MediaRecorder recorder;
    private EditText editText;
    private String pathDir;
    private String pathFile;

    public void init() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        File dir;
        System.err.println(Build.BRAND);
        if (Build.BRAND.toString().equals("htc_europe")) {
            pathDir = "/mnt/emmc/SoundRecorder/";
            dir = new File(pathDir);
        }
        else {
            pathDir = Environment.getExternalStorageDirectory().getPath() + "/SoundRecorder/";
            dir = new File(pathDir);
        }

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

        buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonStopRecord = (Button) findViewById(R.id.buttonStopRecord);
        editText = (EditText) findViewById(R.id.editText);
        recorder = new MediaRecorder();
        activate(buttonRecord);
        deactivate(buttonStopRecord);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        //Vérification que l'autorisation d'accès au système de stockage est accrodée
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Cela signifie que la permission à déjà était
                //demandé et l'utilisateur l'a refusé
                //Vous pouvez aussi expliquer à l'utilisateur pourquoi
                //cette permission est nécessaire et la redemander
                Toaster.toast("Vous avez refusé l'accés au Stockage, fermeture");
                finish();
            } else {
                //Sinon demander la permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }
        else {
            //Permission déjà accrodée
            init();
        }

    }

    public void record(View view) throws IOException {
        activate(buttonStopRecord);

        pathFile = pathDir + editText.getText() + ".mp3";
        recorder.setOutputFile(pathFile);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        recorder.prepare();

        recorder.start();

        deactivate(buttonRecord);
    }

    public void stopRecord(View view) throws TransformerException, ParserConfigurationException {
        activate(buttonRecord);
        recorder.stop();
        recorder.release();
        service.getRecorderLocalService().getManager().getImplementation()
                .sendPath(new GenerateurXml().getDocXml(service.getUdnRecorder().toString(), pathFile));
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est garantie on initialise les services et boutons
                    init();
                } else {
                    Toaster.toast("Permission refusée, fermeture");
                    finish();
                }
                return;
            }
        }
    }
}
