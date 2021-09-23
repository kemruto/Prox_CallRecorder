package com.sonnguyen.callrecorder.ui.fragment.TrackCaller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.model.CallerModel;
import com.sonnguyen.callrecorder.ui.activity.Second.SecondActivity;

public class TrackCallerFragment extends BaseFragment<TrackCallerViewModel> {
    private static final String TAG = "aaa";
    private OnActionCallbackFragment callbackFragment;
    private ImageView imvBack,imvCall,imvMessage,imvInfo,imvSetting;
    private TextView tvContact,tvPhoneNumber;
    private String phoneNumber,contact;
    private CallerModel callerModel;
    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        imvBack = findViewById(R.id.imv_back_caller_detail);
        imvCall = findViewById(R.id.imv_track_caller_call);
        imvMessage = findViewById(R.id.imv_track_caller_message);
        imvSetting = findViewById(R.id.imv_track_caller_setting);
        tvContact = findViewById(R.id.tv_track_caller_contact);
        tvPhoneNumber = findViewById(R.id.tv_track_caller_phone_number);
        imvInfo = findViewById(R.id.imv_track_caller_info);

        Bundle bundle = getArguments();
        if (bundle!=null){
            callerModel = (CallerModel) bundle.getSerializable(SecondActivity.KEYBUNDLE_SECOND_TO_TRACK_CALLER);
            phoneNumber = callerModel.getPhoneNumber();
            contact = callerModel.getNameContact();
            tvContact.setText(contact);
            tvPhoneNumber.setText(phoneNumber);
        }
    }

    @Override
    protected void initEvents() {
        imvBack.setOnClickListener(v -> getActivity().finish());
        imvCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phoneNumber));
            startActivity(callIntent);
        });

        imvMessage.setOnClickListener(v -> {
            Intent messageIntent = new Intent(Intent.ACTION_VIEW);
            messageIntent.setData(Uri.parse("sms:"+phoneNumber));
            startActivity(messageIntent);
        });

        imvInfo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
            startActivity(intent);
        });
    }

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_track_caller;
    }

    @Override
    protected Class<TrackCallerViewModel> getClassModel() {
        return TrackCallerViewModel.class;
    }

    @Override
    public void onClick(View v) {

    }


}
