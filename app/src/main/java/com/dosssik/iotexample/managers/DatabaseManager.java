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
import com.dosssik.iotexample.ui.DashboardActivity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dosssik on 12/3/16.
 */

public class DatabaseManager {

    private static final String DATASTORE_DIR = "data";
    private static final String DATABASE_NAME = "iotp_s0gomy_default_2016-09-27";
    private static final String LOG_TAG = "IOT_EXAPMLE";
    private final Context context;
    private final Handler mHandler;

    private Datastore datastore;
    private Replicator pullReplicator;

    private DashboardActivity dashboardActivity;

    public DatabaseManager(Context context) {
        this.context = context;

        File path = this.context.getApplicationContext().getDir(
                DATASTORE_DIR,
                Context.MODE_PRIVATE
        );

        DatastoreManager datastoreManager = DatastoreManager.getInstance(path);
        try {
            datastore = datastoreManager.openDatastore(DATABASE_NAME);
        } catch (DatastoreNotCreatedException e) {
            Log.e(LOG_TAG, "Unable to open Datastore");
            e.printStackTrace();
        }
        this.mHandler = new Handler(Looper.getMainLooper());

        reloadReplication();
    }

    private void reloadReplication() {
        stopReplicatior();

        URI uri = null;
        try {
            uri = createServerURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        pullReplicator = ReplicatorBuilder.pull().to(datastore).from(uri).build();
        pullReplicator.getEventBus().register(this);

    }

    private URI createServerURI() throws URISyntaxException {
        return new URI("https", "ac6c8f90-1477-484d-814b-1746ac8bfaab-bluemix"
                + ":"
                + "bc3d8eab31c1a6c60e22795d5e291caacf4a31f4d2f4da98809c213911e16351",
                "ac6c8f90-1477-484d-814b-1746ac8bfaab-bluemix.cloudant.com",
                443, "/" + "iotp_s0gomy_default_2016-09-27", null, null);    }

    public void stopReplicatior() {
        if (this.pullReplicator != null) {
            this.pullReplicator.stop();
        }
    }

    public void setListenner(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
    }
    @Subscribe
    public void complete(ReplicationCompleted rc) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (dashboardActivity != null) {
                    dashboardActivity.replicationComplete();
                }
            }
        });
    }

    /**
     * Calls the TodoActivity's replicationComplete method on the main thread,
     * as the error() callback will probably come from a replicator worker
     * thread.
     */
    @Subscribe
    public void error(ReplicationErrored re) {
        Log.e(LOG_TAG, "Replication error:", re.errorInfo.getException());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (dashboardActivity != null) {
                    dashboardActivity.replicationError();
                }
            }
        });
    }

    public Replicator.State getState() {
        return pullReplicator.getState();
    }

    public void startPulling() {
        pullReplicator.start();
    }

    public List<RPiResponseModel> getResult() {
        int quantity = datastore.getDocumentCount();
        List<RPiResponseModel> allData = new ArrayList<>();
        List<DocumentRevision> result = datastore.getAllDocuments(0, quantity, true);
        for (DocumentRevision rev : result) {
            RPiResponseModel rpiResponce = RPiResponseModel.fromRevision(rev);
            allData.add(rpiResponce);
        }
        return allData;
    }
}
