package com.sonnguyen.callrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.sonnguyen.callrecorder.view.activity.MainActivity;

import java.io.File;
import java.io.IOException;

public class CallRecorder extends BroadcastReceiver {
    private static final String SON = "SON";
    private MediaRecorder mediaRecorder;
    private PhoneStateListener phoneStateListener;
    private String callState;

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                Intent intent1 = new Intent(context, AutoRecordService.class);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        callState = "IDLE";
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        callState = "OFFHOOK";
                        break;
                }
                intent1.putExtra("phoneNumber", phoneNumber);
                intent1.putExtra("callState", callState);
                ContextCompat.startForegroundService(context, intent1);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }


//    private void stopRecording() {
//        Log.i(SON, "stop");
//        mediaRecorder.stop();
//        mediaRecorder.release();
//        mediaRecorder = null;
//    }
//
//    private void startRecording() {
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setOutputFile(getRecordingFilePath());
//        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        Log.i(SON, "start");
//        try {
//            mediaRecorder.prepare();
//            mediaRecorder.start();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getRecordingFilePath() {
//        ContextWrapper contextWrapper = new ContextWrapper(activity);
//        File musicDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        File file = new File(musicDir, "test recording" + ".mp3");
//        return file.getPath();
//    }
}

