package com.dosssik.iotexample.managers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
import com.cloudant.sync.datastore.DatastoreNotCreatedException;
import com.cloudant.sync.datastore.DocumentRevision;
import com.cloudant.sync.event.Subscribe;
import com.cloudant.sync.notifications.ReplicationCompleted;
import com.cloudant.sync.notifications.ReplicationErrored;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorBuilder;
import com.dosssik.iotexample.model.RPiResponseModel;
import com.dosssik.iotexample.presenters.DateChoosePresenter;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

/**
 * Created by dosssik on 12/3/16.
 */

public class DatabaseManager {

    public static final String DATASTORE_DIR = "data";
    public static final String DATABASE_NAME_PREFIX = "iotp_s0gomy_default_";
    public static final String DATABASE_NAME = "iotp_s0gomy_default_2016-09-27";
    private static final String LOG_TAG = "IOT_EXAPMLE";
    private final Context context;
    private final Handler mainThreadHandler;

    private Datastore datastore;
    private Replicator pullReplicator;

    private DatastoreManager datastoreManager;

    @Setter
    private DateChoosePresenter dateChoosePresenter;

    public DatabaseManager(Context context) {
        this.context = context;
        this.mainThreadHandler = new Handler(Looper.getMainLooper());

        File path = this.context.getApplicationContext().getDir(
                DATASTORE_DIR,
                Context.MODE_PRIVATE
        );

        datastoreManager = DatastoreManager.getInstance(path);
    }

    public ArrayList<RPiResponseModel> queryForSelectedDay(String selectedDate) {

        initDatastore(selectedDate);

        return fetchResults();
    }

    private void initDatastore(String selectedDate) {
        String databaseName = DATABASE_NAME_PREFIX + selectedDate;
        try {
            datastore = datastoreManager.openDatastore(databaseName);
        } catch (DatastoreNotCreatedException e) {
            Log.e(LOG_TAG, "Unable to open Datastore");
            e.printStackTrace();
        }
    }

    private ArrayList<RPiResponseModel> fetchResults() {
        int quantity = datastore.getDocumentCount();
        ArrayList<RPiResponseModel> allData = new ArrayList<>();
        List<DocumentRevision> result = datastore.getAllDocuments(0, quantity, true);
        for (DocumentRevision rev : result) {
            RPiResponseModel rpiResponce = RPiResponseModel.fromRevision(rev);
            allData.add(rpiResponce);
        }
        return allData;
    }

    private void reloadReplication() {
        stopReplication();

        URI uri = null;
//        try {
//            uri = createServerURI();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

        pullReplicator = ReplicatorBuilder.pull().to(datastore).from(uri).build();
        pullReplicator.getEventBus().register(this);

    }

    private URI createServerURI(String selectedDate) throws URISyntaxException {
        return new URI("https", "ac6c8f90-1477-484d-814b-1746ac8bfaab-bluemix"
                + ":"
                + "bc3d8eab31c1a6c60e22795d5e291caacf4a31f4d2f4da98809c213911e16351",
                "ac6c8f90-1477-484d-814b-1746ac8bfaab-bluemix.cloudant.com",
                443, "/" + DATABASE_NAME_PREFIX + selectedDate, null, null);
    }

    public void stopReplication() {
        if (this.pullReplicator != null) {
            this.pullReplicator.stop();
        }
    }

    @Subscribe
    public void complete(ReplicationCompleted rc) {
        mainThreadHandler.post(() -> {
            if (dateChoosePresenter != null) {
                dateChoosePresenter.onReplicationComplete();
            }
        });
    }

    @Subscribe
    public void error(ReplicationErrored re) {
        Log.e(LOG_TAG, "Replication error:", re.errorInfo.getException());
        mainThreadHandler.post(() -> {
            if (dateChoosePresenter != null) {
                dateChoosePresenter.showError(re.toString());
            }
        });
    }

    public Replicator.State getState() {
        return pullReplicator.getState();
    }

    public void startPulling() {
        pullReplicator.start();
    }


    public void pullSelectedDay(String selectedDate, DateChoosePresenter dateChoosePresenter) {
        URI uri = null;
        try {
            uri = createServerURI(selectedDate);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        initDatastore(selectedDate);

        pullReplicator = ReplicatorBuilder.pull().to(datastore).from(uri).build();
        pullReplicator.getEventBus().register(this);
        pullReplicator.start();
    }
}
