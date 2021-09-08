package com.sonnguyen.callrecorder.ui.fragment.Detail.Note;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.activity.MainActivity;

public class AddNoteFragment extends BaseFragment<AddNoteViewModel> {
    private OnActionCallbackFragment callbackFragment;
    private ImageView imvBack,imvCheck;
    private EditText edtNote;
    private RecordModel recordModel;
    private String note;
    @Override
    protected Class<AddNoteViewModel> getClassModel() {
        return AddNoteViewModel.class;
    }

    @Override
    protected void initEvents() {
        imvBack.setOnClickListener(v -> getFragmentManager().popBackStack());
        imvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note!=""){
                    recordModel.setNote(note);
                    mViewModel.updateRecord(recordModel);
                }
                getFragmentManager().popBackStack();
            }
        });

        edtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note = edtNote.getText().toString();
            }
        });

    }

    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        imvBack = findViewById(R.id.imv_back_note);
        imvCheck = findViewById(R.id.imv_check);
        edtNote = findViewById(R.id.edt_add_note);
        Bundle bundle = getArguments();
        if (bundle!=null){
            recordModel = (RecordModel) bundle.getSerializable(MainActivity.KEYBUNDLE_DETAILFRAGMENT_ADDNOTE);
        }

        edtNote.setText(recordModel.getNote());
    }

    public void setCallBack(OnActionCallbackFragment callbackFragment){
        this.callbackFragment = callbackFragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_note;
    }

    @Override
    public void onClick(View v) {

    }
}
