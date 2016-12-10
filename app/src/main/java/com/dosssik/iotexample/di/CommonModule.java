package com.dosssik.iotexample.di;

import android.content.Context;

import com.dosssik.iotexample.managers.DatabaseManager;
import com.dosssik.iotexample.IoTApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dosssik on 12/9/16.
 */

@Module
public class CommonModule {

    private IoTApplication ioTApplication;

    public CommonModule(IoTApplication ioTApplication) {
        this.ioTApplication = ioTApplication;
    }

    @Provides
    @Singleton
    public IoTApplication provideApplication() {
        return ioTApplication;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext(IoTApplication ioTApplication) {
        return ioTApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    public DatabaseManager provideDatabaseHelper(Context context) {
        return new DatabaseManager(context);
    }
}
