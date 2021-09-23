package com.sonnguyen.callrecorder.ui.activity.Second;

import android.content.Intent;
import android.os.Bundle;

import com.sonnguyen.callrecorder.ui.activity.Main.MainActivity;
import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseAct;
import com.sonnguyen.callrecorder.datasource.model.CallerModel;
import com.sonnguyen.callrecorder.ui.fragment.TrackCaller.TrackCallerFragment;

public class SecondActivity extends BaseAct<SecondActViewModel> implements OnActionCallbackFragment {
    private static final String TAG = "aaa";
    public static final String KEYBUNDLE_SECOND_TO_TRACK_CALLER = "KEYBUNDLE_SECOND_TO_TRACK_CALLER";
    private TrackCallerFragment trackCallerFragment;
    private CallerModel callerModel;

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            callerModel = (CallerModel) bundle.getSerializable(MainActivity.KEYBUNDLE_MAIN_TO_SECOND);
        }

        showTrackCallerFragment();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_second;
    }

    @Override
    protected Class<SecondActViewModel> getClassViewModel() {
        return SecondActViewModel.class;
    }

    private void showTrackCallerFragment() {
        trackCallerFragment = new TrackCallerFragment();
        trackCallerFragment.setCallBack(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEYBUNDLE_SECOND_TO_TRACK_CALLER,callerModel);
        trackCallerFragment.setArguments(bundle);
        showFragment(R.id.frame_layout_second,trackCallerFragment,false);
    }

    @Override
    public void onCallback(String key, Object object) {

    }
}
