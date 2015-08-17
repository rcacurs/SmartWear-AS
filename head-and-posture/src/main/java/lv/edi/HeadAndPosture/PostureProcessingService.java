package lv.edi.HeadAndPosture;

import android.util.Log;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import lv.edi.SmartWearProcessing.Segment;
import lv.edi.SmartWearProcessing.Sensor;
import lv.edi.SmartWearProcessing.SensorDataProcessing;

/**
 * Created by Richards on 17.08.2015..
 */
public class PostureProcessingService {

    private int timeInterval = 10;

    private Vector<Vector<Segment>> savedStateSegments;
    private Vector<Vector<Segment>> savedStateSegmentsInitial;
    private Vector<Vector<Segment>> currentStateSegments;
    private Vector<Vector<Sensor>> sensors;
    private Vector<Vector<Float>> distances;

    private boolean isProcessing;
    private int referenceRow;
    private int referenceCol;
    private float threshold;
    private Timer timer;
    private boolean isStateSaved = false;

    /**
     * constructor for posture processing service
     */
    public PostureProcessingService(
                                    Vector<Vector<Segment>>currenStateSegments,
                                    Vector<Vector<Sensor>> sensors,
                                    int refRow, int refCol,
                                    float threshold){
        this.currentStateSegments = currenStateSegments;
        this.sensors = sensors;
        this.referenceRow = refRow;
        this.referenceCol = refCol;
        this.threshold = threshold;
    }
    public void setReferenceState(Vector<Vector<Segment>>savedStateSegments, Vector<Vector<Segment>>savedStateSegmentInitial){
        this.savedStateSegments = savedStateSegments;
        this.savedStateSegmentsInitial = savedStateSegmentsInitial;
        isStateSaved = true;

    }

    public boolean isStateSaved(){
        return isStateSaved;
    }
    /** starts processing service
     *
     */

    public void startProcessing(){
        timer = new Timer();
        isProcessing = true;
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Segment.setAllSegmentOrientations(currentStateSegments, sensors);
                Segment.setSegmentCenters(currentStateSegments, (short)referenceRow, (short)referenceCol);
                Log.d("PROCESSING", "POSTURE_PROCESSIN_PROCESSED");
            }
        }, 0, timeInterval);

    }

    /** stops processing
     *
     */
    public void stopProcessing(){
        if(timer!=null){
            isProcessing = false;
            timer.cancel();
        }
    }

    public boolean isProcessing(){
        return isProcessing;
    }


}
