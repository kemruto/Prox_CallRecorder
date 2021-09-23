package com.sonnguyen.callrecorder.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.sonnguyen.callrecorder.service.AutoRecordService;

public class CallReceiver extends BroadcastReceiver{
    private static final String TAG = "aaa";
    private static PhoneStateListener phoneStateListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (phoneStateListener==null){
            telephonyManager.listen(phoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String phoneNumber) {
                    super.onCallStateChanged(state, phoneNumber);
                    Intent intent1 = new Intent(context, AutoRecordService.class);
                    Log.i(TAG, "phoneNumber in broadcast: "+phoneNumber);
                    intent1.putExtra("phoneNumber", phoneNumber);
                    switch (state){
                        case TelephonyManager.CALL_STATE_IDLE:
                            intent1.setAction("IDLE");
                            Log.i(TAG, "onIdle: ");
                            break;
                        case TelephonyManager.CALL_STATE_RINGING:
                            intent1.setAction("RINGING");
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            intent1.setAction("OFFHOOK");
                            Log.i(TAG, "onOffhook: ");
                            break;
                        default:
                            intent1.setAction("OUTGOINGCALL");
                            break;
                    }
                    ContextCompat.startForegroundService(context, intent1);
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}

