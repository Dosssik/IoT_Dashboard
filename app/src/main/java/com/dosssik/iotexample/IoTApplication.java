package com.dosssik.iotexample;

import android.app.Application;

import com.dosssik.iotexample.di.IoTComponent;

import lombok.Getter;

/**
 * Created by dosssik on 12/9/16.
 */

public class IoTApplication extends Application {

    @Getter
    private static IoTComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = IoTComponent.Initiazer.init(this);
        component.inject(this);
    }
}
