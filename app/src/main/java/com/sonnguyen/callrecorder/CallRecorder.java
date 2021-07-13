package com.sonnguyen.callrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CallRecorder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            Toast.makeText(context, "Start Call", Toast.LENGTH_SHORT).show();
        }else if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE)){
            Toast.makeText(context, "End Call", Toast.LENGTH_SHORT).show();
        }
    }
}
