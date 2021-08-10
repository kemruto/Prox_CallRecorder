package com.sonnguyen.callrecorder.ui.fragment.Home;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<HomeViewModel> implements OnActionCallbackFragment {
    public static final String KEY_HOME_FRAGMENT_TO_DETAIL = "KEY_HOME_FRAGMENT_TO_DETAIL";

    private HomeAdapter homeAdapter;
    private List<RecordModel> listRecord;
    private RecyclerView recyclerView;
    private Button btShowDialog;
    private OnActionCallbackFragment callbackFragment;
    private RecordDAO recordDAO;

    @Override
    protected Class getClassModel() {
        return HomeViewModel.class;
    }

    @Override
    protected void initEvents() {
        btShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callbackFragment.onCallback(KEY_HOME_FRAGMENT_TO_DETAIL,null);
            }
        });
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
        recyclerView = findViewById(R.id.list_item);
        listRecord = new ArrayList<>();
        btShowDialog = findViewById(R.id.bt_dialog);
        showListRecord();
    }

    private void showListRecord() {
        listRecord = mViewModel.getListRecordModel();
        if (listRecord.size() == 0){
            Toast.makeText(getContext(), "Null", Toast.LENGTH_SHORT).show();
        }else{
            homeAdapter = new HomeAdapter(listRecord,getContext());
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

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }

    @Override
    public void onCallback(String key, Object object) {

    }
}
