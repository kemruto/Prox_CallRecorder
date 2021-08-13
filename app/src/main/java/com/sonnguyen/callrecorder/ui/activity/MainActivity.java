package com.sonnguyen.callrecorder.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sonnguyen.callrecorder.AutoRecordService;
import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.adapter.ViewPagerAdapter;
import com.sonnguyen.callrecorder.base.BaseAct;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.fragment.Caller.CallFragment;
import com.sonnguyen.callrecorder.ui.fragment.Detail.DetailFragment;
import com.sonnguyen.callrecorder.ui.fragment.Favourite.FavouriteFragment;
import com.sonnguyen.callrecorder.ui.fragment.Home.HomeAdapter;
import com.sonnguyen.callrecorder.ui.fragment.Home.HomeFragment;
import com.sonnguyen.callrecorder.ui.fragment.IncomingCall.IncomingCallFragment;
import com.sonnguyen.callrecorder.ui.fragment.OutgoingCall.OutgoingCallFragment;
import com.sonnguyen.callrecorder.ui.fragment.Trimmed.TrimmedFragment;

public class MainActivity extends BaseAct<MainActViewModel> implements OnActionCallbackFragment {

    private static final int REQ = 1;
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 2;
    private static final String KEY_SHARE_PREFERENCES = "KEY_SHARE_PREFERENCES";
    private static final String KEY_STORAGE = "KEY_STORAGE";
    private static final String KEY_RECORD = "KEY_RECORD";
    private static final String KEY_CONTACT = "KEY_CONTACT";
    private static final String KEY_OVERLAY = "KEY_OVERLAY";

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.READ_CONTACTS
            , Manifest.permission.READ_PHONE_STATE};
    private SharedPreferences sharedPreferences;

    private ViewPager viewPager;
    private Intent intent1;
    private DrawerLayout drawerLayout;
    private View imvMenu;

    private HomeAdapter homeAdapter;
    private HomeFragment homeFragment;
    private DetailFragment detailFragment;
    private CallFragment callFragment;
    private FavouriteFragment favouriteFragment;
    private IncomingCallFragment incomingCallFragment;
    private OutgoingCallFragment outgoingCallFragment;
    private TrimmedFragment trimmedFragment;

    @Override
    protected void initViews() {
        sharedPreferences = getApplicationContext().getSharedPreferences(KEY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        checkPermission();
        if (!checkPermission()) {
            showPermissionDialog();
        }
        intent1 = new Intent(this, AutoRecordService.class);
        intent1.putExtra("inputExtra", "Screen rights");

        drawerLayout = findViewById(R.id.drawer_layout);
        imvMenu = findViewById(R.id.imv_menu);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private boolean checkPermission() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED
                    || !Settings.canDrawOverlays(this)) {
                return false;
            }
        }
        return true;
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_permission_dialog);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        SwitchCompat switchStorage = dialog.findViewById(R.id.storage_switch);
        SwitchCompat switchRecord = dialog.findViewById(R.id.record_switch);
        SwitchCompat switchContact = dialog.findViewById(R.id.contact_switch);
        SwitchCompat switchOverlay = dialog.findViewById(R.id.overlay_switch);
        SwitchCompat switchAccessibility = dialog.findViewById(R.id.accessibility_switch);
        Button btSkip = dialog.findViewById(R.id.bt_skip);
        Button btGrant = dialog.findViewById(R.id.bt_grant);

        if (checkPermission()) {
            btSkip.setEnabled(true);
            btSkip.setBackgroundResource(R.drawable.bg_round_7_purple);
        } else {
            btSkip.setEnabled(false);
        }

        btSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(permissions, REQ);
                if (checkPermission()) {
                    switchStorage.setChecked(true);
                    switchRecord.setChecked(true);
                    switchOverlay.setChecked(true);
                    switchContact.setChecked(true);
                    switchAccessibility.setChecked(true);
                }
            }
        });

        boolean checkStorage = sharedPreferences.getBoolean(KEY_STORAGE, false);
        boolean checkRecord = sharedPreferences.getBoolean(KEY_RECORD, false);
        boolean checkContact = sharedPreferences.getBoolean(KEY_CONTACT, false);
        boolean checkOverlay = sharedPreferences.getBoolean(KEY_OVERLAY, false);
//        boolean checkAccessibility = sharedPreferences.getBoolean(KEY_STORAGE,false);
        if (checkStorage) {
            switchStorage.setChecked(checkStorage);
            switchStorage.setEnabled(false);
        }
        if (checkRecord) {
            switchRecord.setChecked(checkRecord);
            switchRecord.setEnabled(false);
        }
        if (checkContact) {
            switchContact.setChecked(checkContact);
            switchContact.setEnabled(false);
        }
        if (checkOverlay) {
            switchOverlay.setChecked(checkOverlay);
            switchOverlay.setEnabled(false);
        }

        switchStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchStorage.isChecked()) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQ);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        switchStorage.setChecked(false);
                    } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_STORAGE, true);
                        editor.commit();
                        switchStorage.setChecked(true);
                        switchStorage.setEnabled(false);
                    }
                }
            }
        });

        switchRecord.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE}, REQ);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_DENIED) {
                switchRecord.setChecked(false);
            } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_RECORD, true);
                editor.commit();
                switchRecord.setEnabled(false);
            }
        });

        switchContact.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQ);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {
                switchContact.setChecked(false);
            } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_CONTACT, true);
                editor.commit();
                switchContact.setEnabled(false);
            }
        });

        switchOverlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
                } else if (Settings.canDrawOverlays(MainActivity.this)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(KEY_OVERLAY, true);
                    editor.commit();
                    switchOverlay.setEnabled(false);
                }
            }
        });

//        switchAccessibility.setOnClickListener(v -> {
//            if (Settings.ACTION_ACCESSIBILITY_SETTINGS ){
////                switchOverlay.setChecked(false);
//                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS, Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
//                switchAccessibility.setChecked(false);
//            }else{
//                switchAccessibility.setChecked(true);
//                switchAccessibility.setEnabled(false);
//                switchAccessibility.setSaveEnabled(false);
//            }
//        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ:

            case OVERLAY_PERMISSION_REQUEST_CODE:
        }
    }

    @Override
    protected void initEvents() {
        imvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected Class<MainActViewModel> getClassViewModel() {
        return MainActViewModel.class;
    }

    @Override
    public void onCallback(String key, Object object) {
        switch (key) {
            case HomeFragment.KEY_HOME_TO_MAIN_TO_DETAIL:
                detailFragment = new DetailFragment();
                detailFragment.setCallBack(this);
                showFragment(R.id.frame_layout,detailFragment,true);
                break;
        }
    }


}