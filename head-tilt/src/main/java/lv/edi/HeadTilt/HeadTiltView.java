package lv.edi.HeadTilt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.Log;
import android.view.View;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by Richards on 16/06/2015.
 * Class that represents view for representing view feedback
 */
public class HeadTiltView extends View implements ProcessingEventListener{
    private double coordX=0;
    private double coordY=0;
    private Bitmap smiley;
    private Bitmap sadface;
    private float threshold = 0.4f;
    private boolean isOverThreshold = false;
    Paint paint = new Paint();

    public HeadTiltView(Context context) {
        super(context);
        smiley = BitmapFactory.decodeResource(getResources(), R.drawable.happyface500);
        sadface = BitmapFactory.decodeResource(getResources(), R.drawable.sadface500);
        smiley = Bitmap.createScaledBitmap(smiley, smiley.getWidth() / 4, smiley.getHeight() / 4, false);
        sadface = Bitmap.createScaledBitmap(sadface, sadface.getWidth()/4, sadface.getHeight()/4, false);

    }
    public HeadTiltView(Context context, AttributeSet attrs){
        super(context, attrs);
        smiley = BitmapFactory.decodeResource(getResources(), R.drawable.happyface500);
        sadface = BitmapFactory.decodeResource(getResources(), R.drawable.sadface500);
        smiley = Bitmap.createScaledBitmap(smiley, smiley.getWidth() / 4, smiley.getHeight() / 4, false);
        sadface = Bitmap.createScaledBitmap(sadface, sadface.getWidth()/4, sadface.getHeight()/4, false);

    }

    /**
     * Method sets postion for HeadTilt feadback view element definec in polar coordinates.
     * method enforces redraw of view.
     * @param R - radius vector in range 0-1;
     * @param phi - angle defined in radians;
     */
    public void setPolarLocation(double R, double phi){
        ;
        coordX=R*Math.cos(phi);
        coordY=R*Math.sin(phi);
        postInvalidate();
    }

    /**
     * Sets location of the smiley
     * @param x - coordinate
     * @param y - coordinate
     */
    public void setLocation(float x, float y){
        coordX=x;
        coordY=y;
        postInvalidate();
    }

    /**
     * Method sets if head tilt is over threshold
     * @param overThreshold
     */
    public void setOverThreshold(boolean overThreshold){
        overThreshold = overThreshold;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int bcx = smiley.getWidth()/2;
        int bcy = smiley.getHeight()/2;
        int cx = canvas.getWidth()/2;
        int cy = canvas.getHeight()/2;
        int range = Math.max(cx,cy);

        float rad = (float)Math.sqrt(Math.pow(coordX,2)+Math.pow(coordY,2));
        if((rad+((float)bcx)/cx)>threshold){
            isOverThreshold=true;
        } else{
            isOverThreshold=false;
        }
        if(!isOverThreshold) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.GREEN);
            paint.setAlpha(60);
            canvas.drawPaint(paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6);
            paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(cx, cy, cx*(threshold), paint);
            canvas.drawBitmap(smiley, (int) (coordX * range + cx - bcx), (int) (coordY * range + cy - bcy), null);
        } else{
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setAlpha(75);
            canvas.drawPaint(paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6);
            paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(cx, cy, cx*(threshold), paint);
            canvas.drawBitmap(sadface, (int) (coordX * range + cx - bcx), (int) (coordY * range + cy - bcy), null);
        }
    }

    @Override
    public void onProcessingResult(float[] result){
        setLocation(result[0], result[1]);
        Log.d("PROCESSING_SERVICE", "RESULT");
    }
}
