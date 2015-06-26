package lv.edi.HeadTilt;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import lv.edi.SmartWearProcessing.Sensor;

/**
 * Created by Richards on 18/06/2015.
 */
public class HeadTiltApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener, BluetoothEventListener, ProcessingEventListener {
    final int NUMBER_OF_SENSORS = 1;
    final int REQUEST_ENABLE_BT = 2;
    SharedPreferences sharedPrefs;
    BluetoothAdapter btAdapter;
    BluetoothDevice btDevice;
    boolean vibrateFeedback;
    boolean alertFeedback;
    float threshold;
    BluetoothService btService;
    Sensor[] sensors = new Sensor[NUMBER_OF_SENSORS];
    BatteryLevel batteryLevel;
    Handler uiHandler;
    HeadTiltProcessingService processingService;
    HeadTiltView htView;
    Vibrator vibrator;
    MediaPlayer mp;

    @Override
    public void onCreate(){
        super.onCreate();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        for(int i=0; i<NUMBER_OF_SENSORS; i++){
            sensors[i]=new Sensor(i, true);
        }
        batteryLevel = new BatteryLevel();
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
            htView.setThreshold(thresholdSettingf);
            if(processingService!=null){
                processingService.setThreshold(thresholdSettingf);
            }
        }

        if(key.equals("pref_vibrate")){
            vibrateFeedback = sharedPreferences.getBoolean("pref_vibrate", false);
            Log.d("PREFERENCES", "vibrate set "+vibrateFeedback);
        }

        if(key.equals("pref_alert")){
            alertFeedback = sharedPreferences.getBoolean("pref_alert", false);
            Log.d("PREFERENCES", "alert set "+alertFeedback);
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

    @Override
    public void onProcessingResult(float[] coords, boolean isOverThreshold){
        htView.onProcessingResult(coords, isOverThreshold);
        if(isOverThreshold){
            if(vibrateFeedback) {
                vibrator.vibrate(100);
            }
            if(alertFeedback && ! mp.isPlaying()){
                mp.start();
            }
        }
    }
}
