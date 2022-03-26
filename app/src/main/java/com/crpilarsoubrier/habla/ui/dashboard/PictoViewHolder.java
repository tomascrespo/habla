package com.crpilarsoubrier.habla.ui.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.crpilarsoubrier.habla.R;
import com.crpilarsoubrier.habla.data.Picto;
import com.crpilarsoubrier.habla.data.PictoViewModel;
import com.crpilarsoubrier.habla.data.PictoWithChildren;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class PictoViewHolder extends RecyclerView.ViewHolder {
    private final TextView pictoTextItemView;
    private final ImageView pictoImageItemView;
    private static final String  ALBUM_NAME = "Habla";
    private PictoWithChildren picto;

    private PictoViewHolder(View itemView, View.OnClickListener pictoClickListener) {
        super(itemView);
        pictoTextItemView = itemView.findViewById(R.id.picto_text);
        pictoImageItemView = itemView.findViewById(R.id.picto_pic);
        itemView.setTag(this);
        itemView.setOnClickListener(pictoClickListener);
    }

    public void bind(PictoWithChildren picto, Context context) {
        this.picto = picto;
        pictoTextItemView.setText(picto.picto.text);
        //pictoTextItemView.setText(picto.picto.pictoId + " " + picto.picto.text + " (" + picto.picto.parentId + ") - ");
        //pictoTextItemView.setText(picto.pictoId + " " + picto.text + " (" + picto.parentId + ") - " + this.pictoViewModel.getChildrenCount(picto.pictoId));
        String textToRead = picto.picto.text;
        //File picFile = new File(picto.picFilePath);
        File picFile =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME + "/" + picto.picto.picFilePath);
        if (picFile.exists()) {
            Bitmap picBitmap = BitmapFactory.decodeFile(picFile.getAbsolutePath());
            pictoImageItemView.setImageBitmap(picBitmap);
        } else {
            Log.e("MyDebug", picto.picto.picFilePath + " doesn't exists");
        }

        //if (picto.isCategory()) this.itemView.findViewById(R.id.picto_card_view).setBackgroundColor(CATEGORY_COLOR);
        if (picto.isCategory()) this.itemView.findViewById(R.id.picto_card_view).setBackgroundColor(getRandomCategoryColor(this.itemView.getContext()));
        else this.itemView.findViewById(R.id.picto_card_view).setBackgroundColor(Color.WHITE);

        // If dashboard is configured to not showing text we should
        // do textview picto_text visibility := gone
    }

    public PictoWithChildren getPicto(){
        return this.picto;
    }

    static public Integer getRandomCategoryColor(Context context) {
        String colors[] = context.getResources().getStringArray(R.array.category_colors);
        List<Integer> colorList = new ArrayList<Integer>();

        // add the color array to the list
        for (int i = 0; i < colors.length; i++) {
            colorList.add(Color.parseColor(colors[i]));
        }

        int rnd = new Random().nextInt(colors.length);
        return colorList.get(rnd);
    }


    static PictoViewHolder create(ViewGroup parent, View.OnClickListener pictoClickListener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_picto_item, parent, false);
        return new PictoViewHolder(view, pictoClickListener);
    }
}
