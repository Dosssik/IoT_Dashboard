package com.dosssik.iotexample.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.sync.replication.Replicator;
import com.dosssik.iotexample.managers.DatabaseManager;
import com.dosssik.iotexample.IoTApplication;
import com.dosssik.iotexample.R;
import com.dosssik.iotexample.model.RPiResponseModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DashboardActivity extends AppCompatActivity {

    Button buttonState, buttonGetResult, buttonStart;

    TextView stateTextView, resultTextView;

    @Inject
    DatabaseManager databaseHelper;

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        IoTApplication.getComponent().inject(this);

//        databaseHelper = new DatabaseManager(getApplicationContext());
        databaseHelper.setListenner(this);
        initView();
        setListenners();
    }

    private void initView() {
        buttonState = (Button) findViewById(R.id.button_state);
        buttonGetResult = (Button) findViewById(R.id.button_result);
        buttonStart = (Button) findViewById(R.id.button_start);

        stateTextView = (TextView) findViewById(R.id.state_text_view);
        resultTextView = (TextView) findViewById(R.id.resutl_text_view);

        lineChart = (LineChart) findViewById(R.id.chart);
        lineChart.setPinchZoom(true);
    }

    private void setListenners() {
        buttonState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Replicator.State state = databaseHelper.getState();
                stateTextView.setText("state " + state.toString());
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.startPulling();
            }
        });

        buttonGetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Entry> chartData = new ArrayList<>();
                List<RPiResponseModel> result = databaseHelper.getResult();

                for (int i = 0; i < result.size(); i++) {
                    RPiResponseModel model = result.get(i);
                    if (model.getTemperature() != 0) {
                        chartData.add(new Entry(i+1, (float) model.getTemperature()));
                    }
                }

                LineDataSet lineDataSet = new LineDataSet(chartData, "TestLabel");
//                lineDataSet.set
                lineDataSet.setColor(Color.BLACK);
                lineDataSet.setCircleColor(Color.RED);
                if (Utils.getSDKInt() >= 18) {
                    Drawable drawable = ContextCompat.getDrawable(DashboardActivity.this, R.drawable.fade_red);
                    lineDataSet.setFillDrawable(drawable);
                }
//                lineDataSet.setValueTextColor(android.R.color.holo_blue_light);

                LineData lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);
                lineChart.invalidate();

            }
        });
    }


    public void replicationComplete() {
//        reloadTasksFromModel();
        Toast.makeText(getApplicationContext(),
                "replicationComplete",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Called by TasksModel when it receives a replication error callback.
     * TasksModel takes care of calling this on the main thread.
     */
    public void replicationError() {
//        reloadTasksFromModel();
        Toast.makeText(getApplicationContext(),
                "replicationError",
                Toast.LENGTH_LONG).show();
    }

}
