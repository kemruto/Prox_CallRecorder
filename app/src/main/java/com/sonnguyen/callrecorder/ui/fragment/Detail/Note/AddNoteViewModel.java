package com.sonnguyen.callrecorder.ui.fragment.Detail.Note;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class AddNoteViewModel extends BaseViewModel {
    private RecordDAO recordDAO;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }

    public void getRecord(){
        recordDAO.listRecord();
    }

    public void updateRecord(RecordModel recordModel){
        recordDAO.updateRecord(recordModel);
    }
}
