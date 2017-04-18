package com.example.rmontoya.retrofitservice.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rmontoya.retrofitservice.model.FourSquareVenue;

import java.util.List;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {
    private List<FourSquareVenue> items;

    public VenueAdapter(List<FourSquareVenue> fourSquareVenues) {
        this.items = fourSquareVenues;
    }

    @Override
    public VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VenueViewHolder holder, int position) {
        FourSquareVenue venue = items.get(position);
        holder.venueNameTextView.setText(venue.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VenueViewHolder extends RecyclerView.ViewHolder {

        TextView venueNameTextView;

        VenueViewHolder(View itemView) {
            super(itemView);
            venueNameTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }

    }

}