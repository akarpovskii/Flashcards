package com.even.mricheditor.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.even.mricheditor.R;

import java.util.Objects;

/**
 * Edit Table Fragment
 * Created by even.wu on 10/8/17.
 */

public class EditTableDialogFragment extends DialogFragment {
    private EditText etRows;

    private EditText etCols;

    private OnTableListener mOnTableListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View rootView = Objects.requireNonNull(getActivity()).getLayoutInflater()
                .inflate(R.layout.fragment_edit_table, null);

        etRows = rootView.findViewById(R.id.et_rows);
        etCols = rootView.findViewById(R.id.et_cols);

        rootView.findViewById(R.id.iv_back).setOnClickListener(v -> dismiss());

        rootView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            if (mOnTableListener != null) {
                mOnTableListener.onTableOK(Integer.valueOf(etRows.getText().toString()),
                        Integer.valueOf(etCols.getText().toString()));
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }

    public void setOnTableListener(OnTableListener mOnTableListener) {
        this.mOnTableListener = mOnTableListener;
    }

    public interface OnTableListener {
        void onTableOK(int rows, int cols);
    }
}
