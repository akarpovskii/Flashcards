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
 * Edit Hyperlink Activity
 * Created by even.wu on 10/8/17.
 */

public class EditHyperlinkDialogFragment extends DialogFragment {
    private EditText etAddress;

    private EditText etDisplayText;

    private OnHyperlinkListener mOnHyperlinkListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View rootView = Objects.requireNonNull(getActivity()).getLayoutInflater()
                .inflate(R.layout.fragment_edit_hyperlink, null);
        etAddress = rootView.findViewById(R.id.et_address);
        etDisplayText = rootView.findViewById(R.id.et_display_text);

        rootView.findViewById(R.id.iv_back).setOnClickListener(v -> dismiss());

        rootView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            if (mOnHyperlinkListener != null) {
                mOnHyperlinkListener.onHyperlinkOK(etAddress.getText().toString(),
                        etDisplayText.getText().toString());
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }

    public void setOnHyperlinkListener(OnHyperlinkListener mOnHyperlinkListener) {
        this.mOnHyperlinkListener = mOnHyperlinkListener;
    }

    public interface OnHyperlinkListener {
        void onHyperlinkOK(String address, String text);
    }
}
