package lv.edi.HeadAndPosture;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Vector;

import lv.edi.SmartWearGraphics3D.PostureRenderer;
import lv.edi.SmartWearGraphics3D.PostureView;
import lv.edi.SmartWearProcessing.Segment;

public class PostureActivity extends Activity {
    private PostureView postureView;
    private PostureRenderer renderer;

    Vector<Vector<Segment>> segments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture);

        postureView = (PostureView)findViewById(R.id.posture_view);

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
