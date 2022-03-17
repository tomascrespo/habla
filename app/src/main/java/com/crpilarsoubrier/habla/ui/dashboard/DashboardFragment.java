package com.crpilarsoubrier.habla.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crpilarsoubrier.habla.R;
import com.crpilarsoubrier.habla.data.Picto;
import com.crpilarsoubrier.habla.data.PictoViewModel;

import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {


    private static final String TAG = "DashboardFragment";
    //private GalleryViewModel galleryViewModel;
    //protected RecyclerView mPictoRecyclerView;
    //protected PictoRecyclerViewAdapter mPictoAdapter;
    //protected RecyclerView.LayoutManager mLayoutManager;
    //protected String[] mDataset;
    private static final int FIXED_SPAN_COUNT = 8;
    private static final int MAIN_SPAN_COUNT = 6;

    private PictoViewModel fixedPictosViewModel;
    private PictoViewModel mainPictosViewModel;

    static private TextToSpeech textToSpeechEngine = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeechEngine = new TextToSpeech(this.getContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeechEngine.setLanguage(new Locale("spa", "ESP"));
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

/*
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
*/
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        rootView.setTag(TAG);

        // Fixed Pictos
        RecyclerView fixedPictosRecyclerView = (RecyclerView) rootView.findViewById(R.id.fixed_pictos_recycler_view);
        RecyclerView.LayoutManager fixedLayoutManager = new LinearLayoutPagerManager(getActivity(), RecyclerView.HORIZONTAL, false,FIXED_SPAN_COUNT);
        fixedPictosRecyclerView.setLayoutManager(fixedLayoutManager);
        PictoRecyclerViewAdapter fixedPictoAdapter = new PictoRecyclerViewAdapter(new PictoRecyclerViewAdapter.PictoDiff());
        fixedPictosRecyclerView.setAdapter(fixedPictoAdapter);
        // Let's connect with the data
        fixedPictosViewModel = new ViewModelProvider(this).get(PictoViewModel.class);
        fixedPictosViewModel.getAllPictos().observe(this.getViewLifecycleOwner(), pictos -> {
            // Update the cached copy of the words in the adapter.
            fixedPictoAdapter.submitList(pictos);
        });

        // Main Pictos
        RecyclerView mainPictosRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_pictos_recycler_view);
        RecyclerView.LayoutManager mainLayoutManager = new GridLayoutManager(getActivity(), MAIN_SPAN_COUNT);
        mainPictosRecyclerView.setLayoutManager(mainLayoutManager);
        //mPictoRecyclerView.scrollToPosition(scrollPosition);
        PictoRecyclerViewAdapter mainPictoAdapter = new PictoRecyclerViewAdapter(new PictoRecyclerViewAdapter.PictoDiff());
        mainPictosRecyclerView.setAdapter(mainPictoAdapter);
        // Let's connect with the data
        this.mainPictosViewModel = new ViewModelProvider(this).get(PictoViewModel.class);

        this.mainPictosViewModel.currentCategoryPictos.observe(getViewLifecycleOwner(), pictos -> {
            if (pictos != null) {
                mainPictoAdapter.submitList(pictos);
                Log.println(Log.INFO, "MyDebug", "DashboardFragment: observe()");
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainPictoAdapter.notifyDataSetChanged();
                Log.println(Log.INFO, "MyDebug", "DashboardFragment: mainPictoAdapter.getItemCount(): " + mainPictoAdapter.getItemCount());
                Log.println(Log.INFO, "MyDebug", "...after 10 seconds...");
                mainPictosViewModel.setCategory(Integer.toUnsignedLong(20));
                //Picto p = new Picto("5.png","0 in DB");
                //mainPictosViewModel.insert(p);
                //mainPictosViewModel.delete(5);

                //mainPictoAdapter.notifyItemRemoved(3);
                //mainPictoAdapter.notifyDataSetChanged();
            }
        }, 10000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.println(Log.INFO, "MyDebug", "DashboardFragment: mainPictoAdapter.getItemCount(): " + mainPictoAdapter.getItemCount());
                Log.println(Log.INFO, "MyDebug", "...after 15 seconds...");
                mainPictosViewModel.setCategory(Integer.toUnsignedLong(74));
                //mainPictosViewModel.delete(196);
                //mainPictosViewModel.delete(3);
                //mainPictosViewModel.setCategory(Integer.toUnsignedLong(20));
                //mainPictoAdapter.notifyDataSetChanged();
            }
        }, 15000);

        return rootView;
    }

    static public void readText(String toSpeak, Context context) {
        //Toast.makeText(context, toSpeak, Toast.LENGTH_SHORT).show();
        textToSpeechEngine.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
    static public void readPicto(View v) {
        TextView tv = (TextView) v;
        String toSpeak = tv.getText().toString();
        textToSpeechEngine.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        textToSpeechEngine.shutdown();;
    }

}