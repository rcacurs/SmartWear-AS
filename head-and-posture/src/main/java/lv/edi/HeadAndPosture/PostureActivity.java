package lv.edi.HeadAndPosture;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Vector;

import lv.edi.SmartWearGraphics3D.PostureRenderer;
import lv.edi.SmartWearGraphics3D.PostureSurfaceModel;
import lv.edi.SmartWearGraphics3D.PostureView;
import lv.edi.SmartWearProcessing.Segment;
import lv.edi.SmartWearProcessing.Sensor;
import lv.edi.SmartWearProcessing.SensorDataProcessing;

public class PostureActivity extends Activity {
    private PostureView postureView;
    private PostureRenderer renderer;

    PostureSurfaceModel currentStateModel;

    private Resources res;
    private HeadAndPostureApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture);

        postureView = (PostureView)findViewById(R.id.posture_view);
        this.application = (HeadAndPostureApplication)getApplication();
        res=getResources();

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(application.postureProcessingService==null){
            application.postureProcessingService=new PostureProcessingService(application.segmentsCurrent,
                    application.sensorGrid, application.refRow, application.refCol, application.postureThreshold);
        }

        currentStateModel = new PostureSurfaceModel(application.segmentsCurrent);
        postureView.setPostureModel(currentStateModel);
        
    }

    public void onClickSave(View view){
        if(application.btService.isConnected()) {

            application.segmentsSaved.get(application.refRow).get(application.refCol).center[0]=0;
            application.segmentsSaved.get(application.refRow).get(application.refCol).center[1]=0;
            application.segmentsSaved.get(application.refRow).get(application.refCol).center[2]=0;

            application.segmentsSavedInitial.get(application.refRow).get(application.refCol).center[0]=0;
            application.segmentsSavedInitial.get(application.refRow).get(application.refCol).center[1]=0;
            application.segmentsSavedInitial.get(application.refRow).get(application.refCol).center[2]=0;

            Segment.setAllSegmentOrientations(application.segmentsSaved, application.sensorGrid);
            Segment.setAllSegmentOrientations(application.segmentsSavedInitial, application.sensorGrid);

            Segment.setSegmentCenters(application.segmentsSaved, (short) application.refRow, (short) application.refCol);
            Segment.setSegmentCenters(application.segmentsSavedInitial, (short)application.refRow, (short)application.refCol);

            application.postureProcessingService.setReferenceState(application.segmentsSaved, application.segmentsSavedInitial);



            application.setIsStateSaved(true);

            Toast.makeText(this, res.getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, res.getString(R.string.toast_must_connect_bt), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickStart(View view){
        ToggleButton button = (ToggleButton)view;
        if(button.isChecked()){
            if(application.postureProcessingService.isStateSaved()) {
                application.postureProcessingService.startProcessing();
            } else{
                button.setChecked(false);
                Toast.makeText(this, res.getString(R.string.toast_save_state), Toast.LENGTH_SHORT).show();
            }
        } else{
            application.postureProcessingService.stopProcessing();
        }
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
