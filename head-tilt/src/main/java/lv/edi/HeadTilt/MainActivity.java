package lv.edi.HeadTilt;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private Timer t = new Timer();
    private HeadTiltView htView;
    double r=0.5;
    double phi=0;
    double phiIncr=0.017;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        htView = (HeadTiltView)findViewById(R.id.headtiltview);
        t.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                phi+=phiIncr;
                htView.setPolarLocation(r, phi);
            }
        }, 15, 15);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
