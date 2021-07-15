package com.sonnguyen.callrecorder.view.fragment.Home;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;

import java.io.File;
import java.io.IOException;

public class HomeFragment extends BaseFragment {
    private Button btRecord, btStop, btPlay;
    private MediaRecorder mediaRecorder;
    private boolean isRecording;
    private String recordFile,recordPath;
    private TextView filenameText;
    private MediaPlayer mediaPlayer = null;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    @Override
    protected Class getClassModel() {
        return HomeViewModel.class;
    }

    @Override
    protected void initEvents() {
        btRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecording();
            }
        });
    }

    private void playRecording() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getRecordFile());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void startRecording() {
        if (checkPermissions()){

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordFile());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Start Recording
            mediaRecorder.start();
        }
    }

    private String getRecordFile(){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File dir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(dir,"testRecord"+".mp3");
        return file.getPath();
    }

    @Override
    protected void initViews() {
        btRecord = (Button) findViewById(R.id.bt_record);
        btStop = (Button) findViewById(R.id.bt_stop);
        btPlay = (Button) findViewById(R.id.bt_play);
        filenameText = (TextView) findViewById(R.id.tv_filename);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {

    }
}
