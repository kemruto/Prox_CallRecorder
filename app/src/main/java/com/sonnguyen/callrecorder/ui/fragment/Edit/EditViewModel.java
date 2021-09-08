package com.sonnguyen.callrecorder.ui.fragment.Edit;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

public class EditViewModel extends BaseViewModel {
    private Context context;
    private RecordDAO recordDAO;

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }

    public void insertRecord(RecordModel recordModel){recordDAO.insertRecord(recordModel);}

    public void deleteRecord(RecordModel recordModel){
        recordDAO.deleteRecord(recordModel);
    }

    public void updateRecord(RecordModel recordModel){
        recordDAO.updateRecord(recordModel);
    }

}
