package com.chiriacd.whitbread;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiriacd.whitbread.foursquare.api.GroupItem;
import com.chiriacd.whitbread.whitbread.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class RecommendedVenuesAdapter extends RecyclerView.Adapter<RecommendedVenuesAdapter.VenuesViewHolder> {

    private List<GroupItem.Venue> venues;

    public RecommendedVenuesAdapter() {
        venues = new ArrayList<>();
    }

    public RecommendedVenuesAdapter(List<GroupItem.Venue> venues) {
        this.venues = venues;
    }

    public void setData(List<GroupItem.Venue> venues) {
        this.venues = venues;
        notifyDataSetChanged();
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

    public static class VenuesViewHolder extends RecyclerView.ViewHolder {

        TextView ratingView;
        TextView nameView;

        public VenuesViewHolder(View itemView) {
            super(itemView);
            ratingView = itemView.findViewById(R.id.rating);
            nameView = itemView.findViewById(R.id.venue_name);
        }

        public void bind(GroupItem.Venue venue) {
            ratingView.setText(String.valueOf(venue.getRating()));
            nameView.setText(venue.getName());
        }
    }
}
