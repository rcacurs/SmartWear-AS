package lv.edi.HeadTilt;

/**
 * Created by Richards on 18/06/2015.
 */
public interface BluetoothEventListener {

    void onBluetoothDeviceConnecting();
    void onBluetoothDeviceConnected();
    void onBluetoothDeviceDisconnected();
}
