package com.sonnguyen.callrecorder;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.sonnguyen.callrecorder.service.AutoRecordService;
import com.sonnguyen.callrecorder.ui.activity.MainActivity;

public class CallReceiver extends BroadcastReceiver{
    private static final String TAG = "aaa";
    private static PhoneStateListener phoneStateListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Log.i(TAG, "phoneStateListener = "+phoneStateListener);

        if (phoneStateListener==null){
            telephonyManager.listen(phoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String phoneNumber) {
                    super.onCallStateChanged(state, phoneNumber);
                    Intent intent1 = new Intent(context, AutoRecordService.class);
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
                    context.startService(intent1);
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}

