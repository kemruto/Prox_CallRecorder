package com.sonnguyen.callrecorder;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.sonnguyen.callrecorder.view.activity.MainActivity;

import java.io.File;
import java.io.IOException;

import static com.sonnguyen.callrecorder.App.CHANNEL_ID;

public class AutoRecordService extends Service {
    private MediaRecorder myAudioRecorder;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String state = intent.getStringExtra("callState");
        Log.i("aaa","state = "+ state );

        if (state=="OFFHOOK"){
            startRecording();
            Log.i("aaa","recording start");

        }else if(state=="IDLE"){
            stopRecording();
            stopSelf();
            Log.i("aaa","recording end");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("aaa","onDestroy");
        super.onDestroy();
    }

    private void startRecording() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Recording")
                .setContentText("Recording")
                .setSmallIcon(R.drawable.ic_notification)
                .build();

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
        startForeground(1, notification);
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDir,"test recording"+".mp3");
        return file.getPath();
    }

    private void stopRecording() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
    }

}

