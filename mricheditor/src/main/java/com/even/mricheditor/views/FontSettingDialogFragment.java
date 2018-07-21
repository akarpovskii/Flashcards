package com.even.mricheditor.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.even.mricheditor.R;
import com.even.mricheditor.adapter.FontSettingAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Font Setting Fragment
 * Created by even.wu on 9/8/17.
 */

public class FontSettingDialogFragment extends DialogFragment {
    public static final String TYPE = "type";

    public static final int TYPE_SIZE = 0;
    public static final int TYPE_LINE_HEIGHT = 1;
    public static final int TYPE_FONT_FAMILY = 2;

    private static final List<String> FONT_FAMILY_LIST =
        Arrays.asList("Roboto", "Merriweather", "Montserrat", "Open Sans", "Playfair Display");

    private static final List<String> FONT_SIZE_LIST =
        Arrays.asList("12", "14", "16", "18", "20", "22", "24", "26", "28", "36");

    private static final List<String> FONT_LINE_HEIGHT_LIST =
        Arrays.asList("1.0", "1.2", "1.4", "1.6", "1.8", "2.0", "3.0");

    private RecyclerView rvContainer;
    private OnResultListener mOnResultListener;
    private List<String> dataSourceList = FONT_SIZE_LIST;

    public static FontSettingDialogFragment NewInstance(int type) {
        FontSettingDialogFragment fragment = new FontSettingDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        @SuppressLint("InflateParams")
        View rootView = Objects.requireNonNull(getActivity()).getLayoutInflater()
                .inflate(R.layout.fragment_font_setting, null);
        rvContainer = rootView.findViewById(R.id.rv_container);

        int type = TYPE_SIZE;

        if (getArguments() != null) {
            type = getArguments().getInt(TYPE);
        }

        if (type == TYPE_SIZE) {
            dataSourceList = FONT_SIZE_LIST;
        } else if (type == TYPE_LINE_HEIGHT) {
            dataSourceList = FONT_LINE_HEIGHT_LIST;
        } else if (type == TYPE_FONT_FAMILY) {
            dataSourceList = FONT_FAMILY_LIST;
        }

        initRecyclerView();

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }

    private void initRecyclerView() {
        rvContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        FontSettingAdapter adapter = new FontSettingAdapter(dataSourceList);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (mOnResultListener != null) {
                mOnResultListener.onResult(dataSourceList.get(position));
            }
            FontSettingDialogFragment.this.dismiss();
        });
        rvContainer.setAdapter(adapter);
    }

    interface OnResultListener {

        void onResult(String result);
    }

    public void setOnResultListener(OnResultListener mOnResultListener) {
        this.mOnResultListener = mOnResultListener;
    }
}
