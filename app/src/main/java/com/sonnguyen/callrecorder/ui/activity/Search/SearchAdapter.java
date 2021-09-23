package com.sonnguyen.callrecorder.ui.activity.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sonnguyen.callrecorder.R;
import com.sonnguyen.callrecorder.datasource.database.RecordDAO;
import com.sonnguyen.callrecorder.datasource.database.RecordDatabase;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;
import com.sonnguyen.callrecorder.utils.callback.OnActionCallbackFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    public static final String KEY_RECORD_TO_DETAIL = "KEY_RECORD_TO_DETAIL";
    private List<RecordModel> mList;
    private List<RecordModel> mListOld;
    private Context mContext;
    private RecordDAO recordDAO;
    private OnActionCallbackFragment callbackFragment;

    public SearchAdapter(List<RecordModel> mList, Context mContext) {
        this.mList = mList;
        this.mListOld = mList;
        this.mContext = mContext;
    }

    public void setNewData(List<RecordModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        RecordModel recordModel = mList.get(position);
        recordDAO = RecordDatabase.getInstance(mContext).recordDAO();
        String name = recordModel.getPhoneContact();
        String phone = recordModel.getPhoneNumber();
        if (name.equals("")){
            holder.tvNameContact.setText(phone);
        }else{
            holder.tvNameContact.setText(name);
        }
        holder.tvDate.setText(recordModel.getDate());

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

//        holder.imvPlay.setOnClickListener(v -> callbackFragment.onCallback(KEY_RECORD_TO_DETAIL,recordModel));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    mList = mListOld;
                }else{
                     List<RecordModel> list = new ArrayList<>();
                     for (RecordModel recordModel : mListOld){
                         if (recordModel.getPhoneNumber().contains(strSearch)
                                 || recordModel.getPhoneContact().toLowerCase().contains(strSearch.toLowerCase())){
                             list.add(recordModel);
                         }
                     }
                     mList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (List<RecordModel>) results.values;
                notifyDataSetChanged();
            }
        };
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
