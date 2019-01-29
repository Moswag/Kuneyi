package com.android.cytex.broadcastreceivers;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityDetector {

    Context context;

    public ConnectivityDetector(Context context) {
        this.context=context;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null){
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if(info!=null){
                if(info.getState()==NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
