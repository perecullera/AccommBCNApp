package com.perecullera.aptapp.Sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by perecullera on 9/10/15.
 */
public class AptSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static AptSyncAdapter sAptSyncAdapter = null;

    @Override
    public void onCreate(){
        Log.d("AptSyncService", "onCreate - AptSyncService");
        synchronized (sSyncAdapterLock) {
            if (sAptSyncAdapter == null) {
                sAptSyncAdapter = new AptSyncAdapter(getApplicationContext(), true);
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return sAptSyncAdapter.getSyncAdapterBinder();
    }
}
