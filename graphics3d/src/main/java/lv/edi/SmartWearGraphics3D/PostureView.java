package lv.edi.SmartWearGraphics3D;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Richards on 15.08.2015..
 * Class that extends gl sufrace view to draw posture models
 */

public class PostureView extends GLSurfaceView {

    /**
     * sonctructor of view
     * @param context
     */
    PostureRenderer renderer;
    public PostureView(Context context){
        this(context, null);
    }

    public PostureView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public PostureView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs);

        setEGLContextClientVersion(2);
        renderer = new PostureRenderer(context);
        setRenderer(renderer);
    }

    /**
     * Sets posture model to be drawed in this view
     * @param model model to be drawed
     */
    public void setPostureModel(PostureSurfaceModel model){
        renderer.setSurfaceModel(model);
    }
}
