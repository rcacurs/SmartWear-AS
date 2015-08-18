package lv.edi.HeadAndPosture;

import android.util.Log;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Richards on 18.08.2015..
 */
public class DataLogger {
    private File file;
    private boolean isLogging = false;
    private HeadTiltProcessingService headTiltProcessingService;
    private PostureProcessingService potureProcessingService;
    private int timeInterval;

    public DataLogger(String folder, HeadTiltProcessingService headTiltProcessingService, PostureProcessingService postureProcessingService, float sampleRate){
        this.headTiltProcessingService=headTiltProcessingService;
        this.potureProcessingService=postureProcessingService;
        timeInterval = (int)(1/sampleRate*1000);
        Calendar c = Calendar.getInstance();
        String am_pm;

        Log.d("LOGGING", "loggedData-"+c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR)+"--"+c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE)+".csv");
    }
}
