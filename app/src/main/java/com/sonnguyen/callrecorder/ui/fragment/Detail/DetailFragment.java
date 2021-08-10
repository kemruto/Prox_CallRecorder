package com.sonnguyen.callrecorder.ui.fragment.Detail;

import android.view.View;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;

public class DetailFragment extends BaseFragment implements OnActionCallbackFragment {
    private OnActionCallbackFragment callbackFragment;

    @Override
    protected Class getClassModel() {
        return DetailViewModel.class;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_call_detail;
    }

    @Override
    public void onClick(View v) {

    }

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }

    @Override
    public void onCallback(String key, Object object) {

    }
}

