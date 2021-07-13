package com.sonnguyen.callrecorder.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {
    protected final MutableLiveData<String> error = new MutableLiveData<>(null);

    public MutableLiveData<String> getError() {
        return error;
    }
}
