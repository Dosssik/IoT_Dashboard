package com.dosssik.iotexample.presenters;

import android.content.Context;

import com.dosssik.iotexample.IoTApplication;
import com.dosssik.iotexample.R;
import com.dosssik.iotexample.managers.DatabaseManager;
import com.dosssik.iotexample.model.RPiResponseModel;
import com.dosssik.iotexample.ui.IDateChooseView;
import com.dosssik.iotexample.utils.NetworkUtil;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by dosssik on 12/10/16.
 */
public class DateChoosePresenter extends MvpBasePresenter<IDateChooseView> {

    @Inject
    DatabaseManager databaseManager;

    @Inject
    Context context;

    private static String selectedDate;

    public DateChoosePresenter() {
        IoTApplication.getComponent().inject(this);
        databaseManager.setDateChoosePresenter(this);
    }

    public void onDateConfirmed(int day, int month, int year) {
        boolean databaseExist = checkIsDatabaseExist(day, month, year);
        if (getView() == null) {
            return;
        }

        if (databaseExist) {
            databaseManager.queryForSelectedDay(selectedDate);
        } else {

            if (!NetworkUtil.isOnline(context)) {
                getView().showToast(R.string.no_connection);
            } else {
                getView().showProgressDialog();
                databaseManager.pullSelectedDay(selectedDate, this);
            }

        }
    }

    private boolean checkIsDatabaseExist(int day, int month, int year) {
        String formatedDay = String.valueOf(day);
        if (formatedDay.length() == 1) formatedDay = "0" + formatedDay;

        String formatedMonth = String.valueOf(month + 1);
        if (formatedMonth.length() == 1) formatedMonth = "0" + formatedMonth;

        selectedDate = String.valueOf(year) +
                "-" +
                formatedMonth +
                "-" +
                formatedDay;

        File file = new File(
                context.getDir(
                        DatabaseManager.DATASTORE_DIR, Context.MODE_PRIVATE).getPath()
                        + "/"
                        + DatabaseManager.DATABASE_NAME_PREFIX
                        + selectedDate
        );

        return file.exists();
    }

    public void onReplicationComplete() {
        if (getView() != null) {
            getView().hideProgressDialog();
            databaseManager.queryForSelectedDay(selectedDate);
        }

    }

    public void stopReplication() {
        databaseManager.stopReplication();
    }

    public void showError(String errorMessage) {
        if (getView() == null) {
            return;
        }
        if (errorMessage.contains("Database not found")) {
            errorMessage = "Database not found";
        }
        getView().showToast(errorMessage);
        getView().hideProgressDialog();
    }

    public void onQueryDone(ArrayList<RPiResponseModel> allData) {
        if (getView() != null) {
            getView().showDescriptionFragment(allData);
        }
    }
}
