package lv.edi.HeadTilt;

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
        return inputScaling*getADCVoltage();
    }

    public void updateAdcValue(int adcValue){
        this.adcValue = adcValue;
    }

}
