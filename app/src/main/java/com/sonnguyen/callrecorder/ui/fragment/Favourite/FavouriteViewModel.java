package com.sonnguyen.callrecorder.ui.fragment.Favourite;

import android.content.Context;

import com.sonnguyen.callrecorder.base.BaseViewModel;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteViewModel extends BaseViewModel {
    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;
    private Context context;

    public FavouriteViewModel(){listRecord = new ArrayList<>();}

    public List<RecordModel> getListFavourRecord(){
        listRecord = recordDAO.listFavourite();
        return listRecord;
    }

    public void setContext(Context context) {
        this.context = context;
        recordDAO = RecordDatabase.getInstance(context).recordDAO();
    }
}
