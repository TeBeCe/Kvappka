package com.example.martin.login;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Zuzka on 07.04.2018.
 */

public class DonationsListAdapter extends RecyclerView.Adapter<DonationsListAdapter.MyViewHolder> {
    private Context context;
    private List<Calendar> calendarList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView name;

        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);

            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public DonationsListAdapter(Context context, List<Calendar> calendarList) {
        this.context = context;
        this.calendarList = calendarList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donations_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Calendar item = calendarList.get(position);
        holder.name.setText(item.get(Calendar.DAY_OF_MONTH)+"/"+item.get(Calendar.MONTH)+"/"+item.get(Calendar.YEAR));
    }

    @Override
    public int getItemCount() {
        return calendarList.size();
    }

    public  void addItem(Calendar calendar ) {
        calendarList.add(calendar);
    }

    public void removeItem(int position) {
        calendarList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Calendar item, int position) {
        calendarList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
