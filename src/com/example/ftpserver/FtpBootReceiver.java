package com.example.ftpserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
 
public class FtpBootReceiver extends BroadcastReceiver
{
    private final static String TAG = "FtpBootReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	
        String action = intent.getAction();
        Log.d(TAG, "action = " + action);
        
        Intent serviceIntent;
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "receive ACTION_BOOT_COMPLETED.");
           
            serviceIntent = new Intent(context, FtpService.class);        
            context.startService(serviceIntent);
        }  
    }

	 
}