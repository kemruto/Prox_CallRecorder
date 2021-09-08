package com.sonnguyen.callrecorder.ui.fragment.TrackCaller;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.CallerModel;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class TrackCallerViewModel extends BaseViewModel {
    private RecordDAO recordDAO;
    private Context context;
    private List<CallerModel> listCaller;

    public TrackCallerViewModel() {
        listCaller = new ArrayList<>();
    }

    public void setContext(Context context){
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }

    public List<CallerModel> getListRecordModel(){
        listCaller = recordDAO.listCaller();
        return listCaller;
    }

    public void insertRecord(RecordModel recordModel){ recordDAO.insertRecord(recordModel); }
}
