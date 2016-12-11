package com.dosssik.iotexample.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.dosssik.iotexample.R;
import com.dosssik.iotexample.model.RPiResponseModel;
import com.dosssik.iotexample.presenters.DateChoosePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dosssik on 12/10/16.
 */

public class DateChooseFragment extends MvpFragment<IDateChooseView, DateChoosePresenter>
        implements IDateChooseView {

    @Bind(R.id.date_choose_fragment_date_picker)
    DatePicker datePicker;

    @Bind(R.id.date_choose_fragment_confirm_button)
    Button confirmButton;

    private ProgressDialog progressDialog;

    public static DateChooseFragment newInstance() {

        Bundle args = new Bundle();

        DateChooseFragment fragment = new DateChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public DateChoosePresenter createPresenter() {
        return new DateChoosePresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.date_choose_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View rootView) {
        ButterKnife.bind(DateChooseFragment.this, rootView);
        datePicker.setMaxDate(System.currentTimeMillis());
        confirmButton.setOnClickListener(click -> onConfirmClick());
    }

    private void onConfirmClick() {
        int dayOfMonth = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        getPresenter().onDateConfirmed(dayOfMonth, month, year);
    }

    @Override
    public void showToast(boolean databaseExist) {
        Toast.makeText(getContext(), "result - " + databaseExist, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDescriptionFragment(ArrayList<RPiResponseModel> data) {
        ((DashboardActivity) getActivity()).showDescriptionFragment(data);
    }

    @Override
    public void showProgressDialog() {
        // TODO: 12/11/16 Обработать поворот экрана
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "button", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 12/11/16 Обработать отмену
                Toast.makeText(getContext(), R.string.download_canceled, Toast.LENGTH_LONG).show();
                getPresenter().stopReplication();
            }
        });
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void showErrorToast(String errorMessage) {
        Toast.makeText(getContext(), "Error случился", Toast.LENGTH_LONG).show();
    }
}
