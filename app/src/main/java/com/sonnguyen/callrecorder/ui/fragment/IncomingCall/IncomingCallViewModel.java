package com.sonnguyen.callrecorder.ui.fragment.IncomingCall;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class IncomingCallViewModel extends BaseViewModel {
    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;
    private Context context;

    public IncomingCallViewModel(){listRecord = new ArrayList<>();}

    public List<RecordModel> getListIncomingRecord(){
        listRecord = recordDAO.listIncomingCall();
        return listRecord;
    }

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }
}
