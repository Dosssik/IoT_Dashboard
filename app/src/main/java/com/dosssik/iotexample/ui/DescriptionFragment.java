package com.dosssik.iotexample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dosssik.iotexample.R;
import com.dosssik.iotexample.model.RPiResponseModel;
import com.dosssik.iotexample.presenters.DescriptionPresenter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dosssik on 12/11/16.
 */

public class DescriptionFragment extends MvpFragment<IDescription, DescriptionPresenter> {

    private static final String DATA_ARGS = "DATA_ARGS";

    @Bind(R.id.description_fragment_chart)
    LineChart lineChart;

    public static DescriptionFragment newInstance(ArrayList<RPiResponseModel> data) {

        Bundle args = new Bundle();
        args.putSerializable(DATA_ARGS, data);
        DescriptionFragment fragment = new DescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public DescriptionPresenter createPresenter() {
        DescriptionPresenter presenter = new DescriptionPresenter();
        List<RPiResponseModel> data = (List<RPiResponseModel>) getArguments().getSerializable(DATA_ARGS);
        presenter.setData(data);
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.description_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        lineChart.setPinchZoom(true);
        lineChart.setData(presenter.getTestData());
        // now modify viewport
        lineChart.setVisibleXRangeMaximum(20); // allow 20 values to be displayed at once on the x-axis, not more
        lineChart.moveViewToX(10);
//        lineChart.setViewPortOffsets(5,15,5,15);
    }
}
