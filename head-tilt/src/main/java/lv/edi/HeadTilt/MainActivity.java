package lv.edi.HeadTilt;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    final int REQUEST_ENABLE_BT = 1;
    private HeadTiltApplication application;
    private Menu optionsMenu;
    private Timer t = new Timer();
    private HeadTiltView htView;
    double r=0.5;
    double phi=0;
    double phiIncr=0.017;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (HeadTiltApplication)getApplication();

        application.btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(application.btAdapter == null){
            Toast.makeText(this, "Sorry, but device does not support bluetooth conection \n Application will now close", Toast.LENGTH_SHORT).show();
            finish();
        }
        // check if bluetooth is turned on
        if(!application.btAdapter.isEnabled()){
            // intnet to open activity, to turn on bluetooth if bluetooth no turned on
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT); //start activity for result
        }

        String btAddress = application.sharedPrefs.getString("pref_bluetooth_target", "none");
        if(btAddress.equals("none")){
            application.btDevice = null;
        } else{
            application.btDevice = application.btAdapter.getRemoteDevice(btAddress);
        }

        //create bluetooth service object and register event listener
        application.btService = new BluetoothService(application.sensors); // create service instance
        application.btService.registerBluetoothEventListener(application);
        // starting bluetooth service

        htView = (HeadTiltView)findViewById(R.id.headtiltview);
        t.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                phi+=phiIncr;
                htView.setPolarLocation(r, phi);
            }
        }, 15, 15);


    }
    @Override
    public void onResume(){
        super.onResume();

        application.uiHandler = new Handler(Looper.getMainLooper()){

            public void handleMessage(Message inputMessage){
                switch(inputMessage.what){
                    case BluetoothService.BT_CONNECTING:
                        Toast.makeText(getApplicationContext(), "Connecting BT device...", Toast.LENGTH_SHORT).show();
                        optionsMenu.findItem(R.id.action_bluetooth_connection_status).setIcon(R.drawable.loading);
                        break;
                    case BluetoothService.BT_CONNECTED:
                        Toast.makeText(getApplicationContext(), "Connected to BT device!", Toast.LENGTH_SHORT).show();
                        optionsMenu.findItem(R.id.action_bluetooth_connection_status).setIcon(R.drawable.check);
                        break;
                    case BluetoothService.BT_DISCONNECTED:
                        Toast.makeText(getApplicationContext(), "Not connected to BT device!", Toast.LENGTH_SHORT).show();
                        optionsMenu.findItem(R.id.action_bluetooth_connection_status).setIcon(R.drawable.not);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, HeadTiltPreferenceActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_bluetooth_connection_status){
            if(!application.btService.isConnecting()) {
                if (application.btService.isConnected()) {
                    application.btService.disconnectDevice();
                } else {
                    if (application.btDevice != null) {
                        application.btService.connectDevice(application.btDevice);
                    } else {
                        Toast.makeText(this, "Set target bluetooth device in settings!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode!=Activity.RESULT_OK){
                    Toast.makeText(this, "In order to use this application \n bluetooth must bet turned on!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

}
