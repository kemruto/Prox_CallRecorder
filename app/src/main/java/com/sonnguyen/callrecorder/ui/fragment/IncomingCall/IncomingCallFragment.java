package com.sonnguyen.callrecorder.ui.fragment.IncomingCall;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class IncomingCallFragment extends BaseFragment<IncomingCallViewModel> implements OnActionCallbackFragment {
    private OnActionCallbackFragment callbackFragment;
    private RecyclerView recyclerView;
    private IncomingCallAdapter incomingCallAdapter;
    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;

    @Override
    protected Class getClassModel() {
        return IncomingCallViewModel.class;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        listRecord = new ArrayList<>();
        recyclerView = findViewById(R.id.list_item_incoming);
        showListIncoming();
    }

    @Override
    public void onResume() {
        super.onResume();
        showListIncoming();
    }

    private void showListIncoming() {
        listRecord = mViewModel.getListIncomingRecord();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (listRecord.size()==0){
            Toast.makeText(getContext(),"Null",Toast.LENGTH_SHORT).show();
        }else{
            incomingCallAdapter = new IncomingCallAdapter(listRecord,getContext());
            incomingCallAdapter.setNewData(listRecord);
            recyclerView.setAdapter(incomingCallAdapter);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_incoming_call;
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
