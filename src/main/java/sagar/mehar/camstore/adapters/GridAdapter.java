package sagar.mehar.camstore.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import sagar.mehar.camstore.R;
import sagar.mehar.camstore.utils.SupportedFileFilter;

/**
 * Created by Mountain on 09-11-18.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public GridAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.grid_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String filePath = mData.get(i);
        File imageFile = new File(filePath);
        if (imageFile.isDirectory()) {
            Picasso.get().load(R.drawable.addfolder).into(viewHolder.gridImageView);
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".png")) {
            Picasso.get().load(imageFile).fit().into(viewHolder.gridImageView);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView gridImageView;
        TextView gridDescriptionView;
        CheckBox gridCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            gridImageView = itemView.findViewById(R.id.gridImage);
            //gridDescriptionView = itemView.findViewById(R.id.gridDescription);
            //gridCheckBox = itemView.findViewById(R.id.gridCheckBox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

}
