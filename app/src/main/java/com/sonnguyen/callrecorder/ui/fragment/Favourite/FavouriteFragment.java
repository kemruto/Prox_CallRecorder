package com.sonnguyen.callrecorder.ui.fragment.Favourite;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends BaseFragment<FavouriteViewModel> implements OnActionCallbackFragment {
    public static final String KEY_FAVOURITE_FRAGMENT_TO_DETAIL = "KEY_FAVOURITE_FRAGMENT_TO_DETAIL";
    private OnActionCallbackFragment callbackFragment;
    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;

    @Override
    protected Class getClassModel() {
        return FavouriteViewModel.class;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        listRecord = new ArrayList<>();
        recordDAO = RecordDatabase.getInstance(getContext()).recordDAO();
        recyclerView = findViewById(R.id.list_item_favourite);
        showListFavourRecord();
    }

    @Override
    public void onResume() {
        super.onResume();
        showListFavourRecord();
    }

    private void showListFavourRecord() {
        listRecord = mViewModel.getListFavourRecord();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (listRecord.size()==0){
            Toast.makeText(getContext(),"Null",Toast.LENGTH_SHORT).show();
        }else{
            favouriteAdapter = new FavouriteAdapter(listRecord,getContext());
            favouriteAdapter.setNewData(listRecord);
            recyclerView.setAdapter(favouriteAdapter);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_favourite;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCallback(String key, Object object) {

    }

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }
}
