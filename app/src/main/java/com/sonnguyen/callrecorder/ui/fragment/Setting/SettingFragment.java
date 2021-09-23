package com.sonnguyen.callrecorder.ui.fragment.Setting;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackService;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;

public class SettingFragment extends BaseFragment<SettingViewModel> {
    private static final String KEY_SHARE_PREFERENCES = "KEY_SHARE_PREFERENCES";
    private static final String KEY_SETTING_CALL_RECORD = "KEY_SETTING_CALL_RECORD";
    private static final String TAG = "aaa";
    public static final String KEY_SETTING_TO_MAIN_TO_SERVICE = "KEY_SETTING_TO_SERVICE";
    public static final String KEY_TIME_LIMIT = "KEY_TIME_LIMIT";
    public static final String KEY_AUTO_DELETE = "KEY_AUTO_DELETE";
    private OnActionCallbackFragment callbackFragment;
    private OnActionCallbackService callbackService;
    private ImageView imvBack;
    private TextView tvAutoDelete,tvTimeLimit;
    private Button btSubmitTimeLimit,btSubmitAutoDelete;
    private ImageView imv5Minute, imv10Minute, imv15Minute, imv20Minute,imvNoLimit;
    private ImageView imv15Days, imv1Month, imv3Month, imv6Month,imvNeverDelete;
    private SwitchCompat switchCallRecord,switchActiveNoti,switchAfterNoti;
    private ConstraintLayout constraintLimitTime,constraintAutoDelete,constraintCallerSetting;
    private Dialog dialog;

    private boolean check5Minutes, check10Minutes, check15Minutes, check20Minutes,checkNoLimit;
    private boolean check15Days, check1Month, check3Months, check6Months,checkNeverDelete;
    private SharedPreferences sharedPreferences;

    @Override
    protected void initViews() {
        sharedPreferences = getContext().getSharedPreferences(KEY_SHARE_PREFERENCES, Context.MODE_PRIVATE);

        imvBack = findViewById(R.id.imv_setting_back);
        switchCallRecord = findViewById(R.id.switch_missed_call);
        constraintAutoDelete = findViewById(R.id.constraint_auto_delete);
        constraintCallerSetting = findViewById(R.id.constraint_setting_for_caller);
        constraintLimitTime = findViewById(R.id.constraint_limit_time_record);

        tvTimeLimit = findViewById(R.id.tv_content_limit_time_recording);
        tvAutoDelete = findViewById(R.id.tv_content_auto_delete);
        getDataFromSharePre();

    }

    private void getDataFromSharePre() {
        int timeLimit = sharedPreferences.getInt(KEY_TIME_LIMIT,4);
        switch (timeLimit){
            case 0:
                tvTimeLimit.setText(R.string.minute5);
                break;
            case 1:
                tvTimeLimit.setText(R.string.minute10);
                break;
            case 2:
                tvTimeLimit.setText(R.string.minute15);
                break;
            case 3:
                tvTimeLimit.setText(R.string.minute20);
                break;
            case 4:
                tvTimeLimit.setText(R.string.no_limit);
                break;
        }

        int autoDelete = sharedPreferences.getInt(KEY_AUTO_DELETE,4);
        switch (autoDelete){
            case 0:
                tvAutoDelete.setText(R.string.delete_15days);
                break;
            case 1:
                tvAutoDelete.setText(R.string.delete_1month);
                break;
            case 2:
                tvAutoDelete.setText(R.string.delete_3months);
                break;
            case 3:
                tvAutoDelete.setText(R.string.delete_6months);
                break;
            case 4:
                tvAutoDelete.setText(R.string.never_delete);
                break;
        }

        boolean checkCallRecording = sharedPreferences.getBoolean(KEY_SETTING_CALL_RECORD,true);
        switchCallRecord.setChecked(checkCallRecording);
    }

    @Override
    protected void initEvents() {
        imvBack.setOnClickListener(v -> getFragmentManager().popBackStack());
        switchCallRecord.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(!isChecked){
                callbackService.onCallbackService(KEY_SETTING_TO_MAIN_TO_SERVICE,false);
                editor.putBoolean(KEY_SETTING_CALL_RECORD, false);
                editor.commit();
            }else{
                callbackService.onCallbackService(KEY_SETTING_TO_MAIN_TO_SERVICE,true);
                editor.putBoolean(KEY_SETTING_CALL_RECORD, true);
                editor.commit();
            }
        });
        constraintLimitTime.setOnClickListener(v -> showLimitTimeDialog());
        constraintAutoDelete.setOnClickListener(v -> showAutoDeleteDialog());

    }

    private void showAutoDeleteDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_auto_delete_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        imv15Days = dialog.findViewById(R.id.imv_15days_check);
        imv1Month = dialog.findViewById(R.id.imv_1month_check);
        imv3Month = dialog.findViewById(R.id.imv_3months_check);
        imv6Month = dialog.findViewById(R.id.imv_6months_check);
        imvNeverDelete = dialog.findViewById(R.id.imv_never_delete_check);
        btSubmitAutoDelete = dialog.findViewById(R.id.bt_submit_auto_delete);

        dialogAutoDeleteOnClick();
    }

    private void dialogAutoDeleteOnClick() {
        imv15Days.setOnClickListener(v -> {
            check15Days = true;
            check1Month = false;
            check3Months = false;
            check6Months = false;
            checkNeverDelete = false;
            imv15Days.setBackgroundResource(R.drawable.ic_checked);
            imv1Month.setBackgroundResource(R.drawable.ic_unchecked);
            imv3Month.setBackgroundResource(R.drawable.ic_unchecked);
            imv6Month.setBackgroundResource(R.drawable.ic_unchecked);
            imvNeverDelete.setBackgroundResource(R.drawable.ic_unchecked);
        });

        imv1Month.setOnClickListener(v -> {
            check15Days = false;
            check1Month = true;
            check3Months = false;
            check6Months = false;
            checkNeverDelete = false;
            imv15Days.setBackgroundResource(R.drawable.ic_unchecked);
            imv1Month.setBackgroundResource(R.drawable.ic_checked);
            imv3Month.setBackgroundResource(R.drawable.ic_unchecked);
            imv6Month.setBackgroundResource(R.drawable.ic_unchecked);
            imvNeverDelete.setBackgroundResource(R.drawable.ic_unchecked);
        });

        imv3Month.setOnClickListener(v -> {
            check15Days = false;
            check1Month = false;
            check3Months = true;
            check6Months = false;
            checkNeverDelete = false;
            imv15Days.setBackgroundResource(R.drawable.ic_unchecked);
            imv1Month.setBackgroundResource(R.drawable.ic_unchecked);
            imv3Month.setBackgroundResource(R.drawable.ic_checked);
            imv6Month.setBackgroundResource(R.drawable.ic_unchecked);
            imvNeverDelete.setBackgroundResource(R.drawable.ic_unchecked);
        });

        imv6Month.setOnClickListener(v -> {
            check15Days = false;
            check1Month = false;
            check3Months = false;
            check6Months = true;
            checkNeverDelete = false;
            imv15Days.setBackgroundResource(R.drawable.ic_unchecked);
            imv1Month.setBackgroundResource(R.drawable.ic_unchecked);
            imv3Month.setBackgroundResource(R.drawable.ic_unchecked);
            imv6Month.setBackgroundResource(R.drawable.ic_checked);
            imvNeverDelete.setBackgroundResource(R.drawable.ic_unchecked);
        });

        imvNeverDelete.setOnClickListener(v -> {
            check15Days = false;
            check1Month = false;
            check3Months = false;
            check6Months = false;
            checkNeverDelete = true;
            imv15Days.setBackgroundResource(R.drawable.ic_unchecked);
            imv1Month.setBackgroundResource(R.drawable.ic_unchecked);
            imv3Month.setBackgroundResource(R.drawable.ic_unchecked);
            imv6Month.setBackgroundResource(R.drawable.ic_unchecked);
            imvNeverDelete.setBackgroundResource(R.drawable.ic_checked);
        });

        btSubmitAutoDelete.setOnClickListener(v -> {
            boolean[] checkAutoDelete = {check15Days,check1Month,check3Months,check6Months,checkNeverDelete};
            for (int i = 0; i < checkAutoDelete.length; i++) {
                if(checkAutoDelete[i]){
                    switch (i){
                        case 0:
                            tvAutoDelete.setText(R.string.delete_15days);
                            break;
                        case 1:
                            tvAutoDelete.setText(R.string.delete_1month);
                            break;
                        case 2:
                            tvAutoDelete.setText(R.string.delete_3months);
                            break;
                        case 3:
                            tvAutoDelete.setText(R.string.delete_6months);
                            break;
                        case 4:
                            tvAutoDelete.setText(R.string.never_delete);
                            break;
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(KEY_AUTO_DELETE,i);
                    editor.commit();
                    callbackFragment.onCallback(KEY_AUTO_DELETE,i);
                    dialog.dismiss();
                }
            }
        });
    }

    private void showLimitTimeDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_limit_time_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        imv5Minute = dialog.findViewById(R.id.imv_5minute_check);
        imv10Minute = dialog.findViewById(R.id.imv_10minute_check);
        imv15Minute = dialog.findViewById(R.id.imv_15minute_check);
        imv20Minute = dialog.findViewById(R.id.imv_20minute_check);
        imvNoLimit = dialog.findViewById(R.id.imv_nolimit_check);
        btSubmitTimeLimit = dialog.findViewById(R.id.bt_submit_time_setting);

        dialogTimeLimitOnClick();

    }

    private void dialogTimeLimitOnClick() {
        imv5Minute.setOnClickListener(v -> {
            check5Minutes = true;
            check10Minutes = false;
            check15Minutes = false;
            check20Minutes = false;
            checkNoLimit = false;
            imv5Minute.setBackgroundResource(R.drawable.ic_checked);
            imv10Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv15Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv20Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imvNoLimit.setBackgroundResource(R.drawable.ic_unchecked);
        });

        imv10Minute.setOnClickListener(v -> {
            check10Minutes = true;
            check5Minutes = false;
            check15Minutes = false;
            check20Minutes = false;
            checkNoLimit = false;
            imv5Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv10Minute.setBackgroundResource(R.drawable.ic_checked);
            imv15Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv20Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imvNoLimit.setBackgroundResource(R.drawable.ic_unchecked);
        });

        imv15Minute.setOnClickListener(v -> {
            check15Minutes = true;
            check5Minutes = false;
            check10Minutes = false;
            check20Minutes = false;
            checkNoLimit = false;
            imv5Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv10Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv15Minute.setBackgroundResource(R.drawable.ic_checked);
            imv20Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imvNoLimit.setBackgroundResource(R.drawable.ic_unchecked);
        });

        imv20Minute.setOnClickListener(v -> {
            check20Minutes = true;
            check5Minutes = false;
            check10Minutes = false;
            check15Minutes = false;
            checkNoLimit = false;
            imv5Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv10Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv15Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv20Minute.setBackgroundResource(R.drawable.ic_checked);
            imvNoLimit.setBackgroundResource(R.drawable.ic_unchecked);
        });

        imvNoLimit.setOnClickListener(v -> {
            checkNoLimit = true;
            check5Minutes = false;
            check10Minutes = false;
            check15Minutes = false;
            check20Minutes = false;
            imv5Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv10Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv15Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imv20Minute.setBackgroundResource(R.drawable.ic_unchecked);
            imvNoLimit.setBackgroundResource(R.drawable.ic_checked);
        });

        btSubmitTimeLimit.setOnClickListener(v -> {
            boolean[] checkTimeLimit = {check5Minutes, check10Minutes, check15Minutes, check20Minutes,checkNoLimit};
            for (int i = 0; i < checkTimeLimit.length; i++) {
                if(checkTimeLimit[i]){
                    switch (i){
                        case 0:
                            tvTimeLimit.setText(R.string.minute5);
                            break;
                        case 1:
                            tvTimeLimit.setText(R.string.minute10);
                            break;
                        case 2:
                            tvTimeLimit.setText(R.string.minute15);
                            break;
                        case 3:
                            tvTimeLimit.setText(R.string.minute20);
                            break;
                        case 4:
                            tvTimeLimit.setText(R.string.no_limit);
                            break;
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(KEY_TIME_LIMIT,i);
                    editor.commit();
                    callbackService.onCallbackService(KEY_TIME_LIMIT,i);
                    dialog.dismiss();
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
