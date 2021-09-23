package com.sonnguyen.callrecorder.ui.fragment.Caller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;
import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.CallerModel;

import java.util.List;

public class CallerAdapter extends RecyclerView.Adapter<CallerAdapter.ViewHolder> {
    private List<CallerModel> mList;
    private Context mContext;
    private RecordDAO recordDAO;
    private OnActionCallbackFragment callbackFragment;

    public CallerAdapter(List<CallerModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setNewData(List<CallerModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setCallback(OnActionCallbackFragment callbackFragment) {
        this.callbackFragment = callbackFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_caller, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallerAdapter.ViewHolder holder, int position) {
        CallerModel callerModel = mList.get(position);
        recordDAO = RecordDatabase.getInstance(mContext).recordDAO();
        holder.tvPhoneNumber.setText(callerModel.getPhoneNumber());
        holder.tvDate.setText(callerModel.getCreateAt());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPhoneNumber;
        private TextView tvDate;
        private Button btTrack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneNumber = itemView.findViewById(R.id.tv_caller_phonenumber);
            btTrack = itemView.findViewById(R.id.bt_caller_track);
            tvDate = itemView.findViewById(R.id.tv_caller_date);
        }
    }
}
