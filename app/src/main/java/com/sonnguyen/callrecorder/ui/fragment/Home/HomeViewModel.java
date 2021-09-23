package com.sonnguyen.callrecorder.ui.fragment.Home;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends BaseViewModel {
    private List<RecordModel> listRecord;
    private List<RecordModel> listSearch;
    private RecordDAO recordDAO;
    private Context context;
    private RecordModel recordModel;

    public HomeViewModel(){
        listRecord = new ArrayList<>();
        listSearch = new ArrayList<>();
    }

    public List<RecordModel> getListRecordModel(){
        listRecord = recordDAO.listRecord();
        return listRecord;
    }

    public void insertRecord(RecordModel recordModel){ recordDAO.insertRecord(recordModel); }
    public void deleteRecord(RecordModel recordModel){ recordDAO.deleteRecord(recordModel); }
    public void updateRecord(RecordModel recordModel){ recordDAO.updateRecord(recordModel); }
    public List<RecordModel> searchRecord(String phoneNumber){
        listSearch = recordDAO.searchPhone(phoneNumber);
        return listSearch;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void autoDelete(int compareDay){
        int i = listRecord.size();
        LocalDate today = LocalDate.now();
        LocalDate checkDate = today.minusDays(compareDay);
        for (int j = 0; j < i; j++) {
            recordModel = listRecord.get(j);
            String day = recordModel.getCreateAt();
            if (day.equals(String.valueOf(checkDate))){
                recordDAO.deleteRecord(recordModel);
            }
        }
    }

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }
}
