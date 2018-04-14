package com.mgr.arapp.vuforiaexample.libgdx.utils;

import android.app.Activity;
import android.opengl.GLES20;
import android.util.Log;

import com.mgr.arapp.vuforiaexample.SampleApplication.SampleAppRenderer;
import com.vuforia.CameraCalibration;
import com.vuforia.CameraDevice;
import com.vuforia.Device;
import com.vuforia.Renderer;
import com.vuforia.State;
import com.vuforia.TrackableResult;
import com.vuforia.TrackerManager;
import com.vuforia.Vec2F;
import com.vuforia.Vuforia;

/**
 * Vuforia renderer, responsible for video background rendering, tracking and position calculations
 */
public class VuforiaRenderer {

    private static final String LOGTAG = "VuforiaRenderer";

    public static String lastTrackableName = "";

    private AppSession vuforiaAppSession;

    private Renderer mRenderer;

    private SampleAppRenderer mSampleAppRenderer;

    public boolean mIsActive = false;

    public float fieldOfViewRadians;


    public VuforiaRenderer(AppSession session)
    {
        vuforiaAppSession = session;
    }


    // Called when the surface changed size.
    public void onSurfaceChanged(int width, int height)
    {
        Log.d(LOGTAG, "GLRenderer.onSurfaceChanged");

        // Call Vuforia function to handle render surface size changes:
        vuforiaAppSession.onSurfaceChanged(width, height);
    }


    // Function for initializing the renderer.
    public void initRendering(Activity activity)
    {
        Log.d(LOGTAG, "GLRenderer.initRendering");

        mRenderer = Renderer.getInstance();

        mSampleAppRenderer = new SampleAppRenderer(activity, Device.MODE.MODE_AR, false, 0.01f, 5f);
        mSampleAppRenderer.initRendering();

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, Vuforia.requiresAlpha() ? 0.0f
                : 1.0f);


        // Call Vuforia function to (re)initialize rendering after first use
        // or after OpenGL ES context was lost (e.g. after onPause/onResume):
        vuforiaAppSession.onSurfaceCreated();
    }


    // The render function.
    public TrackableResult[] processFrame()
    {
        if (!mIsActive)
            return null;

//        State state = mRenderer.begin();
//        mRenderer.renderVideoBackground();
        State state;
        state = TrackerManager.getInstance().getStateUpdater().updateState();
        mRenderer.begin(state);
        mSampleAppRenderer.renderVideoBackground(true);


        // did we find any trackables this frame?
        TrackableResult[] results = new TrackableResult[state.getNumTrackableResults()];
        Log.d(LOGTAG, "*********REsults:" + results.length);

        for (int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++)
        {
            //remember trackable
            TrackableResult result = state.getTrackableResult(tIdx);
            lastTrackableName = result.getTrackable().getName();
            results[tIdx] = result;

            //calculate filed of view
            CameraCalibration calibration = CameraDevice.getInstance().getCameraCalibration();
            Vec2F size = calibration.getSize();
            Vec2F focalLength = calibration.getFocalLength();
            fieldOfViewRadians = (float) (2 * Math.atan(0.5f * size.getData()[0] / focalLength.getData()[0]));
        }

        mRenderer.end();

        return results;
    }

}
