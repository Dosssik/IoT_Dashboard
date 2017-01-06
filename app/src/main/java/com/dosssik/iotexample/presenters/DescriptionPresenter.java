package com.dosssik.iotexample.presenters;

import android.graphics.Color;

import com.dosssik.iotexample.model.RPiResponseModel;
import com.dosssik.iotexample.ui.DescriptionFragment;
import com.dosssik.iotexample.ui.IDescription;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dosssik on 12/11/16.
 */

public class DescriptionPresenter extends MvpBasePresenter<IDescription> {

    private List<RPiResponseModel> data;
    private Map<DescriptionFragment.DataType, LineData> chartData = new HashMap<>();

    public void setData(List<RPiResponseModel> data) {
        this.data = data;
    }

    public void onTabSelected(DescriptionFragment.DataType selectedType, CharSequence label) {

        LineData selectedLineData = chartData.get(selectedType);

        if (selectedLineData == null) {
            LineData resultData = new LineData();

            List<Entry> entryList = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                RPiResponseModel model = data.get(i);
                double value = getModelValue(model, selectedType);
                if (value != 0) {
                    entryList.add(new Entry(i+1, (float) value));
                }
            }

            LineDataSet lineDataSet = new LineDataSet(entryList, label.toString());

            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setCircleColor(Color.RED);

            resultData.addDataSet(lineDataSet);

            selectedLineData = resultData;
            chartData.put(selectedType, selectedLineData);
        }

        LineDataSet dataSet = (LineDataSet) selectedLineData.getDataSetByIndex(0);

        if (dataSet.getEntryCount() == 0) {
            showViewEmptyState();
        } else {
            showData(selectedLineData);
        }
    }

    private void showViewEmptyState() {
        getView().setEmptyState();
    }

    private void showData(LineData selectedLineData) {
        if (getView() != null) {
            getView().setChartData(selectedLineData);
        }
    }

    private double getModelValue(RPiResponseModel model, DescriptionFragment.DataType selectedType) {
        double value;
        switch (selectedType) {
            case cpuTemp:
                value = model.getCpuTemperature();
                break;
            case cpuUsage:
                value = model.getCpuUsage();
                break;
            case humidity:
                value = model.getHumidity();
                break;
            case pressure:
                value = model.getPressure();
                break;
            case temperature:
                value = model.getTemperature();
                break;
            default:
                value = 0;
        }
        return value;
    }
}
