package com.sonnguyen.callrecorder.ui.fragment.Detail;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sonnguyen.callrecorder.utils.app.MessageEvent;
import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.activity.Main.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Long.getLong;

public class DetailFragment extends BaseFragment<DetailViewModel> {

    public static final String KEY_DETAIL_TO_ADD_NOTE = "KEY_DETAIL_TO_ADD_NOTE";
    public static final String KEY_DETAIL_TO_TRIM = "KEY_DETAIL_TO_TRIM";
    private static final String TAG = "aaa";
    public static int PLAYING = 0;
    public static int PAUSE = 1;
    public static int STOP = 2;
    private MediaPlayer mediaPlayer;
    public Handler handler;
    private OnActionCallbackFragment callbackFragment;
    private ImageView imvBack, imvShare, imvBin, imvAddNote, imvTrim, imvPlay, imvStatusCall, imvTrimmed;
    private TextView tvNameContact, tvDate, tvStartTime, tvEndTime;
    private RecordModel recordModel;
    private int state;
    private SeekBar seekBar;
    private Runnable runnable;
    private Thread thread;

    @Override
    protected Class getClassModel() {
        return DetailViewModel.class;
    }

    @Override
    protected void initEvents() {
        imvBack.setOnClickListener(v -> {
            getFragmentManager().popBackStack();
        });

        imvPlay.setOnClickListener(v -> {
            play();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(seekBar.getProgress());
            }
        });

        imvAddNote.setOnClickListener(v -> {
            callbackFragment.onCallback(KEY_DETAIL_TO_ADD_NOTE, recordModel);
        });

        imvTrim.setOnClickListener(v -> callbackFragment.onCallback(KEY_DETAIL_TO_TRIM, recordModel));

        imvBin.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete record");
            builder.setMessage("Do you want to delete this record");
            builder.setCancelable(false);
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.setPositiveButton("Confirm", (dialog, which) -> {
                mViewModel.deleteRecord(recordModel);
                sendMessageToHome();
                dialog.dismiss();
                getFragmentManager().popBackStack();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void sendMessageToHome() {
        MessageEvent messageEvent = new MessageEvent("deleteSuccess");
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    protected void initViews() {
        mViewModel.setContext(getContext());
        imvStatusCall = findViewById(R.id.imv_detail_status_call);
        imvBack = findViewById(R.id.imv_back_detail);
        imvShare = findViewById(R.id.imv_share);
        imvBin = findViewById(R.id.imv_bin);
        imvAddNote = findViewById(R.id.imv_add_note);
        imvTrim = findViewById(R.id.imv_trimmed);
        imvPlay = findViewById(R.id.imv_play_detail);
        tvNameContact = findViewById(R.id.tv_track_caller_contact);
        tvDate = findViewById(R.id.tv_detail_date);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        seekBar = findViewById(R.id.seekbar);

        getFirstValue();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recordModel.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startBGTask();
    }

    private void getFirstValue() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            recordModel = (RecordModel) bundle.getSerializable(MainActivity.KEYBUNDLE_HOMEFRAGMENT_DETAILFRAGMENT);
        }

        String name = recordModel.getPhoneContact();
        String phone = recordModel.getPhoneNumber();
        if (name.equals("")) {
            tvNameContact.setText(phone);
        } else {
            tvNameContact.setText(name);
        }
        tvDate.setText(recordModel.getDate());
        if (recordModel.getStatus() == 0) {
            imvStatusCall.setBackgroundResource(R.drawable.ic_outgoing_call);
        } else if (recordModel.getStatus() == 1) {
            imvStatusCall.setBackgroundResource(R.drawable.ic_incoming_call);
        }
        state = STOP;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_call_detail;
    }

    @Override
    public void onClick(View v) {

    }

    private void startBGTask() {
        handler = new Handler();
        getActivity().runOnUiThread(runnable = new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    String currentTime = getCurrentTime();
                    String totalTime = getTotalTime();
                    int currentDuration = getCurrentDuration();
                    int totalDuration = getTotalDuration();
                    Log.i(TAG, "currentTime: " + currentTime);
                    Log.i(TAG, "totalTime: " + totalTime);
                    Log.i(TAG, "currentDuration: " + currentDuration);
                    Log.i(TAG, "totalDuration: " + totalDuration);

                    tvStartTime.setText(currentTime);
                    tvEndTime.setText(totalTime);
                    seekBar.setMax(totalDuration);
                    seekBar.setProgress(currentDuration);
                    handler.postDelayed(this,500);
                }
            }
        });
    }

    private void play() {
        try {
            if (state == STOP) {
                mediaPlayer.start();
                state = PLAYING;
            } else if (state == PLAYING) {
                mediaPlayer.pause();
                state = PAUSE;
            } else if (state == PAUSE) {
                mediaPlayer.start();
                state = PLAYING;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (state == PLAYING) {
            imvPlay.setImageLevel(1);
        } else {
            imvPlay.setImageLevel(0);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentTime() {
        String time = "00:00";
        try {
            int currentPosition = mediaPlayer.getCurrentPosition();
            SimpleDateFormat df = new SimpleDateFormat("mm:ss");
            time = df.format(new Date(currentPosition));
        } catch (Exception e) {
            e.getMessage();
        }
        return time;
    }

    public String getTotalTime() {
        String time = "--:--";
        try {
            int duration = mediaPlayer.getDuration();
            SimpleDateFormat df = new SimpleDateFormat("mm:ss");
            time = df.format(new Date(duration));
        } catch (Exception e) {
            e.getMessage();
        }
        return time;
    }

    public int getCurrentDuration() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception ignored) {
        }
        return 0;
    }

    public int getTotalDuration() {
        try {
            return mediaPlayer.getDuration();
        } catch (Exception ignored) {
        }
        return 100;
    }

    public void seekTo(int progress) {
        try {
            mediaPlayer.seekTo(progress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCallBack(OnActionCallbackFragment callbackFragment) {
        this.callbackFragment = callbackFragment;
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStopDetail: ");
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}

