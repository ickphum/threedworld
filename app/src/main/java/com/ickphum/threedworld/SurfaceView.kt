package com.ickphum.threedworld

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import kotlinx.coroutines.Runnable

private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f
private const val TAG = "SurfaceView"

class SurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: com.ickphum.threedworld.Renderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = Renderer( context )

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        // Render the view only when there is a change in the drawing data.
//        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

    }

}
