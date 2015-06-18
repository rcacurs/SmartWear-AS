package lv.edi.HeadTilt;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

/**
 * Created by Richards on 18/06/2015.
 */
public class HeadTiltApplication extends Application {
    BluetoothAdapter btAdapter;
    BluetoothDevice btDevice;
    @Override
    public void onCreate(){
        super.onCreate();
    }
}
