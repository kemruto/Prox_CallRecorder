package com.sonnguyen.callrecorder.ui.fragment.Trimmed;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class TrimmedFragment extends BaseFragment<TrimmedViewModel> {
    private RecyclerView recyclerView;
    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;
    private TrimmedAdapter trimmedAdapter;

    @Override
    protected Class getClassModel() {
        return TrimmedViewModel.class;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        recyclerView = findViewById(R.id.list_item_trimmed);
        listRecord = new ArrayList<>();
        listRecord = mViewModel.getListTrimmedRecord();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showListTrimmedRecord();
    }

    private void showListTrimmedRecord() {
        if(listRecord.size()==0){
            Toast.makeText(getContext(),"Null",Toast.LENGTH_SHORT).show();
        }else{
            trimmedAdapter = new TrimmedAdapter(listRecord,getContext());
            trimmedAdapter.setNewData(listRecord);
            recyclerView.setAdapter(trimmedAdapter);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_trimmed;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        showListTrimmedRecord();
    }
}
