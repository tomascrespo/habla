package com.crpilarsoubrier.habla.ui.dashboard;
/*
 *
  */

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.crpilarsoubrier.habla.data.Picto;
import com.crpilarsoubrier.habla.view_models.PictoViewModel;
import com.crpilarsoubrier.habla.data.PictoWithChildren;

import java.util.ArrayList;
import java.util.List;


/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class PictoRecyclerViewAdapter extends ListAdapter<PictoWithChildren, PictoViewHolder> {

    private static final String TAG = "PictoRecyclerViewAdapter";
    private PictoViewModel pictoViewModel; // Not in use currently
    private View.OnClickListener mOnPictoClickListener;

    /**
     */
    public PictoRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<PictoWithChildren> diffCallback) {
        super(diffCallback);
    }

    /**
     * Allows assign a function that resides in DashboardFragment
     * @param pictoClickListener
     */
    public void setOnItemClickListener(View.OnClickListener pictoClickListener) {
        mOnPictoClickListener = pictoClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PictoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PictoViewHolder.create(parent, mOnPictoClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PictoViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Element " + position + " set.");
        viewHolder.bind(getItem(position), viewHolder.itemView.getContext());
    }

    /**
     * We will hack this method to add a new picto (go back picto)
     * that does not exists in the real view model
     * @param list
     */
    @Override
    public void submitList(List<PictoWithChildren> list) {
        // We should check if we need to add a go back picto or not
        // We will do it checking if the first picto of the list is in a category or not
        if ((list != null) && (!list.isEmpty())){
            PictoWithChildren picto = list.get(0);
            if( (picto != null) && !picto.isCategory()) {
                PictoWithChildren backPicto = new PictoWithChildren();
                backPicto.picto = new Picto("-1000.png", "VOLVER");
                backPicto.picto.setId(-1000);
                backPicto.children = new ArrayList<>();
                list.add(0, backPicto);
            }
        }
        super.submitList(list);

    }

    // Sets the on click listener
    public void setOnPictoClickListener(View.OnClickListener pictoClickListener) {
        mOnPictoClickListener = pictoClickListener;
    }

    /**
     * PictoDiff, allowing compare two pictos to create beautiful animations when
     * add or remove a picto
     */
    static class PictoDiff extends DiffUtil.ItemCallback<PictoWithChildren> {
        @Override
        public boolean areItemsTheSame(@NonNull PictoWithChildren oldItem, @NonNull PictoWithChildren newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PictoWithChildren oldItem, @NonNull PictoWithChildren newItem) {
            return oldItem.picto.getText().equals(newItem.picto.getText()) && oldItem.picto.getPicFilePath().equals(newItem.picto.getPicFilePath()) ;
        }
    }
}