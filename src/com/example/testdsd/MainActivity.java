package com.example.testdsd;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends Activity {

    // Tag for logging
    private static final String TAG = "BluetoothActivity";
    private final String mac = "20:15:05:22:24:03";
    BluetoothThread blue;
    Handler handleWrite;

    public void connectButtonPressed(View v) {
        Log.v(TAG, "Connect button pressed.");

        // Only one thread at a time
        if (blue != null) {
            Log.w(TAG, "Already connected!");
            return;
        }

        // Initialize the Bluetooth thread, passing in a MAC address
        // and a Handler that will receive incoming messages
        blue = new BluetoothThread(mac, new Handler() {

            @Override
            public void handleMessage(Message message) {

                String s = (String) message.obj;

                // Do something with the message
                if (s.equals("CONNECTED")) {
                    TextView tv = (TextView) findViewById(R.id.statusText);
                    tv.setText("Connected");
                    //-----------------------------------------
                    //connected hoilei likha pathano start kori
                          

                } else if (s.equals("DISCONNECTED")) {
                    TextView tv = (TextView) findViewById(R.id.statusText);
       
                    tv.setText("Disconnected");
                } else if (s.equals("CONNECTION FAILED")) {
                    TextView tv = (TextView) findViewById(R.id.statusText);
                    tv.setText("Connection Failed");
                    blue = null;
                } else {
                 
                }
            }
        });

        // Get the handler that is used to send messages
        handleWrite = blue.getWriteHandler();

        // Run the thread
        blue.start();

        TextView tv = (TextView) findViewById(R.id.statusText);
        tv.setText("Connecting...");
        
        Message msg = Message.obtain();
        msg.obj = "on";
    	handleWrite.sendMessage(msg);
        
        
        
        
    }

    /**
     * Kill the Bluetooth thread.
     */
    public void disconnectButtonPressed(View v) {
    	Message msg = Message.obtain();
        msg.obj = "0";
    	handleWrite.sendMessage(msg);
    	
    	
    	
        Log.v(TAG, "Disconnect button pressed.");

        if(blue != null) {
            blue.interrupt();
            blue = null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
   

    /**
     * Kill the thread when we leave the activity.
     */
    protected void onPause() {
        super.onPause();

        if(blue != null) {
            blue.interrupt();
            blue = null;
        }
    }
}