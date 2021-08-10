package com.sonnguyen.callrecorder.ui.fragment.OutgoingCall;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class OutgoingCallViewModel extends BaseViewModel {
    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;
    private Context context;

    public OutgoingCallViewModel(){listRecord = new ArrayList<>();}

    public List<RecordModel> getListOutgoingRecord(){
        listRecord = recordDAO.listOutgoingCall();
        return listRecord;
    }

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }
}
