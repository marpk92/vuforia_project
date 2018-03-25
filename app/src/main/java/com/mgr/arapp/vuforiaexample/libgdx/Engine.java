package com.mgr.arapp.vuforiaexample.libgdx;

import android.app.Activity;
import android.util.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.mgr.arapp.vuforiaexample.libgdx.utils.VuforiaRenderer;

/**
 * Instance of libgdx Game class responsible for rendering 3D content over augmented reality.
 */
public class Engine extends Game {

    private FPSLogger fps;
    private VuforiaRenderer vuforiaRenderer;
    private Activity mActivity;

    public Engine(VuforiaRenderer vuforiaRenderer, Activity activity) {
        this.vuforiaRenderer = vuforiaRenderer;
        this.mActivity = activity;
    }

    @Override
    public void create () {
        Display mDisplay = new Display(vuforiaRenderer);
        setScreen(mDisplay);
        vuforiaRenderer.initRendering(mActivity);
        fps = new FPSLogger();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Log.d("ENGINE", "Resize: "+width+"x"+height);
        vuforiaRenderer.onSurfaceChanged(width, height);
    }

    @Override
    public void render () {
        super.render();
        fps.log();
    }

}
