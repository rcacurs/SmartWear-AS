package lv.edi.HeadTilt;

import android.util.Log;

/**
 * Created by Richards on 26/06/2015.
 */
public class BatteryLevel {

    private int previousAdcValue=0;
    private int adcValue=0;
    private float referenceVoltageP = 2.5f;
    private float referenceVoltageN = 0.0f;
    private int resolution=10;
    private float inputScaling=0.5f;

    // curve fir coeefficients
    float p1 = 18.56f;
    float p2 = -253.2f;
    float p3 = 1291;
    float p4 = -2918;
    float p5 = 2466;
    float q1 = -15.64f;
    float q2 = 92;
    float q3 = -241;
    float q4 = 237.2f;

    /**
     * return integer representing raw ADC value
     */
    public int getRawADCValue(){
        return adcValue;
    }

    public float getADCVoltage(){
        return (float)(adcValue*(referenceVoltageP-referenceVoltageN)/(Math.pow(2, resolution)-1)+referenceVoltageN);
    }

    public float getBatteryVoltage(){
        return getADCVoltage()/inputScaling;
    }

    public void updateAdcValue(int adcValue){
        this.adcValue = adcValue;
        this.previousAdcValue = adcValue;
        Log.d("BATTERY_LEVEL", " adc value: " + adcValue);
        Log.d("BATTERY_LEVEL", " battery voltage" + getBatteryVoltage());
        Log.d("BATTERY_LEVEL", " battery percentage " + batteryVoltageLevelToPercent(getBatteryVoltage()));
    }

    public float batteryVoltageLevelToPercent(float x){

        return (float)((p1*Math.pow(x, 4)+p2*Math.pow(x,3)+p3*Math.pow(x,2)+p4*x+p5)/
                (Math.pow(x,4)+q1*Math.pow(x,3)+q2*Math.pow(x,2)+q3*x+q4));
    }

}
