package com.sonnguyen.callrecorder.ui.activity.Search;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends BaseViewModel {
    private List<RecordModel> listRecord;
    private List<RecordModel> listSearch;
    private RecordDAO recordDAO;
    private Context context;
    private RecordModel recordModel;

    public SearchViewModel(){
        listRecord = new ArrayList<>();
        listSearch = new ArrayList<>();
    }

    public List<RecordModel> getListRecordModel(){
        listRecord = recordDAO.listRecord();
        return listRecord;
    }

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }
}
