package com.sonnguyen.callrecorder.ui.fragment.Home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.MessageEvent;
import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.activity.MainActivity;
import com.sonnguyen.callrecorder.ui.fragment.Detail.DetailFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<HomeViewModel> implements OnActionCallbackFragment{

    public static final String KEY_HOME_TO_MAIN_TO_DETAIL = "KEY_HOME_TO_MAIN_TO_DETAIL";
    public static final String KEY_DETAIL_TO_MAIN_TO_ADD_NOTE = "KEY_DETAIL_TO_MAIN_TO_ADD_NOTE";

    private HomeAdapter homeAdapter;
    private List<RecordModel> listRecord;
    private RecyclerView recyclerView;
    private OnActionCallbackFragment callbackFragment;
    private RecordDAO recordDAO;
    private Thread thread;
    private int autoDeleteDay;

    @Override
    protected Class getClassModel() {
        return HomeViewModel.class;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void onResume() {
        super.onResume();
        showListRecord();
    }

    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        recordDAO = RecordDatabase.getInstance(getContext()).recordDAO();
        autoDeleteDay = MainActivity.getMainActivityInstance().getAutoDeleteDay();
        recyclerView = findViewById(R.id.list_item);
        listRecord = new ArrayList<>();
        showListRecord();
        backgroundAutoDeleteThread();
    }

    private void backgroundAutoDeleteThread() {
        thread = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                    mViewModel.autoDelete(autoDeleteDay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void showListRecord() {
        listRecord = mViewModel.getListRecordModel();
        if (listRecord.size() == 0){
            //
        }else{
            homeAdapter = new HomeAdapter(listRecord,getContext());
            homeAdapter.setCallback(this);
            homeAdapter.setNewData(listRecord);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(homeAdapter);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCallback(String key, Object object) {
        switch (key){
            case HomeAdapter.KEY_RECORD_TO_DETAIL:
                RecordModel recordModel = (RecordModel) object;
                callbackFragment.onCallback(KEY_HOME_TO_MAIN_TO_DETAIL,recordModel);
                break;
            case DetailFragment.KEY_DETAIL_TO_ADD_NOTE:
                callbackFragment.onCallback(KEY_DETAIL_TO_MAIN_TO_ADD_NOTE,null);
                break;
        }
    }

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent){
        Log.i("aaa","Event fired");
        listRecord = mViewModel.getListRecordModel();
        homeAdapter.setNewData(listRecord);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(homeAdapter);
    }

}
