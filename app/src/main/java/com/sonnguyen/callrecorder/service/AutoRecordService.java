package com.sonnguyen.callrecorder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sonnguyen.callrecorder.MessageEvent;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static com.sonnguyen.callrecorder.utils.app.App.CHANNEL_ID;
import static com.sonnguyen.callrecorder.utils.app.App.getContext;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AutoRecordService extends Service {
    private static final String TAG = "aaa";
    private static String phoneNumber;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now();
    private MediaRecorder mediaRecorder;
    private boolean isRecording;
    private RecordDAO recordDAO;
    private int status = 0;
    private String recordFile;
    private File parentDir;
    private String fileName;

    private boolean addView;
    private boolean removeView;
    private WindowManager mWindowManager;
    private View myFloatingView;
    private ImageView imvMic;
    private boolean muteRecording = false;
    private boolean autoRecord;

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
        myFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_view, null);
        imvMic = myFloatingView.findViewById(R.id.imv_mic);
        imvMic.setOnClickListener(v -> {
            muteRecording();
        });
    }

    private void muteRecording() {
        if (muteRecording) {
            imvMic.setBackgroundResource(R.drawable.ic_mic_on_24);
            muteRecording = false;
            return;
        }
        muteRecording = true;
        imvMic.setBackgroundResource(R.drawable.ic_mic_off_24);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        phoneNumber = intent.getStringExtra("phoneNumber");
        autoRecord = intent.getBooleanExtra("KEY_AUTO_RECORD", true);
        if (intent.getAction().equals("OFFHOOK")) {
            createNotificationChannel();
            buildStartNotification();
//            createView();
            startRecording();
        } else if (intent.getAction().equals("IDLE")) {
            stopRecording();
//            if (!removeView) {
//                removeView = true;
//                mWindowManager.removeView(myFloatingView);
//            }
            stopSelf();
        } else if (intent.getAction() == "RINGING") {
            status = 1;
            Log.i(TAG, "incoming call");
        }
        return START_STICKY;
    }

    private void startRecording() {
        if (!isRecording) {
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
            Log.i("aaa", "start recording");
        }
    }

    private String getRecordingFilePath() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();
        String date = simpleDateFormat.format(now);
        recordFile = "Recording_" + phoneNumber + "_" + date + ".m4a";
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        parentDir = contextWrapper.getExternalFilesDir(Environment.getStorageDirectory().getPath());
        fileName = parentDir + "/" + recordFile;
        File file = new File(parentDir, recordFile + "");
        return file.getPath();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            RecordModel recordModel = new RecordModel(phoneNumber
                    , status, formatter.format(dateTime), "", 0, 0, fileName, "");
            recordDAO.insertRecord(recordModel);
            isRecording = false;
            Log.i("aaa", "stop recording");
        }
    }

    private void createView() {
        int layout_parms;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layout_parms = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layout_parms = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layout_parms,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.x = 600;
        params.y = 200;

        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (!addView) {
            addView = true;
            mWindowManager.addView(myFloatingView, params);
        }

        //adding an touchlistener to make drag movement of the floating widget
        myFloatingView.findViewById(R.id.floating_view).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TOUCH", "THIS IS TOUCHED");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(myFloatingView, params);
                        return true;
                }
                return false;
            }
        });
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

}

