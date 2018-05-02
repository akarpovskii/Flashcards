package com.gmail.ooad.flashcards.slideshow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.ooad.flashcards.R;

public class SlideShowStatisticsFragment extends Fragment {
    SlideShowStatistics mStatistics;

    public SlideShowStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static SlideShowStatisticsFragment NewInstance(SlideShowStatistics stat) {
        SlideShowStatisticsFragment fragment = new SlideShowStatisticsFragment();
        Bundle args = new Bundle();
        args.putParcelable("statistics", stat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStatistics = getArguments().getParcelable("statistics");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide_show_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int memorized = mStatistics.getMemorized();
        int unmemorized = mStatistics.getUnmemorized();
        ((TextView)view.findViewById(R.id.statistics_memorized)).setText(String.valueOf(memorized));
        ((TextView)view.findViewById(R.id.statistics_total)).setText(String.valueOf(memorized + unmemorized));
    }
}
