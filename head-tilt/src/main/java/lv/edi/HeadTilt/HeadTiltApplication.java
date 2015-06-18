package lv.edi.HeadTilt;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.widget.Toast;

import lv.edi.SmartWearProcessing.Sensor;

/**
 * Created by Richards on 18/06/2015.
 */
public class HeadTiltApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    final int NUMBER_OF_SENSORS = 1;
    BluetoothAdapter btAdapter;
    BluetoothDevice btDevice;
    BluetoothService btService;
    Sensor[] sensors = new Sensor[NUMBER_OF_SENSORS];

    @Override
    public void onCreate(){
        super.onCreate();
        btService = new BluetoothService(sensors); // create service instance
        //Toast.makeText(this, "Creating application object", Toast.LENGTH_LONG).show();
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
}
