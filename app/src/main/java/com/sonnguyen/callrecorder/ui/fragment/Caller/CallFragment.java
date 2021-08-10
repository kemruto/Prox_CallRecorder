package com.sonnguyen.callrecorder.ui.fragment.Caller;

import android.view.View;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;

public class CallFragment extends BaseFragment implements OnActionCallbackFragment {

    public static final String KEY_CALL_FRAGMENT_TO_DETAIL = "KEY_CALL_FRAGMENT_TO_DETAIL";
    private OnActionCallbackFragment callbackFragment;

    @Override
    protected Class getClassModel() {
        return CallViewModel.class;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_caller;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCallback(String key, Object object) {

    }

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
}
