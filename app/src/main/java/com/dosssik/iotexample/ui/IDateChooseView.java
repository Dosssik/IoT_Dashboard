package com.dosssik.iotexample.ui;

import com.dosssik.iotexample.model.RPiResponseModel;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by dosssik on 12/10/16.
 */
public interface IDateChooseView extends MvpView {

    void showDescriptionFragment(ArrayList<RPiResponseModel> data);

    void showProgressDialog();

    void hideProgressDialog();

    void showToast(int textResId);

    void showToast(String text);
}
