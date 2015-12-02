package lv.edi.SmartWearCalibJavaApp;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import lv.edi.SmartWearProcessing.Calibration;

public class Main {

    static double data[] =   {-1.0667,0.32706,0.079934,0.93373,1.0826,-0.94848,0.35032,1.0061,0.41149,-0.029006,-0.65091,0.67698,0.18245,0.25706,0.85773,-1.5651,-0.94438,-0.69116,-0.084539,-1.3218,0.44938,1.6039,0.92483,0.10063,0.098348,4.9849e-05,0.82607,0.041374,-0.054919,0.53616,-0.73417,0.91113,0.89789,-0.030814,0.59458,-0.13194,0.23235,0.3502,-0.1472,0.42639,1.2503,1.0078,-0.37281,0.92979,-2.1237,-0.23645,0.23976,-0.50459,2.0237,-0.69036,-1.2706,-2.2584,-0.65155,-0.38258,2.2294,1.1921,0.64868,0.33756,-1.6118,0.82573,1.0001,-0.024462,-1.0149,-1.6642,-1.9488,-0.47107,-0.59003,1.0205,0.13702,-0.27806,0.86172,-0.29186,0.42272,0.0011621,0.30182,-1.6702,-0.070837,0.39993,0.47163,-2.4863,-0.92996,-1.2128,0.58117,-0.17683,0.06619,-2.1924,-2.1321,0.65236,-2.3193,1.1454
    };
    public static void main(String args[]) throws InterruptedException{
        System.out.print("Text\r");
        System.out.println("New");
        DenseMatrix64F inputData = new DenseMatrix64F(30, 3, true, data);
        DenseMatrix64F W_inverted = new DenseMatrix64F(3, 3);
        DenseMatrix64F offsets = new DenseMatrix64F(3,1);
        Calibration.init();
        long tick1= System.currentTimeMillis();
        Calibration.ellipsoidFitCalibration(inputData, offsets, W_inverted);
        long tick2=System.currentTimeMillis();
        System.out.printf("elips fit time: %d [ms]\n", (tick2-tick1));
        System.out.println("Offsets: "+offsets.toString());
        System.out.println("Scaling: "+W_inverted);

//        for(int i=0; i<10; i++){
//            System.out.print(i+"\r");
//            Thread.sleep(1000);
//        }

    }
}
