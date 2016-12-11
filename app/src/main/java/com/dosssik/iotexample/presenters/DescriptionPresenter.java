package com.dosssik.iotexample.presenters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.dosssik.iotexample.R;
import com.dosssik.iotexample.model.RPiResponseModel;
import com.dosssik.iotexample.ui.DashboardActivity;
import com.dosssik.iotexample.ui.IDescription;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dosssik on 12/11/16.
 */

public class DescriptionPresenter extends MvpBasePresenter<IDescription> {

    private List<RPiResponseModel> data;

    public void setData(List<RPiResponseModel> data) {
        this.data = data;
    }

    public LineData getTestData() {
        List<Entry> chartData = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            RPiResponseModel model = data.get(i);
            if (model.getTemperature() != 0) {
                chartData.add(new Entry(i+1, (float) model.getTemperature()));
            }
        }

        LineDataSet lineDataSet = new LineDataSet(chartData, "TestLabel");

        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.RED);

        return new LineData(lineDataSet);

    }
}
