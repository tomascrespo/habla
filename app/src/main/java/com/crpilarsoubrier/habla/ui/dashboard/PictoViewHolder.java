package com.crpilarsoubrier.habla.ui.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;

class PictoViewHolder extends RecyclerView.ViewHolder {
    private final TextView pictoTextItemView;
    private final ImageView pictoImageItemView;
    private static final String  ALBUM_NAME = "Habla";

    private PictoViewHolder(View itemView) {
        super(itemView);
        pictoTextItemView = itemView.findViewById(R.id.picto_text);
        pictoImageItemView = itemView.findViewById(R.id.picto_pic);
    }

    public void bind(Picto picto, Context context) {
        pictoTextItemView.setText(picto.text);
        //File picFile = new File(picto.picFilePath);
        File picFile =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME + "/" + picto.picFilePath);
        if (picFile.exists()) {
            Bitmap picBitmap = BitmapFactory.decodeFile(picFile.getAbsolutePath());
            pictoImageItemView.setImageBitmap(picBitmap);
        } else {
            Log.e("viewholder", picto.picFilePath + " doesn't exists");
        }

    }

    static PictoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_picto_item, parent, false);
        return new PictoViewHolder(view);
    }
}
