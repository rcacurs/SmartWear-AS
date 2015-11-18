package lv.edi.YesNo;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Vector;

import lv.edi.BluetoothLib.BatteryLevel;
import lv.edi.BluetoothLib.BatteryLevelEventListener;
import lv.edi.BluetoothLib.BluetoothEventListener;
import lv.edi.BluetoothLib.BluetoothService;
import lv.edi.SmartWearProcessing.Sensor;

/**
 * Created by Richards on 18.11.2015..
 */
public class YesNoApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener, BluetoothEventListener, BatteryLevelEventListener {
    final int NUMBER_OF_SENSORS = 1;
    static final int BATTERY_LEVEL_UPDATE=45;
    static final int BATTERY_PACKET_INDEX=1;
    BluetoothService btService;
    BluetoothAdapter btAdapter;
    BluetoothDevice btDevice;
    Handler uiHandler;
    SharedPreferences sharedPrefs;
    BatteryLevel batteryLevel;
    boolean vibrateFeedback=false;
    boolean alertFeedback=false;
    Vector<Sensor> sensors;

    YesNoProcessingService processingService;

    public void onCreate(){
        super.onCreate();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        sensors = new Vector<Sensor>(NUMBER_OF_SENSORS, BATTERY_PACKET_INDEX);
        sensors.setSize(NUMBER_OF_SENSORS);
        for(int i=0; i<NUMBER_OF_SENSORS; i++){
            sensors.set(i,new Sensor(i, true));
        }
        processingService = new YesNoProcessingService(sensors.get(0));
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

        if(key.equals("pref_threshold")){
            String thresholdSetting = sharedPreferences.getString("pref_threshold", "0.7");
            float thresholdSettingf = Float.parseFloat(thresholdSetting);
//            htView.setThreshold(thresholdSettingf);
//            if(processingService!=null){
////                processingService.setThreshold(thresholdSettingf);
//            }
        }

        if(key.equals("pref_vibrate")){
            vibrateFeedback = sharedPreferences.getBoolean("pref_vibrate", false);
            Log.d("PREFERENCES", "vibrate set " + vibrateFeedback);
        }

        if(key.equals("pref_alert")){
            alertFeedback = sharedPreferences.getBoolean("pref_alert", false);
            //Log.d("PREFERENCES", "alert set "+alertFeedback);
        }
    }

    // battery level listeners
    public void onBatteryLevelChange(BatteryLevel bLevel){
        uiHandler.obtainMessage(BATTERY_LEVEL_UPDATE).sendToTarget();
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
