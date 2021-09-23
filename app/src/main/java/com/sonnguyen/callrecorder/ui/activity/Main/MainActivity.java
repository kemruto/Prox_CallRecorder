package com.sonnguyen.callrecorder.ui.activity.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sonnguyen.callrecorder.ui.activity.Search.SearchActivity;
import com.sonnguyen.callrecorder.ui.activity.Second.SecondActivity;
import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackService;
import com.sonnguyen.callrecorder.service.AutoRecordService;
import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.ui.ViewPagerAdapter;
import com.sonnguyen.callrecorder.base.BaseAct;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.CallerModel;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.fragment.Caller.CallerFragment;
import com.sonnguyen.callrecorder.ui.fragment.Detail.DetailFragment;
import com.sonnguyen.callrecorder.ui.fragment.Detail.Note.AddNoteFragment;
import com.sonnguyen.callrecorder.ui.fragment.Edit.EditFragment;
import com.sonnguyen.callrecorder.ui.fragment.Favourite.FavouriteFragment;
import com.sonnguyen.callrecorder.ui.fragment.Home.HomeFragment;
import com.sonnguyen.callrecorder.ui.fragment.IncomingCall.IncomingCallFragment;
import com.sonnguyen.callrecorder.ui.fragment.OutgoingCall.OutgoingCallFragment;
import com.sonnguyen.callrecorder.ui.fragment.Setting.SettingFragment;
import com.sonnguyen.callrecorder.ui.fragment.Trimmed.TrimmedFragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends BaseAct<MainActViewModel> implements OnActionCallbackFragment, OnActionCallbackService {

    public static final String KEYBUNDLE_HOMEFRAGMENT_DETAILFRAGMENT = "KEYBUNDEL_HOMEFRAGMENT_TO_DETAIL";
    public static final String KEYBUNDLE_DETAILFRAGMENT_ADDNOTE = "KEYBUNDLE_DETAILFRAGMENT_ADDNOTE";
    public static final String KEYBUNDLE_DETAILFRAGMENT_EDIT = "KEYBUNDLE_DETAILFRAGMENT_EDIT";
    public static final String KEYBUNDLE_MAIN_TO_SECOND = "KEYBUNDLE_MAIN_TO_SECOND";
    private static final int REQ = 1;
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 2;
    private static final String KEY_SHARE_PREFERENCES = "KEY_SHARE_PREFERENCES";
    private static final String KEY_STORAGE = "KEY_STORAGE";
    private static final String KEY_RECORD = "KEY_RECORD";
    private static final String KEY_CONTACT = "KEY_CONTACT";
    private static final String KEY_OVERLAY = "KEY_OVERLAY";
    private static final String TAG = "aaa";
    private static final int minute5 = 5;
    private static final int minute10 = 10;
    private static final int minute15 = 15;
    private static final int minute20 = 20;
    private static final int NO_LIMIT = 10000;

    private static final int delete15Days = 15;
    private static final int delete1Month = 30;
    private static final int delete3Month = 90;
    private static final int delete6Month = 180;
    private static final int NEVER_DELETE = 10000;

    private static MainActivity INSTANCE;
    private RecordDAO recordDAO;
    private boolean autoRecord = true;
    private int duration = NO_LIMIT * 1000;
    private int deleteDay = NEVER_DELETE;

    private ImageView imvSetting,imvSearch;
    private Button btSkip;
    private SwitchCompat switchStorage, switchRecord, switchContact, switchOverlay;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.READ_CONTACTS
            , Manifest.permission.READ_PHONE_STATE};
    private SharedPreferences sharedPreferences;

    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private Intent intent1;
    private DrawerLayout drawerLayout;
    private View imvMenu;
    private Dialog dialog;

    private HomeFragment homeFragment;
    private DetailFragment detailFragment;
    private CallerFragment callerFragment;
    private FavouriteFragment favouriteFragment;
    private IncomingCallFragment incomingCallFragment;
    private OutgoingCallFragment outgoingCallFragment;
    private TrimmedFragment trimmedFragment;
    private AddNoteFragment addNoteFragment;
    private EditFragment editFragment;

    public static MainActivity getMainActivityInstance() {
        return INSTANCE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
    }

    @Override
    protected void initViews() {
        recordDAO = RecordDatabase.getInstance(this).recordDAO();
        sharedPreferences = getApplicationContext().getSharedPreferences(KEY_SHARE_PREFERENCES, Context.MODE_PRIVATE);

        checkPermission();
        if (!checkPermission()) {
            showPermissionDialog();
        }
        intent1 = new Intent(this, AutoRecordService.class);
        intent1.putExtra("inputExtra", "Screen rights");

        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        drawerLayout = findViewById(R.id.drawer_layout);
        imvMenu = findViewById(R.id.imv_menu);

        homeFragment = new HomeFragment();
        callerFragment = new CallerFragment();
        favouriteFragment = new FavouriteFragment();
        incomingCallFragment = new IncomingCallFragment();
        outgoingCallFragment = new OutgoingCallFragment();
        trimmedFragment = new TrimmedFragment();
        editFragment = new EditFragment();

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(homeFragment, "All");
        pagerAdapter.addFragment(callerFragment, "Caller ID");
        pagerAdapter.addFragment(favouriteFragment, "Favourite");
        pagerAdapter.addFragment(incomingCallFragment, "Incoming Call");
        pagerAdapter.addFragment(outgoingCallFragment, "Outgoing Call");
        pagerAdapter.addFragment(trimmedFragment, "Trimmed");

        homeFragment.setCallBack(this);
        callerFragment.setCallBack(this);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        imvSetting = findViewById(R.id.imv_setting);
        imvSearch = findViewById(R.id.imv_search);
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
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_permission_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        switchStorage = dialog.findViewById(R.id.storage_switch);
        switchRecord = dialog.findViewById(R.id.record_switch);
        switchContact = dialog.findViewById(R.id.contact_switch);
        switchOverlay = dialog.findViewById(R.id.overlay_switch);
        btSkip = dialog.findViewById(R.id.bt_skip);
        Button btGrant = dialog.findViewById(R.id.bt_grant);

        btSkip.setOnClickListener(v -> dialog.dismiss());

        btGrant.setOnClickListener(v -> {
            requestPermissions(permissions, REQ);
            if (checkPermission()) {
                switchStorage.setChecked(true);
                switchRecord.setChecked(true);
                switchOverlay.setChecked(true);
                switchContact.setChecked(true);
            }
        });

        boolean checkStorage = sharedPreferences.getBoolean(KEY_STORAGE, false);
        boolean checkRecord = sharedPreferences.getBoolean(KEY_RECORD, false);
        boolean checkContact = sharedPreferences.getBoolean(KEY_CONTACT, false);
        boolean checkOverlay = sharedPreferences.getBoolean(KEY_OVERLAY, false);

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

        switchStorage.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
                    switchStorage.setEnabled(false);
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

        switchOverlay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
            } else if (Settings.canDrawOverlays(this)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_OVERLAY, true);
                editor.commit();
                switchOverlay.setEnabled(false);
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ:
                switchStorage.setChecked(true);
                switchContact.setChecked(true);
                switchRecord.setChecked(true);
            case OVERLAY_PERMISSION_REQUEST_CODE:
                switchOverlay.setChecked(true);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                btSkip.setBackgroundResource(R.drawable.bg_round_7_purple);
                btSkip.setEnabled(true);
        }
    }

    @Override
    protected void initEvents() {
        imvMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        imvSetting.setOnClickListener(v -> {
            SettingFragment settingFragment = new SettingFragment();
            settingFragment.setCallBack(this);
            settingFragment.setCallBackService(this);
            showFragment(R.id.frame_layout, settingFragment, true);
        });

        imvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCallback(String key, Object object) {
        switch (key) {
            case HomeFragment.KEY_HOME_TO_MAIN_TO_DETAIL:
                detailFragment = new DetailFragment();
                detailFragment.setCallBack(this);
                RecordModel recordModel = (RecordModel) object;
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEYBUNDLE_HOMEFRAGMENT_DETAILFRAGMENT, recordModel);
                detailFragment.setArguments(bundle);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                showFragment(R.id.frame_layout, detailFragment, true);
                break;
            case DetailFragment.KEY_DETAIL_TO_ADD_NOTE:
                addNoteFragment = new AddNoteFragment();
                addNoteFragment.setCallBack(this);
                RecordModel recordModel1 = (RecordModel) object;
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(KEYBUNDLE_DETAILFRAGMENT_ADDNOTE, recordModel1);
                addNoteFragment.setArguments(bundle1);
                showFragment(R.id.frame_layout, addNoteFragment, true);
                break;
            case DetailFragment.KEY_DETAIL_TO_TRIM:
                editFragment = new EditFragment();
                editFragment.setCallBack(this);
                RecordModel recordModel2 = (RecordModel) object;
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(KEYBUNDLE_DETAILFRAGMENT_EDIT, recordModel2);
                editFragment.setArguments(bundle2);
                showFragment(R.id.frame_layout, editFragment, true);
                break;
            case CallerFragment.KEY_CALLER_TO_TRACKING_ACT:
                String phoneNumber = (String) object;
                String contact = getContactNameByNumber(phoneNumber);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.now();
                Log.i(TAG, "dateTime" + dtf.format(dateTime));
                CallerModel callerModel = new CallerModel(contact, phoneNumber, dtf.format(dateTime),"");
                recordDAO.insertCaller(callerModel);

                Bundle bundle3 = new Bundle();
                bundle3.putSerializable(KEYBUNDLE_MAIN_TO_SECOND, callerModel);
                Intent intent = new Intent(this, SecondActivity.class);
                intent.putExtras(bundle3);
                startActivity(intent);
                break;
            case SettingFragment.KEY_AUTO_DELETE:
                int i = (int) object;
                switch (i) {
                    case 0:
                        deleteDay = delete15Days;
                        break;
                    case 1:
                        deleteDay = delete1Month;
                        break;
                    case 2:
                        deleteDay = delete3Month;
                        break;
                    case 3:
                        deleteDay = delete6Month;
                        break;
                    case 4:
                        deleteDay = NEVER_DELETE;
                        break;
                }
                break;
        }
    }

    @Override
    public void onCallbackService(String key, Object object) {
        switch (key) {
            case SettingFragment.KEY_SETTING_TO_MAIN_TO_SERVICE:
                autoRecord = (boolean) object;
                break;
            case SettingFragment.KEY_TIME_LIMIT:
                int i = (int) object;
                switch (i) {
                    case 0:
                        duration = minute5 * 1000;
                        break;
                    case 1:
                        duration = minute10 * 1000;
                        break;
                    case 2:
                        duration = minute15 * 1000;
                        break;
                    case 3:
                        duration = minute20 * 1000;
                        break;
                    case 4:
                        duration = NO_LIMIT * 1000;
                        break;
                }
        }
    }

    public String getContactNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = "Unknown";

        ContentResolver contentResolver = this.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[]{BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }
        return name;
    }

    public boolean getAutoRecord() {
        return this.autoRecord;
    }

    public int getDurationRecord() {
        return this.duration;
    }

    public int getAutoDeleteDay() {
        return this.deleteDay;
    }

}

