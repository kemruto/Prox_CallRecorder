package com.sonnguyen.callrecorder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.activity.MainActivity;
import com.sonnguyen.callrecorder.ui.activity.SecondActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static com.sonnguyen.callrecorder.utils.app.App.CHANNEL_ID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AutoRecordService extends Service {
    private static final String TAG = "aaa";
    private static String phoneNumber;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    DateTimeFormatter createFormatDate = DateTimeFormatter.ISO_DATE;
    LocalDateTime localDateTime = LocalDateTime.now();
    private MediaRecorder mediaRecorder;
    private boolean isRecording;
    private RecordDAO recordDAO;
    private int status = 0;
    private String recordFile,phoneContact="";
    private File parentDir;
    private String fileName;

    private boolean addView;
    private boolean removeView;
    private WindowManager mWindowManager;
    private View myFloatingView;
    private ImageView imvMic;
    private boolean muteRecording = false;
    private boolean autoRecord = true;
    private boolean pauseRecord;
    private int duration;
    private Thread thread;

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
            startRecording(duration);
            return;
        }else{
            muteRecording = true;
            imvMic.setBackgroundResource(R.drawable.ic_mic_off_24);
            stopRecording();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        phoneNumber = intent.getStringExtra("phoneNumber");
        autoRecord = MainActivity.getMainActivityInstance().getAutoRecord();
        duration = MainActivity.getMainActivityInstance().getDurationRecord();
//        phoneContact = getContactNameByNumber(phoneNumber);
        if (intent.getAction() == "OFFHOOK") {
            createNotificationChannel();
            buildStartNotification();
            createView();
            if (autoRecord) {
                startRecording(duration);
            } else {
                backgroundMuteRecord();
            }
        } else if (intent.getAction() == "IDLE") {
            stopRecording();
        } else if (intent.getAction() == "RINGING") {
            status = 1;
            Log.i(TAG, "Incoming call");
        }
        return START_STICKY;
    }

    private void backgroundMuteRecord() {
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                    muteRecording();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        };
        thread.start();
    }

    private void startRecording(int duration) {
        if (!isRecording) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mediaRecorder.setMaxDuration(duration);
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
            if (!removeView && addView) {
                removeView = true;
                mWindowManager.removeView(myFloatingView);
            }
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            phoneContact = getContactNameByNumber(phoneNumber);
            RecordModel recordModel = new RecordModel(phoneContact,phoneNumber
                    , status, formatter.format(localDateTime), 0, 0, fileName, ""
                    ,createFormatDate.format(localDateTime),"");
            recordDAO.insertRecord(recordModel);
            isRecording = false;
            Log.i("aaa", "stop recording");
            stopSelf();
        }
    }

    public String getContactNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = "";

        ContentResolver contentResolver = this.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[]{BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }
        return name;
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

        myFloatingView.findViewById(R.id.floating_view).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroyService: ");
    }
}

