package com.sonnguyen.callrecorder;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.sonnguyen.callrecorder.ui.activity.MainActivity;

public class CallRecorder extends BroadcastReceiver {
    private static final String SON = "SON";
    private MediaRecorder mediaRecorder;
    private PhoneStateListener phoneStateListener;
    private String callState;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                Intent intent1 = new Intent(context, AutoRecordService.class);
                intent1.putExtra("phoneNumber", phoneNumber);
                intent1.putExtra("inputExtra", "Screen rights");
//                if (state==TelephonyManager.CALL_STATE_IDLE){
//                    intent1.setAction("IDLE");
//                }else if (state==TelephonyManager.CALL_STATE_RINGING){
//                    intent1.setAction("RINGING");
//                }else if (state==TelephonyManager.CALL_STATE_OFFHOOK){
//                    Log.i("aaa", "reveiver call state ofhook: " + phoneNumber);
//                    intent1.setAction("OFFHOOK");
//                }else{
//                    intent1.setAction("OUTGOINGCALL");
//                }
                switch (state){
                    case TelephonyManager.CALL_STATE_IDLE:
                        intent1.setAction("IDLE");
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        intent1.setAction("RINGING");
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        intent1.setAction("OFFHOOK");
                        Log.i("aaa", "reveiver call state ofhook: " + phoneNumber);
                        break;
                    default:
                        intent1.setAction("OUTGOINGCALL");
                        break;
                }
                context.startService(intent1);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
}

