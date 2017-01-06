package com.dosssik.iotexample.ui;

import com.github.mikephil.charting.data.LineData;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by dosssik on 12/11/16.
 */

public interface IDescription extends MvpView {
    void setChartData(LineData selectedLineData);

    void setEmptyState();
}
