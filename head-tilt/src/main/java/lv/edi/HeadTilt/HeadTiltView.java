package lv.edi.HeadTilt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by Richards on 16/06/2015.
 * Class that represents view for representing view feedback
 */
public class HeadTiltView extends View{
    private double coordX;
    private double coordY;
    private Bitmap smiley;

    public HeadTiltView(Context context) {
        super(context);
        smiley = BitmapFactory.decodeResource(getResources(), R.drawable.smileyfacetransp);
        smiley = Bitmap.createScaledBitmap(smiley, smiley.getWidth()/2, smiley.getHeight()/2, false);
    }
    public HeadTiltView(Context context, AttributeSet attrs){
        super(context, attrs);
        smiley = BitmapFactory.decodeResource(getResources(), R.drawable.smileyfacetransp);
        smiley = Bitmap.createScaledBitmap(smiley, smiley.getWidth() / 2, smiley.getHeight() / 2, false);
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

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int bcx = smiley.getWidth()/2;
        int bcy = smiley.getHeight()/2;
        int cx = canvas.getWidth()/2;
        int cy = canvas.getHeight()/2;
        int range = Math.max(cx,cy);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setAlpha(0);
        canvas.drawPaint(paint);
        canvas.drawBitmap(smiley, (int)(coordX*range+cx-bcx), (int)(coordY*range+cy-bcy), null);
    }
}
