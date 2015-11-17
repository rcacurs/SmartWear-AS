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
    private float optionRelativeRadius=0.7f;
    private Paint paint = new Paint();

    public YesNoView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public YesNoView(Context context, AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);

    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvasWidth=canvas.getWidth();
        canvasHeight=canvas.getHeight();

        // draw no circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setAlpha(60);
        canvas.drawCircle(canvasWidth/4, canvasHeight/2, canvasWidth*optionRelativeRadius/4, paint);

        // draw yes circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setAlpha(60);
        canvas.drawCircle(3*canvasWidth/4, canvasHeight/2, canvasWidth*optionRelativeRadius/4, paint);


    }
}
