package com.sonnguyen.callrecorder.ui.fragment.Setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.OnActionCallbackService;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;

public class SettingFragment extends BaseFragment<SettingViewModel> {
    private static final String KEY_SHARE_PREFERENCES = "KEY_SHARE_PREFERENCES";
    private static final String KEY_SETTING_CALL_RECORD = "KEY_SETTING_CALL_RECORD";
    private static final String TAG = "aaa";
    public static final String KEY_SETTING_TO_MAIN_TO_SERVICE = "KEY_SETTING_TO_SERVICE";
    private OnActionCallbackFragment callbackFragment;
    private OnActionCallbackService callbackService;
    private ImageView imvBack;
    private TextView tvAutoDelete,tvTimeLimit;
    private SwitchCompat switchCallRecord,switchActiveNoti,switchAfterNoti;
    private ConstraintLayout constraintLimitTime,constraintAutoDelete,constraintCallerSetting;

    private SharedPreferences sharedPreferences;

    @Override
    protected void initViews() {
        imvBack = findViewById(R.id.imv_setting_back);
        switchCallRecord = findViewById(R.id.switch_call_recording);
        constraintAutoDelete = findViewById(R.id.constraint_auto_delete);
        constraintCallerSetting = findViewById(R.id.constraint_setting_for_caller);
        constraintLimitTime = findViewById(R.id.constraint_limit_time_record);

        sharedPreferences = getContext().getSharedPreferences(KEY_SHARE_PREFERENCES, Context.MODE_PRIVATE);

        boolean checkCallRecording = sharedPreferences.getBoolean(KEY_SETTING_CALL_RECORD,true);
        switchCallRecord.setChecked(checkCallRecording);
    }

    @Override
    protected void initEvents() {
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        switchCallRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                if(!isChecked){
                    callbackService.onCallbackService(KEY_SETTING_TO_MAIN_TO_SERVICE,false);
                    editor.putBoolean(KEY_SETTING_CALL_RECORD, false);
                    editor.commit();
                }else{
                    callbackService.onCallbackService(KEY_SETTING_TO_MAIN_TO_SERVICE,true);
                    editor.putBoolean(KEY_SETTING_CALL_RECORD, true);
                    editor.commit();
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected Class<SettingViewModel> getClassModel() {
        return SettingViewModel.class;
    }

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }

    public void setCallBackService(OnActionCallbackService callBackService){
        this.callbackService = callBackService;
    }

    @Override
    public void onClick(View v) {

    }


}
