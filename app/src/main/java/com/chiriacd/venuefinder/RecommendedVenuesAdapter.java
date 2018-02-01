package com.chiriacd.venuefinder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiriacd.venuefinder.foursquare.translation.VenueWrapper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;


public class RecommendedVenuesAdapter extends RecyclerView.Adapter<RecommendedVenuesAdapter.VenuesViewHolder> {

    private List<VenueWrapper> venues;
    private String location;

    public RecommendedVenuesAdapter() {
        location = "";
        venues = new ArrayList<>();
    }

    public void setData(String location, List<VenueWrapper> venues) {
        this.location = location;
        this.venues = venues;
        notifyDataSetChanged();
    }

    public List<VenueWrapper> getData() {
        return venues;
    }

    @Override
    public VenuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venues_adapter_item, parent, false);
        return new VenuesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VenuesViewHolder holder, int position) {
        holder.bind(venues.get(position));
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public void clear() {
        venues.clear();
        location = "";
        notifyDataSetChanged();
    }

    public String getLocation() {
        return location;
    }

    public static class VenuesViewHolder extends RecyclerView.ViewHolder {

        TextView ratingView;
        TextView nameView;
        SimpleDraweeView icon;

        public VenuesViewHolder(View itemView) {
            super(itemView);
            ratingView = itemView.findViewById(R.id.rating);
            nameView = itemView.findViewById(R.id.venue_name);
            icon = itemView.findViewById(R.id.icon);//Fresco handles recycling
        }

        public void bind(VenueWrapper venue) {
            ratingView.setText(String.valueOf(venue.getRating()));
            nameView.setText(venue.getName());
            icon.setImageURI(Uri.parse(venue.getIconUrl()));
        }
    }
}
