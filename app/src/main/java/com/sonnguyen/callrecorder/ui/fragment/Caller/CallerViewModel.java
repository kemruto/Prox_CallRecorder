package com.sonnguyen.callrecorder.ui.fragment.Caller;

import android.content.Context;
import android.telecom.Call;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.CallerModel;

import java.util.ArrayList;
import java.util.List;

public class CallerViewModel extends BaseViewModel {
    private RecordDAO recordDAO;
    private Context context;
    private List<CallerModel> listCaller;

    public CallerViewModel() {
        listCaller = new ArrayList<>();
    }

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }

    public List<CallerModel> getListCaller (){
        listCaller = recordDAO.listCaller();
        return listCaller;
    }

    public void deleteCaller(List<CallerModel> listCaller){
        recordDAO.deleteCaller(listCaller);
    }
}
