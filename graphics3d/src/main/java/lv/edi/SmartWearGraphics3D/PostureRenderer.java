package lv.edi.SmartWearGraphics3D;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Richards on 15.08.2015..
 */
public class PostureRenderer implements GLSurfaceView.Renderer{
    private Context mContext;
    volatile public double viewPointVector[] = {0, -40, 0};
    volatile public double cameraUpVector[] = {0, 0, 1};
    private PostureSurfaceModel surfaceModel;

    public PostureRenderer(Context context)
    {
        mContext = context;
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        //DO STUFF

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity(); //7
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);

        GLU.gluLookAt(gl, (float) viewPointVector[0], (float) viewPointVector[1], (float) viewPointVector[2], 0, 0, 0, (float) cameraUpVector[0], (float) cameraUpVector[1], (float) cameraUpVector[2]);
        Log.d("RENDERING", "in on draw frame surface model is " + surfaceModel);
        if(surfaceModel!=null) {
            surfaceModel.draw(gl);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 140);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        //DO STUFF
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST);

        gl.glClearColor(1,1,1,1);
        //gl.glEnable(GL10.GL_CULL_FACE);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }

    /**
     * sets model for renderer to render
     * @param model PostureSurfaceModel object
     */
    public void setSurfaceModel(PostureSurfaceModel model){
        this.surfaceModel = model;
    }
}