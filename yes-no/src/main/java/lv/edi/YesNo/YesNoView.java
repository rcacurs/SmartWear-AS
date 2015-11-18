package lv.edi.YesNo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Richards on 17.11.2015..
 */
public class YesNoView extends View {

    private int canvasWidth;
    private int canvasHeight;
    private float mRelPosX=0; // marker relative position
    private float mRelPosY=0; // marker relative position
    private float relativeRadius=0.7f;
    private Paint paint = new Paint();
    private Bitmap smiley;
    private Bitmap sadface;
    private int scx;          // center coordinate in marker coordinates
    private int scy;          // cetner coordinate in marker coordinates

    public void initYesNoView(){
        smiley = BitmapFactory.decodeResource(getResources(), R.drawable.happyface500);
        sadface = BitmapFactory.decodeResource(getResources(), R.drawable.sadface500);
        smiley = Bitmap.createScaledBitmap(smiley, smiley.getWidth() / 4, smiley.getHeight() / 4, false);
        sadface = Bitmap.createScaledBitmap(sadface, sadface.getWidth() / 4, sadface.getHeight() / 4, false);
        scx=smiley.getWidth()/2;
        scy=smiley.getHeight()/2;
    }
    public YesNoView(Context context) {
        super(context);
        setWillNotDraw(false);
        initYesNoView();
    }

    public YesNoView(Context context, AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);
        initYesNoView();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvasWidth=canvas.getWidth();
        canvasHeight=canvas.getHeight();

        int cx = canvasWidth/2;
        int cy = canvasHeight/2;

        // draw no circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setAlpha(60);
        canvas.drawCircle(canvasWidth/4, canvasHeight/2, canvasWidth*relativeRadius/4, paint);

        // draw yes circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setAlpha(60);
        canvas.drawCircle(3*canvasWidth/4, canvasHeight/2, canvasWidth*relativeRadius/4, paint);

        canvas.drawBitmap(smiley, (int) (cx + mRelPosX * cx - scx), (int) (cy + mRelPosY * cx - scy), null);

    }

    public void setMarkerPosition(float[] newPosition){
        mRelPosX=newPosition[0];
        mRelPosY=newPosition[1];
        postInvalidate();
    }
    public void setRadius(float radius){
        this.relativeRadius = radius;
        postInvalidate();
    }
}