package com.sonnguyen.callrecorder.ui.fragment.Detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sonnguyen.callrecorder.MessageEvent;
import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.base.BaseFragment;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.ui.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Long.getLong;

public class DetailFragment extends BaseFragment<DetailViewModel> {

    public static final String KEY_DETAIL_TO_ADD_NOTE = "KEY_DETAIL_TO_ADD_NOTE";
    public static final String KEY_DELETE_SUCCESS = "KEY_DELETE_SUCCESS";
    public static final String KEY_DETAIL_TO_TRIM = "KEY_DETAIL_TO_TRIM";
    private static final String TAG = "aaa";
    private OnActionCallbackFragment callbackFragment;
    private ImageView imvBack, imvShare, imvBin, imvAddNote, imvTrim, imvPlay, imvStatusCall, imvTrimmed;
    private TextView tvPhoneNumber, tvDate,tvStartTime,tvEndTime;
    private RecordModel recordModel;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int state;
    public static int PLAYING = 0;
    public static int PAUSE = 1;
    public static int STOP = 2;
    private Thread thTask;
    private SeekBar seekBar;

    @Override
    protected Class getClassModel() {
        return DetailViewModel.class;
    }

    @Override
    protected void initEvents() {
        imvBack.setOnClickListener(v -> {
            sendMessageToHome();
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
            callbackFragment.onCallback(KEY_DETAIL_TO_ADD_NOTE,recordModel);
        });

        imvTrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackFragment.onCallback(KEY_DETAIL_TO_TRIM,recordModel);
            }
        });

        imvBin.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete record");
            builder.setMessage("Do you want to delete this record");
            builder.setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.deleteRecord(recordModel);
                    dialog.dismiss();
                }
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
        tvPhoneNumber = findViewById(R.id.tv_caller_contact);
        tvDate = findViewById(R.id.tv_track_caller_date);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        seekBar = findViewById(R.id.seekbar);

        getFirstValue();
        startBGTask();
    }

    private void getFirstValue() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            recordModel = (RecordModel) bundle.getSerializable(MainActivity.KEYBUNDLE_HOMEFRAGMENT_DETAILFRAGMENT);
        }

        // thiết lập các giá trị title , content , time , imvPin cho userModel
        tvPhoneNumber.setText(recordModel.getPhoneNumber());
        tvDate.setText(recordModel.getDate());
        if (recordModel.getStatus()==0){
            imvStatusCall.setBackgroundResource(R.drawable.ic_outgoing_call);
        }else if (recordModel.getStatus()==1){
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
        thTask = new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(500);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String currentTime = getCurrentTime();
                                String totalTime = getTotalTime();
                                int currentDuration = getCurrentDuration();
                                int totalDuration = getTotalDuration();

                                tvStartTime.setText(currentTime);
                                tvEndTime.setText(totalTime);
                                seekBar.setMax(totalDuration);
                                seekBar.setProgress(currentDuration);
                            }
                        });
                    }
                    catch (Exception e){
                        e.getMessage();
                        return;
                    }
                }
            }
        };
        thTask.start();
    }

    private void play() {
        Log.i(TAG, "play: "+recordModel.getPath());
        try {
            if (state==STOP){
                mediaPlayer.reset();
                mediaPlayer.setDataSource(recordModel.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                state = PLAYING;
            }else if(state==PLAYING){
                mediaPlayer.pause();
                state = PAUSE;
            }else if (state==PAUSE){
                mediaPlayer.start();
                state = PLAYING;
            }
            if (state==PLAYING){
                imvPlay.setImageLevel(0);
            }else {
                imvPlay.setImageLevel(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentTime() {
        String time = "00:00";
        try {
            int duration = mediaPlayer.getCurrentPosition();
            SimpleDateFormat df = new SimpleDateFormat("mm:ss");
            time = df.format(new Date(duration));
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
            time =df.format(new Date(duration));

        }catch (Exception e){
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
        }catch (Exception ignored){
        }
        return 100;
    }

    public void seekTo(int progress) {
        try{
            mediaPlayer.seekTo(progress);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setCallBack(OnActionCallbackFragment callbackFragment) {
        this.callbackFragment = callbackFragment;
    }

}

