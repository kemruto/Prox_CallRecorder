package com.sonnguyen.callrecorder.ui.fragment.Caller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.model.CallerModel;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.activity.MainActivity;
import com.sonnguyen.callrecorder.ui.activity.SecondActivity;

import java.util.ArrayList;
import java.util.List;

public class CallerFragment extends BaseFragment<CallerViewModel> implements OnActionCallbackFragment {
    public static final String KEY_CALLER_TO_TRACKING_ACT = "KEY_CALLER_TO_TRACKING_ACT";
    private EditText edtTracking;
    private ImageView imvTracking,imvBin;
    private String phoneNumber;
    private OnActionCallbackFragment callbackFragment;
    private List<CallerModel> listCaller;
    private RecyclerView recyclerView;
    private CallerAdapter callerAdapter;

    @Override
    protected Class getClassModel() {
        return CallerViewModel.class;
    }

    @Override
    protected void initEvents() {
        imvTracking.setOnClickListener(v -> trackPhone());
        imvBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteCaller(listCaller);
                listCaller.clear();
                callerAdapter.setNewData(listCaller);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(callerAdapter);
            }
        });
    }

    private void trackPhone() {
        phoneNumber = edtTracking.getText().toString();
        callbackFragment.onCallback(KEY_CALLER_TO_TRACKING_ACT,phoneNumber);
    }

    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        listCaller = new ArrayList<>();
        recyclerView = findViewById(R.id.list_item_caller);
        edtTracking = (EditText) findViewById(R.id.edt_tracking);
        imvTracking = (ImageView) findViewById(R.id.imv_tracking);
        imvBin = findViewById(R.id.imv_bin);

        showListCaller();
    }

    @Override
    public void onResume() {
        super.onResume();
        showListCaller();
    }

    private void showListCaller() {
        listCaller = mViewModel.getListCaller();
        if (listCaller.size() == 0){
            //
        }else{
            callerAdapter = new CallerAdapter(listCaller,getContext());
            callerAdapter.setCallback(this);
            callerAdapter.setNewData(listCaller);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(callerAdapter);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_caller;
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
