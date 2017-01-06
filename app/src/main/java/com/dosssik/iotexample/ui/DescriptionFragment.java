package com.dosssik.iotexample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dosssik.iotexample.R;
import com.dosssik.iotexample.model.RPiResponseModel;
import com.dosssik.iotexample.presenters.DescriptionPresenter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.LineData;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dosssik on 12/11/16.
 */

public class DescriptionFragment extends MvpFragment<IDescription, DescriptionPresenter> implements IDescription {

    public enum DataType {
        cpuTemp,
        cpuUsage,
        humidity,
        pressure,
        temperature;
    }
    private static final String DATA_ARGS = "DATA_ARGS";

    @Bind(R.id.description_fragment_chart)
    LineChart lineChart;

    @Bind(R.id.description_fragment_placeholder)
    TextView placeHolder;

    @Bind(R.id.description_fragment_tab_layout)
    TabLayout tabLayout;

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

        configureLineChart();

        prepareTabs();

        requestData();
    }

    private void configureLineChart() {
        lineChart.setPinchZoom(true);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(false);
    }

    private void requestData() {
        TabLayout.Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        getPresenter().onTabSelected((DataType) selectedTab.getTag(), selectedTab.getText());
    }

    private void prepareTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cpu_temp).setTag(DataType.cpuTemp));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cpu_usage).setTag(DataType.cpuUsage));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.humidity).setTag(DataType.humidity));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pressure).setTag(DataType.pressure));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.temperature).setTag(DataType.temperature));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getPresenter().onTabSelected((DataType)tab.getTag(), tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void setChartData(LineData selectedLineData) {
        if (lineChart.getVisibility() == View.GONE) {
            lineChart.setVisibility(View.VISIBLE);
        }
        setContentVisibility(true);

        lineChart.setData(selectedLineData);
        lineChart.invalidate();
    }

    private void setContentVisibility(boolean visible) {
        lineChart.setVisibility(visible
                ? View.VISIBLE
                : View.GONE);

        placeHolder.setVisibility(visible
                ? View.GONE
                : View.VISIBLE);
    }

    @Override
    public void setEmptyState() {
        setContentVisibility(false);
    }
}
