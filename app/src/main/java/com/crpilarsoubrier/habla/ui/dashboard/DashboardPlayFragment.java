package com.crpilarsoubrier.habla.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crpilarsoubrier.habla.R;
import com.crpilarsoubrier.habla.data.dashboard.Dashboard;
import com.crpilarsoubrier.habla.view_models.PictoViewModel;
import com.crpilarsoubrier.habla.data.PictoWithChildren;
import com.crpilarsoubrier.habla.view_models.DashboardListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardPlayFragment extends Fragment {


    private static final String TAG = "DashboardPlayFragment";
    //private GalleryViewModel galleryViewModel;
    //protected RecyclerView mPictoRecyclerView;
    //protected PictoRecyclerViewAdapter mPictoAdapter;
    //protected RecyclerView.LayoutManager mLayoutManager;
    //protected String[] mDataset;
    private static final int FIXED_SPAN_COUNT = 8;
    private static final int MAIN_SPAN_COUNT = 6;

    private PictoViewModel fixedPictosViewModel;
    private PictoViewModel mainPictosViewModel;
    private PictoRecyclerViewAdapter mainPictoAdapter;
    private RecyclerView mainPictosRecyclerView;

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
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        rootView.setTag(TAG);

        // Get the arg that decides what dashboard we will play with
//        long dashboardId = PlantDetailFragmentArgs.fromBundle(getArguments()).getPlantId();
//        setupViewModel(plantId);

        // Fixed Pictos
        RecyclerView fixedPictosRecyclerView = (RecyclerView) rootView.findViewById(R.id.fixed_pictos_recycler_view);
        RecyclerView.LayoutManager fixedLayoutManager = new LinearLayoutPagerManager(getActivity(), RecyclerView.HORIZONTAL, false,FIXED_SPAN_COUNT);
        fixedPictosRecyclerView.setLayoutManager(fixedLayoutManager);

        // Let's connect with the data
        fixedPictosViewModel = new ViewModelProvider(this).get(PictoViewModel.class);

        // Test dashboards
        DashboardListViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardListViewModel.class);
        /*
        List<Dashboard> dashboardList = new ArrayList<>();
        dashboardList = dashboardViewModel.allDashboards.getValue();
        if ((dashboardList != null) && (!dashboardList.isEmpty())) {
            for (Dashboard d : dashboardViewModel.allDashboards.getValue()) {
                Log.println(Log.INFO, "MyDebug", "allDashboards: " + d.dashboardId + " - " + d.dashboardName);
            }
        } else Log.println(Log.INFO, "MyDebug", "allDashboards is null");
*/

/*
        Dashboard dashboard = new Dashboard(1,"Asamblea");
        dashboardViewModel.addDashboard(dashboard);
        //dashboardViewModel.addDashboard("Asamblea", true, true, 4, 5);

        DashboardRepository dashboardRepository = new DashboardRepository(getActivity().getApplication());
        DashboardDetailViewModelFactory dashboardDetailViewModelFactory = new DashboardDetailViewModelFactory(dashboardRepository, dashboard.dashboardId);
        DashboardDetailViewModel dashboardDetailViewModel = ViewModelProviders.of(this, (ViewModelProvider.Factory) dashboardDetailViewModelFactory).get(DashboardDetailViewModel.class);

        dashboardDetailViewModel.addPictoToDashboard(1, true,  PictoInDashboardEntity.MAIN_PICTO);
        dashboardDetailViewModel.addPictoToDashboard(2, true,  PictoInDashboardEntity.MAIN_PICTO);
        dashboardDetailViewModel.addPictoToDashboard(3, true,  PictoInDashboardEntity.MAIN_PICTO);
        dashboardDetailViewModel.addPictoToDashboard(4, true,  PictoInDashboardEntity.MAIN_PICTO);
        dashboardDetailViewModel.addPictoToDashboard(5, true,  PictoInDashboardEntity.MAIN_PICTO);
*/

        PictoRecyclerViewAdapter fixedPictoAdapter = new PictoRecyclerViewAdapter(new PictoRecyclerViewAdapter.PictoDiff());
        fixedPictosRecyclerView.setAdapter(fixedPictoAdapter);
        fixedPictoAdapter.setOnPictoClickListener(onPictoClickListener);
        fixedPictosViewModel.getAllPictosWithChildren().observe(this.getViewLifecycleOwner(), pictos -> {
            // Update the cached copy of the words in the adapter.
            fixedPictoAdapter.submitList(pictos);
        });

        // Main Pictos
        RecyclerView mainPictosRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_pictos_recycler_view);
        this.mainPictosRecyclerView = mainPictosRecyclerView;
        RecyclerView.LayoutManager mainLayoutManager = new GridLayoutManager(getActivity(), MAIN_SPAN_COUNT);
        mainPictosRecyclerView.setLayoutManager(mainLayoutManager);
        //mPictoRecyclerView.scrollToPosition(scrollPosition);
        // Let's connect with the data
        this.mainPictosViewModel = new ViewModelProvider(this).get(PictoViewModel.class);

        PictoRecyclerViewAdapter mainPictoAdapter = new PictoRecyclerViewAdapter(new PictoRecyclerViewAdapter.PictoDiff());
        this.mainPictoAdapter = mainPictoAdapter;
        mainPictosRecyclerView.setAdapter(mainPictoAdapter);
        mainPictoAdapter.setOnPictoClickListener(onPictoClickListener);
        //mainPictosViewModel.setCategory(Integer.toUnsignedLong(1));

        dashboardViewModel.allDashboards.observe(getViewLifecycleOwner(), dashboards -> {
            Log.println(Log.INFO, "MyDebug", "allDashboards.observe()");
            if (dashboards != null) {
                for (Dashboard d : dashboards) {
                    Log.println(Log.INFO, "MyDebug", "allDashboards: " + d.dashboardId + " - " + d.dashboardName);
                }
            } else Log.println(Log.INFO, "MyDebug", "allDashboards.observe(): allDashboards is null");
            //dashboardViewModel.addDashboard("Probando", true, true, 4, 4);
        });

        this.mainPictosViewModel.currentCategoryPictosWithChildren.observe(getViewLifecycleOwner(), pictos -> {
            if (pictos != null) {
                mainPictoAdapter.submitList(pictos);
                //Log.println(Log.INFO, "MyDebug", "currentCategoryPictosWithChildren.observe()");
                //dashboardViewModel.addDashboard("Probando", true, true, 4, 4);
                Log.println(Log.INFO, "MyDebug", "allDashboards.size(): " + dashboardViewModel.allDashboards.getValue().size());

            }
        });
/*
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainPictoAdapter.notifyDataSetChanged();
                Log.println(Log.INFO, "MyDebug", "DashboardFragment: mainPictoAdapter.getItemCount(): " + mainPictoAdapter.getItemCount());
                Log.println(Log.INFO, "MyDebug", "...after 8 seconds...");
                //mainPictosViewModel.setCategory(Integer.toUnsignedLong(20));
                //Picto p = new Picto("5.png","0 in DB");
                //mainPictosViewModel.insert(p);
                //mainPictosViewModel.delete(5);

                //mainPictoAdapter.notifyItemRemoved(3);
                //mainPictoAdapter.notifyDataSetChanged();
            }
        }, 8000);
*/

        return rootView;
    }

    // This is a variable that will be passed to the recyclerview adapter and it will pass to the viewholder
    private View.OnClickListener onPictoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: Step 4 of 4: Finally call getTag() on the view.
            // This viewHolder will have all required values.
            PictoViewHolder viewHolder = (PictoViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            //Toast.makeText(getContext(), "Estoy en mi nuevo onClick", Toast.LENGTH_SHORT).show();
            PictoWithChildren picto = viewHolder.getPicto();
            if (picto.picto.getId() == -1000) mainPictosViewModel.setCategory(null);
            else {
                if (picto.isCategory()) {
                    mainPictosViewModel.setCategory(picto.picto.getId());
                }
                if (picto.picto.shouldBeRead()) readPicto(picto);
                // add the picto to the phrase, if dashboard is phrase-enabled
            }
        }
    };

    static public void readPicto(PictoWithChildren picto) {
        String toSpeak = picto.picto.text;
        textToSpeechEngine.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        textToSpeechEngine.shutdown();;
    }

}
