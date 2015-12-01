package lv.edi.SmartWearCalibJavaApp;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

public class Main {
    public static void main(String args[]){

        double[] testMatrix = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        DenseMatrix64F testData = new DenseMatrix64F(4,3, true, testMatrix);
        DenseMatrix64F oneCol = new DenseMatrix64F(testData.numRows, 1);
        CommonOps.extractColumn(testData, 0, oneCol);
        System.out.print(oneCol.toString());
        System.out.println("Works");
    }
}
