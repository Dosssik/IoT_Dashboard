package com.dosssik.iotexample.ui;

import com.dosssik.iotexample.presenters.DateChoosePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

/**
 * Created by dosssik on 12/10/16.
 */

public class DateChooseFragment extends MvpFragment<IDateChooseView, DateChoosePresenter> {
    @Override
    public DateChoosePresenter createPresenter() {
        return new DateChoosePresenter();
    }
}
