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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Zuzka on 07.04.2018.
 */

public class DonationsListAdapter extends RecyclerView.Adapter<DonationsListAdapter.MyViewHolder> {
    private Context context;
    private List<DonationsEntity> donationsEntitiesList;
    private SimpleDateFormat format1= new SimpleDateFormat("dd.MM.yyyy");

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView name,locationName;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            locationName = view.findViewById(R.id.locationName);

            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public DonationsListAdapter(Context context, List<DonationsEntity> donationsEntitiesList) {
        this.context = context;
        this.donationsEntitiesList = donationsEntitiesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donations_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DonationsEntity item = donationsEntitiesList.get(position);
        holder.name.setText(format1.format(item.getCalendar().getTime()));
        holder.locationName.setText(item.getPlace());
    }

    @Override
    public int getItemCount() {
        return donationsEntitiesList.size();
    }

    public  void addItem(DonationsEntity newItem ) {
        donationsEntitiesList.add(newItem);
        Collections.sort(donationsEntitiesList,Collections.reverseOrder());
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        donationsEntitiesList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);

    }

    public void restoreItem(DonationsEntity item, int position) {
        donationsEntitiesList.add(position, item);
        // notify item added by position

        notifyItemInserted(position);
    }
    public List<DonationsEntity> returnList(){

        return donationsEntitiesList;

    }
}
