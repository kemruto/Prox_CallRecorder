package com.sonnguyen.callrecorder.ui.fragment.Favourite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private List<RecordModel> mList;
    private Context mContext;
    private OnActionCallbackFragment callback;
    private RecordDAO recordDAO;

    public void setNewData(List<RecordModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public FavouriteAdapter(List<RecordModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setCallback(OnActionCallbackFragment callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.ViewHolder holder, int position) {
        RecordModel recordModel = mList.get(position);
        recordDAO = RecordDatabase.getInstance(mContext).recordDAO();

        String name = recordModel.getPhoneContact();
        String phone = recordModel.getPhoneNumber();
        if (name.equals("")){
            holder.tvNameContact.setText(phone);
        }else{
            holder.tvNameContact.setText(name);
        }

        if (recordModel.getStatus()==1){
            holder.imvStatusCall.setBackgroundResource(R.drawable.ic_incoming_call);
        }else {
            holder.imvStatusCall.setBackgroundResource(R.drawable.ic_outgoing_call);
        }
        holder.tvDate.setText(recordModel.getDate());
        if (recordModel.getFavourite()==1){
            holder.imvFavourite.setBackgroundResource(R.drawable.ic_purple_star);
        }

        holder.imvFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete record from favourite
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
        TextView tvNameContact;
        TextView tvDate;
        ImageView imvStatusCall;
        ImageView imvFavourite;
        ImageView imvPlay;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameContact = itemView.findViewById(R.id.tv_name_contact);
            tvDate = itemView.findViewById(R.id.tv_date);
            imvStatusCall = itemView.findViewById(R.id.lv_status_call);
            imvFavourite = itemView.findViewById(R.id.lv_status_star);
            imvPlay = itemView.findViewById(R.id.imv_play);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
