package com.crpilarsoubrier.habla.ui.dashboard;
/*
 *
  */

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crpilarsoubrier.habla.R;
import com.crpilarsoubrier.habla.data.Picto;
import com.google.android.material.snackbar.Snackbar;


/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class PictoRecyclerViewAdapter extends ListAdapter<Picto, PictoViewHolder> {

    private static final String TAG = "PictoRecyclerViewAdapter";

    //private String[] mDataSet; // The pictos and their text

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    /* // I have a specific viewholder in PictoViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Pictograma en posición " + getAdapterPosition() + " pulsado", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            textView = (TextView) v.findViewById(R.id.picto_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)
    */
    /**
     */
    public PictoRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<Picto> diffCallback) {
        super(diffCallback);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PictoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PictoViewHolder.create(parent);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PictoViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Element " + position + " set.");
        viewHolder.bind(getItem(position), viewHolder.itemView.getContext());
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    static class PictoDiff extends DiffUtil.ItemCallback<Picto> {

        @Override
        public boolean areItemsTheSame(@NonNull Picto oldItem, @NonNull Picto newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Picto oldItem, @NonNull Picto newItem) {
            return oldItem.getText().equals(newItem.getText()) && oldItem.getPicFilePath().equals(newItem.getPicFilePath()) ;
        }
    }

}