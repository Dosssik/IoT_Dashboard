package com.dosssik.iotexample.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.dosssik.iotexample.R;

/**
 * Created by dosssik on 1/4/17.
 */

public class ProgressDialogFragment extends DialogFragment {

    public static ProgressDialogFragment newInstance() {
        return new ProgressDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                getString(R.string.cancel_button_text), (dialogInterface, i) -> {
                    onCancelClick();
                });
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return progressDialog;
    }

    private void onCancelClick() {
        if (getTargetFragment() != null) {
            DateChooseFragment parentFragment = (DateChooseFragment) getTargetFragment();
            parentFragment.onDialogCancelClick();
        }
    }
}
