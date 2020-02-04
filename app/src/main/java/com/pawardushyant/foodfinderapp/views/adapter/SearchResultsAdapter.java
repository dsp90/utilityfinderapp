package com.pawardushyant.foodfinderapp.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.pawardushyant.foodfinderapp.R;
import com.pawardushyant.foodfinderapp.repository.models.PlacePhoto;
import com.pawardushyant.foodfinderapp.repository.models.ResultHits;
import com.pawardushyant.foodfinderapp.utils.Commons;
import com.pawardushyant.foodfinderapp.utils.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.MyViewHolder> {

    private List<ResultHits> mResultHits;
    private final RequestManager glide;

    public SearchResultsAdapter(RequestManager glide) {
        this.glide = glide;
    }

    @NonNull
    @Override
    public SearchResultsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item
                        , parent
                        , false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.MyViewHolder holder, int position) {
        holder.onBind(mResultHits.get(position));
    }

    public void setResultHits(List<ResultHits> resultHits) {
        mResultHits = resultHits;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mResultHits != null ? mResultHits.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRating, tvType;
        ImageView ivImage;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_result_title);
            tvType = itemView.findViewById(R.id.tv_result_desc_small);
            tvRating = itemView.findViewById(R.id.tv_result_rating);
            ivImage = itemView.findViewById(R.id.iv_brand_image);
        }

        void onBind(ResultHits resultHits) {
            tvName.setText(resultHits.getName());
            tvType.setText(resultHits.getVicinity());
            setRating(resultHits.getRating(), tvRating);
            List<PlacePhoto> photos = resultHits.getPhotos();
            String imageurl = (photos != null && photos.size() > 0 )?
                    Commons.getImageUrl(photos.get(0).getPhoto_reference()) :
                    Constants.DEFAULT_IMAGE_URL;
            glide.load(imageurl).into(ivImage);
        }

        void setRating(String rating, TextView tvRating) {
            float ratingFl = rating != null ? Float.valueOf(rating) : 0.f;
            if (ratingFl >= 4.5f) {
                tvRating.setBackgroundColor(tvRating.getContext().getColor(R.color.green_dark));
            } else {
                tvRating.setBackgroundColor(tvRating.getContext().getColor(R.color.green_light));
            }
            tvRating.setText(String.valueOf(ratingFl));
        }
    }
}
