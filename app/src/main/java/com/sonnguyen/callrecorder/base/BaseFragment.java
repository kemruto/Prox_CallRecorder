package com.sonnguyen.callrecorder.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseFragment<T extends BaseViewModel> extends Fragment implements View.OnClickListener {
    protected View mView;
    protected T mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutResId(),container,false);
        mViewModel = new ViewModelProvider(this).get(getClassModel());
        return mView;
    }

    protected abstract Class<T> getClassModel();

    protected abstract void initEvents();

    protected abstract void initViews();

    protected abstract int getLayoutResId();

    public final <T extends View> T findViewById(int id) { return findViewById(id,null);}
    public final <T extends View> T findViewById(int id,View.OnClickListener event){
        T v = mView.findViewById(id);
        if (v!=null&&event!=null){
            v.setOnClickListener(event);
        }
        return v;
    }
}
