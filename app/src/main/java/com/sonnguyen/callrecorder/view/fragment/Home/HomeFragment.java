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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;

import java.io.File;
import java.io.IOException;

public class HomeFragment extends BaseFragment<HomeViewModel> {
    private Button btRecord, btStop, btPlay;
    private MediaRecorder myAudioRecorder;
    private boolean isRecording;
    private String recordFile,recordPath;
    private TextView filenameText;
    private MediaPlayer mediaPlayer = new MediaPlayer();
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
        try {
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(getActivity(), "Playing Audio", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
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
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;

        btPlay.setEnabled(true);
        btStop.setEnabled(false);
        btPlay.setEnabled(true);
        Toast.makeText(getActivity(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
    }

    private void startRecording() {
        if (checkPermissions()){
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setOutputFile(getRecordingFilePath());
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            try {
                myAudioRecorder.prepare();
                myAudioRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btRecord.setEnabled(false);
            btStop.setEnabled(true);
            Toast.makeText(getContext(),"Record started",Toast.LENGTH_LONG).show();
        }
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File musicDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDir,"test recording"+".mp3");
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
