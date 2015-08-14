package lv.edi.HeadAndPosture;

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

import java.util.ArrayList;
import java.util.Vector;

import lv.edi.BluetoothLib.*;

import lv.edi.SmartWearProcessing.Sensor;

/**
 * Created by Richards on 18/06/2015.
 */
public class HeadAndPostureApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener, BluetoothEventListener, ProcessingEventListener, BatteryLevelEventListener {
    public static final int  BATTERY_LEVEL_UPDATE = 45;
    final int REQUEST_ENABLE_BT = 2;
    SharedPreferences sharedPrefs;
    BluetoothAdapter btAdapter;
    BluetoothDevice btDevice;
    boolean vibrateFeedback;
    boolean alertFeedback;
    float threshold;
    int numberOfSensors;
    int headSensorIndex;
    int nrOfCols, nrOfRows;
    int refRow, refCol;
    int batteryPacketIndex;
    boolean startSensorLeft;

    BluetoothService btService;
    Vector<Sensor> sensors; ;

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

        // OBTAIN SETTING VALUES
        String numberOfSensorsS = sharedPrefs.getString("pref_nr_sensors", "21");
        int numberOfSensors = Integer.parseInt(numberOfSensorsS);
        String headSensorIndexS = sharedPrefs.getString("pref_head_idx", "20");
        int headSensorIndex = Integer.parseInt(headSensorIndexS);
        String nrOfColsS = sharedPrefs.getString("pref_nr_cols", "4");
        nrOfCols = Integer.parseInt(nrOfColsS);
        String nrOfRowsS = sharedPrefs.getString("pref_nr_rows", "5");
        nrOfRows = Integer.parseInt(nrOfRowsS);
        String batteryPacketIndexS = sharedPrefs.getString("pref_battery_idx", "21");
        batteryPacketIndex = Integer.parseInt(batteryPacketIndexS);
        startSensorLeft = sharedPrefs.getBoolean("pref_start_left", true);
        alertFeedback = sharedPrefs.getBoolean("pref_vibrate", false);
        vibrateFeedback = sharedPrefs.getBoolean("pref_alert", false);
        String refRowS = sharedPrefs.getString("pref_ref_row_idx", "2");
        refRow = Integer.parseInt(refRowS);
        String refColS = sharedPrefs.getString("pref_ref_col_idx", "2");

        sensors = new Vector<Sensor>(numberOfSensors);
        sensors.setSize(numberOfSensors);
        for(int i=0; i<numberOfSensors; i++){
            sensors.set(i,new Sensor(i, true));
        }
        batteryLevel = new BatteryLevel();
        batteryLevel.registerListener(this);
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
            Log.d("PREFERENCES", "vibrate set " + vibrateFeedback);
        }

        if(key.equals("pref_alert")){
            alertFeedback = sharedPreferences.getBoolean("pref_alert", false);
            Log.d("PREFERENCES", "alert set "+alertFeedback);
        }

        if(key.equals("pref_nr_sensors")){
            String numberOfSensorsS = sharedPreferences.getString("pref_nr_sensors", "21");
            numberOfSensors = Integer.parseInt(numberOfSensorsS);
            sensors.setSize(numberOfSensors);
            for(int i=0; i<numberOfSensors; i++){
                sensors.set(i,new Sensor(i, true));
            }
            Log.d("PREFERENCES", "sensor nr changed "+numberOfSensors);

        }

        if(key.equals("pref_head_idx")){
            String headSensorIndexS = sharedPreferences.getString("pref_head_idx", "20");
            headSensorIndex = Integer.parseInt(headSensorIndexS);

            Log.d("PREFERENCES", "head sensor idx changed "+headSensorIndex);
        }

        if(key.equals("pref_nr_cols")){
            String nrOfColsS = sharedPreferences.getString("pref_nr_cols", "4");
            nrOfCols = Integer.parseInt(nrOfColsS);

            Log.d("PREFERENCES", "number of columns changed "+nrOfCols);
        }

        if(key.equals("pref_nr_rows")){
            String nrOfRowsS = sharedPreferences.getString("pref_nr_rows", "5");
            nrOfRows = Integer.parseInt(nrOfRowsS);

            Log.d("PREFERENCES", "number of rows changed "+nrOfRows);
        }

        if(key.equals("pref_start_left")){
            startSensorLeft = sharedPreferences.getBoolean("pref_start_left", true);
            Log.d("PREFERENCES", "starting sensor from left: "+startSensorLeft);
        }

        if(key.equals("pref_battery_idx")){
            String batteryPacketIndexS = sharedPreferences.getString("pref_battery_idx", "21");
            batteryPacketIndex = Integer.parseInt(batteryPacketIndexS);

            Log.d("PREFERENCES", "battery packet index changed "+batteryPacketIndex);
        }

        if(key.equals("pref_ref_row_idx")){
            String refRowS = sharedPreferences.getString("pref_ref_row_idx", "2");
            refRow = Integer.parseInt(refRowS);

            Log.d("PREFERENCES", "reference row changed "+refRow);
        }

        if(key.equals("pref_ref_col_idx")){
            String refColS = sharedPreferences.getString("pref_ref_col_idx", "2");
            refCol = Integer.parseInt(refColS);

            Log.d("PREFERENCES", "reference col changed  "+refCol);
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

    @Override
    public void onProcessingResult(ProcessingResult result){
        htView.onProcessingResult(result);
        if(result.isOverThreshold()){
            if(vibrateFeedback) {
                vibrator.vibrate(100);
            }
            if(alertFeedback && ! mp.isPlaying()){
                mp.start();
            }
        }
    }
}