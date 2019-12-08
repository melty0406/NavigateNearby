package com.example.user.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.myapplication.Model.PlaceNearby;
import com.example.user.myapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PlacesNearbyAdapter extends RecyclerView.Adapter<PlacesNearbyAdapter.ViewHolder> {

    // Declaration
    private List<PlaceNearby> mPlaces;
    OnItemClickListener mListener;

    // Constructor
    public PlacesNearbyAdapter(List<PlaceNearby> places){mPlaces = places;}


    // Interface for Item Click Listener
    public interface OnItemClickListener {
        void onItemClick(int p);
    }

    // Function for Item Click Listener
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    //public class ViewHolder extends RecyclerView.ViewHolder {
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvPlaceName;
        public TextView tvVicinity;
        public TextView tvRating;
        public TextView tvDistance;
        public ImageView ivPhoto;
        public ImageView ivStar;
        public ImageView ivNavigate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPlaceName = itemView.findViewById(R.id.tv_placeName);
            tvVicinity = itemView.findViewById(R.id.tv_Vicinity);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
            ivStar = itemView.findViewById(R.id.iv_icStar);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            ivNavigate = itemView.findViewById(R.id.iv_navigate);


            // OnClick function for btnRate

            ivNavigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View placeView = inflater.inflate(R.layout.item_places,parent,false);
        ViewHolder viewHolder = new ViewHolder(placeView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        PlaceNearby placeNearby = mPlaces.get(position);
        DecimalFormat precision = new DecimalFormat("0.00");
        String icon = "";
        String url = "";

        holder.ivStar.setVisibility(View.GONE);

        if (placeNearby.getIcon() != null) {
            icon = placeNearby.getIcon();
            url = icon;
        }

        if(url != null) {
            Picasso.get()
                    .load(url)
                    .into(holder.ivPhoto);
        }

        if(placeNearby.getPlaceName() != null) {
            holder.tvPlaceName.setText(placeNearby.getPlaceName());
        }

        if(placeNearby.getVicinity() != null) {
            holder.tvVicinity.setText(placeNearby.getVicinity());
        }

        if(placeNearby.getRating() != null) {
            holder.tvRating.setText(placeNearby.getRating());
            holder.ivStar.setVisibility(View.VISIBLE);
        }

        double distance = placeNearby.getDistance();
        holder.tvDistance.setText( precision.format(distance) + "km");

    }


    @Override
    public int getItemCount() {
        return mPlaces.size();
    }
}
