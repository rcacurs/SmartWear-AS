package lv.edi.HeadTilt;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import lv.edi.SmartWearProcessing.Sensor;

/**
 * Created by Richards on 18/06/2015.
 */
public class HeadTiltApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener, BluetoothEventListener {
    final int NUMBER_OF_SENSORS = 1;
    final int REQUEST_ENABLE_BT = 2;
    SharedPreferences sharedPrefs;
    BluetoothAdapter btAdapter;
    BluetoothDevice btDevice;
    BluetoothService btService;
    Sensor[] sensors = new Sensor[NUMBER_OF_SENSORS];
    Handler uiHandler;
    HeadTiltProcessingService processingService;

    @Override
    public void onCreate(){
        super.onCreate();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        for(int i=0; i<NUMBER_OF_SENSORS; i++){
            sensors[i]=new Sensor(i, true);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("pref_bluetooth_target")){
            String btDeviceAddress = sharedPreferences.getString("pref_bluetooth_target", "none");
            if(btDeviceAddress.equals("none")){
                btDevice = null;
            } else{
                btAdapter.getRemoteDevice(btDeviceAddress);
            }
        }
    }


    // Bluetooth event listeners
    @Override
    public void onBluetoothDeviceConnecting(){
        uiHandler.obtainMessage(BluetoothService.BT_CONNECTING).sendToTarget();
    }
    @Override
    public void onBluetoothDeviceConnected(){
        uiHandler.obtainMessage(BluetoothService.BT_CONNECTED).sendToTarget();
    }
    @Override
    public void onBluetoothDeviceDisconnected(){
        uiHandler.obtainMessage(BluetoothService.BT_DISCONNECTED).sendToTarget();
    }
}
