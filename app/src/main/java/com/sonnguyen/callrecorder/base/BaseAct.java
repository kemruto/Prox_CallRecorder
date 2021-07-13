package com.sonnguyen.callrecorder.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseAct<T extends ViewModel> extends AppCompatActivity implements View.OnClickListener {
    protected T mModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mModel = new ViewModelProvider(this).get(getClassViewModel());
        initViews();
    }

    protected abstract void initViews();

    protected abstract int getLayoutResId();

    protected abstract Class<T> getClassViewModel();

    public <T extends View> T findViewById(int id){ return  super.findViewById(id);}

    @Override
    public void onClick(View v) {

    }

    protected void showFragment(int layoutId, Fragment fragment,boolean addToBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutId,fragment);
        if (addToBackStack){
            transaction.addToBackStack("add");
        }
        transaction.commit();
    }
}
