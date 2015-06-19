package lv.edi.HeadTilt;

import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lv.edi.SmartWearProcessing.Sensor;

/**
 * Created by Richards on 19/06/2015.
 * Class provides processing service for head tilt application
 * processing is done at specified time interval
 */
public class HeadTiltProcessingService{

    private Sensor sensor;
    private float[] referenceState = new float[3];
    private int timeInterval=30;
    private boolean isProcessing=false;
    private Timer timer;
    private ProcessingEventListener listener;

    /**
     * @param - Sensor object from which input data is taken. must be not null!
     */
    public HeadTiltProcessingService(Sensor sensor){
        this.sensor=sensor;
    }

    /**
     * Allows specifying time interval at which computation is done
     * @param sensor sensor object from which input data is taken. must be not null!
     * @param timeInterval time interval in ms for computation period
     */
    public HeadTiltProcessingService(Sensor sensor, int timeInterval){
        this(sensor);
        this.timeInterval=timeInterval;
    }

    /**
     * Sets accelerometer data for reference state
     * @param reference float[3] array containing reference sensor data
     */
    public void setReference(float[] reference){
        if(reference.length==3){
            referenceState[0]=reference[0];
            referenceState[1]=reference[1];
            referenceState[2]=reference[2];
        }
    }

    /**
     * sets event listener for processing events
     */
    public void setProcessingEventListener(ProcessingEventListener listener){
        this.listener = listener;
    }

    /** starts processing service
     *
     */
    public void startProcessing(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Log.d("PROCESSING_SERVICE", "processed!");
            }
        }, 0, timeInterval);

    }

    /** stops processing
     *
     */
    public void stopProcessing(){
        if(timer!=null){
            timer.cancel();
        }
    }

    /**
     *
     * @return true if curren service is running
     */
    public boolean isProcessing(){
        return isProcessing;
    }


}
