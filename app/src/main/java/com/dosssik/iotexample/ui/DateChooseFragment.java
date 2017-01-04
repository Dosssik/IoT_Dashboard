package com.dosssik.iotexample.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dosssik.iotexample.R;
import com.dosssik.iotexample.model.RPiResponseModel;
import com.dosssik.iotexample.presenters.DateChoosePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dosssik on 12/10/16.
 */

public class DateChooseFragment extends MvpFragment<IDateChooseView, DateChoosePresenter>
        implements IDateChooseView {


    @Bind(R.id.date_choose_fragment_choose_date_button)
    Button chooseDateButton;

    @Bind(R.id.date_choose_fragment_confirm_button)
    Button confirmButton;

    @Bind(R.id.date_choose_fragment_selected_date)
    TextView selectedDateTv;

    private static int selectedDay = 0;
    private static int selectedMonth = 0;
    private static int selectedYear = 0;

    private DatePickerDialog.OnDateSetListener dateListener;

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

        initDate();
        initDateListener();
        updateSelectedDateTv();

        chooseDateButton.setOnClickListener(click -> {
            new DatePickerDialog(getActivity(),
                    dateListener,
                    selectedYear,
                    selectedMonth,
                    selectedDay)
                    .show();
        });

        confirmButton.setOnClickListener(click -> onConfirmClick());
    }

    private void updateSelectedDateTv() {
        String day = String.valueOf(selectedDay);
        String month = String.valueOf(selectedMonth + 1);
        selectedDateTv.setText(makeReadableDate(day) + "/" + makeReadableDate(month) + "/" + selectedYear);
    }

    private String makeReadableDate(String date) {
        if (date.length() == 1) {
            date = "0" + date;
        }
        return date;
    }

    private void initDate() {
        if (selectedYear == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
            selectedMonth = calendar.get(Calendar.MONTH);
            selectedYear = calendar.get(Calendar.YEAR);
        }
    }

    private void initDateListener() {
        dateListener = (view, year, monthOfYear, dayOfMonth) -> {
            selectedDay = dayOfMonth;
            selectedMonth = monthOfYear;
            selectedYear = year;
            updateSelectedDateTv();
        };
    }

    private void onConfirmClick() {

        if (selectedDateIsValid()) {
            getPresenter().onDateConfirmed(selectedDay, selectedMonth, selectedYear);
        } else {
            showToast(R.string.date_not_valid);
        }
    }

    private boolean selectedDateIsValid() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay);
        Date selectedDate = calendar.getTime();
        return today.after(selectedDate);
    }

    public void showToast(int textResId) {
        Toast.makeText(getContext(), textResId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDescriptionFragment(ArrayList<RPiResponseModel> data) {
        ((DashboardActivity) getActivity()).showDescriptionFragment(data);
    }

    @Override
    public void showProgressDialog() {

        ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance();
        progressDialogFragment.setTargetFragment(this, 0);
        progressDialogFragment.show(getFragmentManager(), ProgressDialogFragment.class.getName());
    }

    @Override
    public void hideProgressDialog() {
        Fragment dialog = getFragmentManager()
                .findFragmentByTag(ProgressDialogFragment.class.getName());
        if (dialog != null) {
            ((DialogFragment) dialog).dismiss();
        }
    }

    @Override
    public void showErrorToast(String errorMessage) {
        Toast.makeText(getContext(), "Ошибка загрузки", Toast.LENGTH_LONG).show();
    }

    public void onDialogCancelClick() {
        showToast(R.string.download_canceled);
        getPresenter().stopReplication();
    }
}
