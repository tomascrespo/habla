package com.crpilarsoubrier.habla.ui.dashboard;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crpilarsoubrier.habla.R;
import com.crpilarsoubrier.habla.data.PictoViewModel;

import java.util.Locale;

public class DashboardFragment extends Fragment {


    private static final String TAG = "DashboardFragment";
    //private GalleryViewModel galleryViewModel;
    //protected RecyclerView mPictoRecyclerView;
    //protected PictoRecyclerViewAdapter mPictoAdapter;
    //protected RecyclerView.LayoutManager mLayoutManager;
    //protected String[] mDataset;
    private static final int FIXED_SPAN_COUNT = 2;
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
        fixedPictosViewModel.getAllPictos().observe(this, pictos -> {
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
        mainPictosViewModel = new ViewModelProvider(this).get(PictoViewModel.class);
        mainPictosViewModel.getAllPictos().observe(this, pictos -> {
            // Update the cached copy of the words in the adapter.
            mainPictoAdapter.submitList(pictos);
        });

        return rootView;
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