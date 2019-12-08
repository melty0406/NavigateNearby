package com.example.user.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.myapplication.Model.PlaceNavigated;
import com.example.user.myapplication.Model.PlaceNearby;
import com.example.user.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlacesNavigatedAdapter extends RecyclerView.Adapter<PlacesNavigatedAdapter.ViewHolder> {

    // Declaration
    private List<PlaceNavigated> mPlaces;

    // Constructor
    public PlacesNavigatedAdapter(List<PlaceNavigated> places){mPlaces = places;}

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvPlaceNavName;
        public TextView tvPlaceNavDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPlaceNavName = itemView.findViewById(R.id.tv_nav_place_name);
            tvPlaceNavDate = itemView.findViewById(R.id.tv_nav_place_date);
        }
    }

    @NonNull
    @Override
    public PlacesNavigatedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View placeView = inflater.inflate(R.layout.item_places_navigated,parent,false);
        ViewHolder viewHolder = new ViewHolder(placeView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesNavigatedAdapter.ViewHolder holder, int position) {
        PlaceNavigated placeNavigated = mPlaces.get(position);

        holder.tvPlaceNavName.setText(placeNavigated.getPlaceName());
        holder.tvPlaceNavDate.setText(placeNavigated.getDate());

    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }


}
