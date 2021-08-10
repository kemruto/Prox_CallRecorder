package com.sonnguyen.callrecorder;

import com.sonnguyen.callrecorder.datasource.model.RecordModel;

public interface OnActionCallback {
    void onCallbackDB(String key, RecordModel model);
}
