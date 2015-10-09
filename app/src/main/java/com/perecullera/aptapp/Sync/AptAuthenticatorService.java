package com.perecullera.aptapp.Sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by perecullera on 9/10/15.
 */
public class AptAuthenticatorService extends Service{
    // Instance field that stores the authenticator object
    private AptAuthenticator mAuthenticator;

    @Override
    public  void onCreate(){
        // Create a new authenticator object
        mAuthenticator = new AptAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
