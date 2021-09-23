package com.sonnguyen.callrecorder.ui.activity.Search;

import android.app.SearchManager;
import android.content.Context;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseAct;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseAct<SearchViewModel> {
    private SearchView searchView;
    private ImageView imvBack;
    private SearchAdapter searchAdapter;
    private List<RecordModel> listRecord;
    private RecordDAO recordDAO;
    private RecyclerView recyclerView;

    @Override
    protected void initViews() {
        mModel.setContext(this);
        recordDAO = RecordDatabase.getInstance(this).recordDAO();

        searchView = (SearchView) findViewById(R.id.edt_search);
        imvBack = findViewById(R.id.imv_back_search);
        recyclerView = findViewById(R.id.list_item);
        listRecord = new ArrayList<>();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });

        showListRecord();
    }

    private void showListRecord() {
        listRecord = mModel.getListRecordModel();
        if (listRecord.size() == 0){

        }else{
            searchAdapter = new SearchAdapter(listRecord,SearchActivity.this);
            searchAdapter.setNewData(listRecord);
            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
            recyclerView.setAdapter(searchAdapter);
        }
    }

    @Override
    protected void initEvents() {
        imvBack.setOnClickListener(v -> finish());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected Class<SearchViewModel> getClassViewModel() {
        return SearchViewModel.class;
    }
}
