package com.sonnguyen.callrecorder.ui.fragment.Trimmed;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class TrimmedViewModel extends BaseViewModel {
    private List<RecordModel> listRecord;
    private Context context;
    private RecordDAO recordDAO;

    public TrimmedViewModel() { listRecord = new ArrayList<>(); }

    public void setContext(Context context){
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }

    public List<RecordModel> getListTrimmedRecord(){
        listRecord = recordDAO.listTrimmedRecord();
        return listRecord;
    }
}
