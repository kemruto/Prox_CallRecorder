package com.sonnguyen.callrecorder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.activity.MainActivity;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.sonnguyen.callrecorder.utils.app.App.CHANNEL_ID;

public class AutoRecordService extends Service implements OnActionCallback {
    private MediaRecorder mediaRecorder;
     private boolean isRecording;
    private RecordDAO recordDAO;
    private int status = 0;
    private static String phoneNumber;
    private OnActionCallback onActionCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildStartNotification();
        recordDAO = RecordDatabase.getInstance(this).recordDAO();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        phoneNumber = intent.getStringExtra("phoneNumber");
        if (intent.getAction()==("OFFHOOK")){
            createNotificationChannel();
            buildStartNotification();
            startRecording();
        }else if(intent.getAction()==("IDLE")){
            stopRecording();
            stopSelf();
        }else if (intent.getAction()=="RINGING"){
            status = 1;
            Log.i("aaa","incoming call");
        }
        return START_STICKY;
    }

    private void startRecording() {
        if (!isRecording){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();
            isRecording = true;
            Log.i("aaa","start recording");
        }
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDir,"test recording 2"+".mp3");
        return file.getPath();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void stopRecording() {
        if (isRecording){
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            saveRecord();
            isRecording = false;
            Log.i("aaa","stop recording");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveRecord() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now();
        RecordModel recordModel = new RecordModel(""+phoneNumber,status,dtf.format(dateTime),0,0);
        recordDAO.insertRecord(recordModel);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void buildStartNotification() {
        Intent notificationIntent = new Intent(getBaseContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notification Title")
                .setContentText("input")
                .setShowWhen(false)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onCallbackDB(String key, RecordModel model) {

    }
}

