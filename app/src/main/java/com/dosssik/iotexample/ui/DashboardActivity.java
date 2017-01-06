package com.dosssik.iotexample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dosssik.iotexample.R;
import com.dosssik.iotexample.model.RPiResponseModel;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getSupportFragmentManager()
                .findFragmentByTag(
                        DateChooseFragment.class.getSimpleName()) == null) {
            showDataChooseFragment();
        }


    }

    private void showDataChooseFragment() {

        Fragment fragment = DateChooseFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.activity_dashboard_container,
                        fragment,
                        DateChooseFragment.class.getSimpleName()
                )
                .commit();
    }

    public void showDescriptionFragment(ArrayList<RPiResponseModel> data) {

        if (data.size() != 0
                && getSupportFragmentManager().findFragmentByTag(DescriptionFragment.class.getName()) == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left,
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right)
                    .replace(R.id.activity_dashboard_container, DescriptionFragment.newInstance(data))
                    .addToBackStack(DateChooseFragment.class.getName())
                    .commit();
        } else {
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
        }
    }
}
