package lv.edi.SmartWearProcessing;
import org.ejml.*;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

/**
 * Created by Richards on 01.12.2015..
 */
public class Calibration {
    /**
     * Function for fitting data to elipsoid for sensor data calibration.
     * @param inputData DenseMatrix64F n x 3 matrix, (n rows). each n is one measurement
     */
    public static void ellipsoidFitCalibration(DenseMatrix64F inputData){
        CommonOps.extract(inputData, 0, inputData.numRows, 0, 1);

    }
}
