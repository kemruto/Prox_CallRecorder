package com.sonnguyen.callrecorder.ui.fragment.Detail;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class DetailViewModel extends BaseViewModel {
    private Context context;
    private RecordDAO recordDAO;

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }

    public void deleteRecord(RecordModel recordModel){
        recordDAO.deleteRecord(recordModel);
    }
}
