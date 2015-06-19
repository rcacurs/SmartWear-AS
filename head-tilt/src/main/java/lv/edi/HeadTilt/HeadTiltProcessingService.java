package lv.edi.HeadTilt;

import android.util.Log;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import lv.edi.SmartWearProcessing.Sensor;
import lv.edi.SmartWearProcessing.SensorDataProcessing;

/**
 * Created by Richards on 19/06/2015.
 * Class provides processing service for head tilt application
 * processing is done at specified time interval
 */
public class HeadTiltProcessingService{

    private Sensor sensor;
    private float[] referenceState = new float[3];
    private float[] currentSens1 = new float[3];
    private float[] crossVertical = new float[3];
    private float[] crossHorizontal = new float[3];
    private float[] crossVerticalN = new float[3];
    private float[] crossHorizontalN = new float[3];
    private int timeInterval=10;
    private boolean isProcessing=false;
    private boolean isStateSaved=false;
    private Timer timer;
    private ProcessingEventListener listener;
    private boolean isXZplane=false;
    private float XX;
    private float YY;

    // locators for processing
    float[] tempSens = new float[3];
    float[] tempRef = new float[3];

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

            isStateSaved=true;

            if(referenceState[0]>Math.abs(referenceState[1])){
                isXZplane=true;
            } else{
                isXZplane=false;
            }
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
                currentSens1 = sensor.getAccRawNorm();

                if(isXZplane){
                    tempSens[0]=currentSens1[0];
                    tempSens[1]=0;
                    tempSens[2]=currentSens1[2];

                    tempRef[0]=referenceState[0];
                    tempRef[1]=0;
                    tempRef[2]=referenceState[2];

                    SensorDataProcessing.crossProduct(tempRef, tempSens, crossVertical);

                } else{
                    tempSens[0]=0;
                    tempSens[1]=currentSens1[1];
                    tempSens[2]=currentSens1[2];

                    tempRef[0]=0;
                    tempRef[1]=referenceState[1];
                    tempRef[2]=referenceState[2];

                    SensorDataProcessing.crossProduct(tempRef, tempSens, crossVertical);
                }
                crossVerticalN = Arrays.copyOf(crossVertical, crossVertical.length);
                SensorDataProcessing.normalizeVector(crossVerticalN);

                tempSens[0]=currentSens1[0];
                tempSens[1]=currentSens1[1];
                tempSens[2]=0;

                tempRef[0]=referenceState[0];
                tempRef[1]=referenceState[1];
                tempRef[2]=0;

                SensorDataProcessing.crossProduct(tempSens, tempRef, crossHorizontal);
                crossHorizontalN = Arrays.copyOf(crossHorizontal, crossHorizontal.length);
                SensorDataProcessing.normalizeVector(crossHorizontalN);

                SensorDataProcessing.absVector(crossHorizontal);
                SensorDataProcessing.absVector(crossVertical);

                XX=-(float)(Math.asin(SensorDataProcessing.dotProduct(crossHorizontal, crossHorizontalN))*180/Math.PI)/90;
                YY=(float)(Math.asin(SensorDataProcessing.dotProduct(crossVertical, crossVerticalN))*180/Math.PI)/90;

                if(listener!=null){
                    float[] result = new float[2];
                    result[0]=XX;
                    result[1]=YY;
                    listener.onProcessingResult(result);
                    result = null;
                    Log.d("PROCESSING_SERVICE", "SENDING TO LISTENER");
                }
                Log.d("PROCESSING_SERVICE", "XX: "+XX+" YY: "+YY);
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

    /**
     *
     * @return returns true if this process has saved reference accelerometer vector
     */
    public boolean isStateSaved(){
        return isStateSaved;
    }


}
