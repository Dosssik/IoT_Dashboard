package com.dosssik.iotexample.di;

import com.dosssik.iotexample.ui.DashboardActivity;
import com.dosssik.iotexample.IoTApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dosssik on 12/9/16.
 */
@Singleton
@Component(modules = CommonModule.class)
public interface IoTComponent {

    void inject(IoTApplication ioTApplication);

    void inject(DashboardActivity activity);

    final class Initiazer {
        public static IoTComponent init(IoTApplication application) {
            return DaggerIoTComponent.builder()
                    .commonModule(new CommonModule(application))
                    .build();
        }
    }
}
