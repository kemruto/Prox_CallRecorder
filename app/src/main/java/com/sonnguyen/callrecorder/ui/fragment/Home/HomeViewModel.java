package com.sonnguyen.callrecorder.ui.fragment.Home;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends BaseViewModel {
    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;
    private Context context;

    public HomeViewModel(){listRecord = new ArrayList<>();}

    public List<RecordModel> getListRecordModel(){
        listRecord = recordDAO.listRecord();
        return listRecord;
    }

    public void insertRecord(RecordModel recordModel){ recordDAO.insertRecord(recordModel); }
    public void deleteRecord(RecordModel recordModel){ recordDAO.deleteRecord(recordModel); }
    public void updateRecord(RecordModel recordModel){ recordDAO.updateRecord(recordModel); }

//    public List<RecordModel> searchRecord(String phone){return recordDAO.searchPhone(phone);}

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }
}
