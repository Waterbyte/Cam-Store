package sagar.mehar.camstore.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sagar.mehar.camstore.R;

/**
 * Created by Mountain on 09-11-18.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private Context localContext;
    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private SparseBooleanArray mSelectedItems;
    private boolean mIsInChoiceMode;

    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    public void switchSelectedState(int position) {
        if (mSelectedItems.get(position)) {
            mSelectedItems.delete(position);
        } else {
            mSelectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void clearSelectedState() {
        List<Integer> selection = getSelectedItems();
        mSelectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    public void beginChoiceMode(int position) {
        mSelectedItems = new SparseBooleanArray(getItemCount());
        setIsInChoiceMode(true);
        switchSelectedState(position);
    }

    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); ++i) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

    public void setIsInChoiceMode(boolean isInChoiceMode) {
        this.mIsInChoiceMode = isInChoiceMode;

    }

    public boolean getIsInChoiceMode() {
        return mIsInChoiceMode;
    }


    public GridAdapter(Context context, ArrayList<String> data) {
        this.localContext = context;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        if (mIsInChoiceMode) {
            viewHolder.gridCheckBox.setVisibility(View.VISIBLE);
            viewHolder.gridCheckBox.setChecked(mSelectedItems.get(position));
        } else {
            viewHolder.gridCheckBox.setChecked(false);
            viewHolder.gridCheckBox.setVisibility(View.GONE);
        }


        String filePath = mData.get(position);
        File imageFile = new File(filePath);
        if (imageFile.isDirectory()) {
            Glide.with(localContext).load(R.drawable.folderdisplay).into(viewHolder.gridImageView);
            viewHolder.gridDescriptionView.setText(imageFile.getName());
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".png")) {
            Glide.with(localContext).load(imageFile).into(viewHolder.gridImageView);
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

        boolean onItemLongClicked(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView gridImageView;
        TextView gridDescriptionView;
        CheckBox gridCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            gridImageView = itemView.findViewById(R.id.gridImage);
            gridDescriptionView = itemView.findViewById(R.id.gridImageDescription);
            gridCheckBox = itemView.findViewById(R.id.gridImageCheck);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mClickListener != null)
                return mClickListener.onItemLongClicked(view, getAdapterPosition());

            return true;
        }
    }

}
