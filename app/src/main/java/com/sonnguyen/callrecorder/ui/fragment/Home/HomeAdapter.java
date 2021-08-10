package com.sonnguyen.callrecorder.ui.fragment.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    public static final String KEY_RECORD_TO_DETAIL = "KEY_RECỎD_TO_DETAIL";
    private List<RecordModel> mList;
    private Context mContext;
    private OnActionCallbackFragment callback;
    private RecordDAO recordDAO;

    public HomeAdapter(List<RecordModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setNewData(List<RecordModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setCallback(OnActionCallbackFragment callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        RecordModel recordModel = mList.get(position);
        recordDAO = RecordDatabase.getInstance(mContext).recordDAO();
        holder.tvPhoneNumber.setText(recordModel.getPhoneNumber());
        holder.tvTime.setText(recordModel.getTime());

        if (recordModel.getStatus()==1){
            holder.imvStatusCall.setBackgroundResource(R.drawable.ic_incoming_call);
        }else {
            holder.imvStatusCall.setBackgroundResource(R.drawable.ic_outgoing_call);
        }

        if (recordModel.getFavourite()==0){
            holder.imvFavourite.setBackgroundResource(R.drawable.ic_gray_star);
        }else{
            holder.imvFavourite.setBackgroundResource(R.drawable.ic_purple_star);
        }

        holder.imvFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(recordModel.getFavourite()){
                    case 0:
                        holder.imvFavourite.setBackgroundResource(R.drawable.ic_purple_star);
                        recordModel.setFavourite(1);
                        recordDAO.updateRecord(recordModel);
                        break;
                    case 1:
                        holder.imvFavourite.setBackgroundResource(R.drawable.ic_gray_star);
                        recordModel.setFavourite(0);
                        recordDAO.updateRecord(recordModel);
                        break;
                }
            }
        });

        holder.imvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open detail fragment
            }
        });

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callback.onCallback(KEY_RECORD_TO_DETAIL,recordModel);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhoneNumber;
        TextView tvTime;
        ImageView imvStatusCall;
        ImageView imvFavourite;
        ImageView imvPlay;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneNumber = itemView.findViewById(R.id.tv_name_contact);
            tvTime = itemView.findViewById(R.id.tv_time);
            imvStatusCall = itemView.findViewById(R.id.lv_status_call);
            imvFavourite = itemView.findViewById(R.id.lv_status_star);
            imvPlay = itemView.findViewById(R.id.imv_play);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
