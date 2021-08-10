package com.sonnguyen.callrecorder.ui.fragment.OutgoingCall;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class OutgoingCallFragment extends BaseFragment<OutgoingCallViewModel> {
    public static final String KEY_OUTGOING_FRAGMENT_TO_DETAIL = "KEY_OUTGOING_FRAGMENT_TO_DETAIL";
    private OnActionCallbackFragment callbackFragment;

    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;
    private OutgoingCallAdapter outgoingCallAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onResume() {
        super.onResume();
        showListOutgoing();
    }

    @Override
    protected Class getClassModel() {
        return OutgoingCallViewModel.class;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        recyclerView = findViewById(R.id.list_item_outgoing);
        listRecord = new ArrayList<>();
        listRecord = mViewModel.getListOutgoingRecord();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showListOutgoing();
    }

    private void showListOutgoing() {
        if (listRecord.size()==0){
            Toast.makeText(getContext(),"Null",Toast.LENGTH_SHORT).show();
        }else{
            outgoingCallAdapter = new OutgoingCallAdapter(listRecord,getContext());
            outgoingCallAdapter.setNewData(listRecord);
            recyclerView.setAdapter(outgoingCallAdapter);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_outgoing_call;
    }

    @Override
    public void onClick(View v) {

    }

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
}
