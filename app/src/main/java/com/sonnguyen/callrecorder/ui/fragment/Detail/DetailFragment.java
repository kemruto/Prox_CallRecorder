package com.sonnguyen.callrecorder.ui.fragment.Detail;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;

public class DetailFragment extends BaseFragment<DetailViewModel>{
    private OnActionCallbackFragment callbackFragment;
    private ImageView imvBack,imvShare,imvBin,imvAddNote,imvTrim,imvPlay;

    @Override
    protected Class getClassModel() {
        return DetailViewModel.class;
    }

    @Override
    protected void initEvents() {
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    protected void initViews() {
        imvBack = findViewById(R.id.imv_back_detail);
        imvShare = findViewById(R.id.imv_share);
        imvBin = findViewById(R.id.imv_bin);
        imvAddNote = findViewById(R.id.imv_add_note);
        imvTrim = findViewById(R.id.imv_trimmed);
        imvPlay = findViewById(R.id.imv_play_detail);
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

}

