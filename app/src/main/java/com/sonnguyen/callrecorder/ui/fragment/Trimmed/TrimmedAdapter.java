package com.sonnguyen.callrecorder.ui.fragment.Trimmed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.List;

public class TrimmedAdapter extends RecyclerView.Adapter<TrimmedAdapter.ViewHolder> {
    private RecordDAO recordDAO;
    private List<RecordModel> mList;
    private Context mContext;

    public void setNewData(List<RecordModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public TrimmedAdapter(List<RecordModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        recordDAO = RecordDatabase.getInstance(mContext).recordDAO();
        RecordModel recordModel = mList.get(position);

        holder.tvPhoneNumber.setText(recordModel.getPhoneNumber());
        holder.tvDate.setText(recordModel.getDate());
        holder.imvStatusCall.setBackgroundResource(R.drawable.ic_trimmed);

        if (recordModel.getFavourite()==1){
            holder.imvFavourite.setBackgroundResource(R.drawable.ic_purple_star);
        }else{
            holder.imvFavourite.setBackgroundResource(R.drawable.ic_gray_star);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhoneNumber;
        TextView tvDate;
        ImageView imvStatusCall;
        ImageView imvFavourite;
        ImageView imvPlay;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneNumber = itemView.findViewById(R.id.tv_name_contact);
            tvDate = itemView.findViewById(R.id.tv_date);
            imvStatusCall = itemView.findViewById(R.id.lv_status_call);
            imvFavourite = itemView.findViewById(R.id.lv_status_star);
            imvPlay = itemView.findViewById(R.id.imv_play);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }
}
