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
    private var previousX: Float = 0f
    private var previousY: Float = 0f
    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = Renderer( context )

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        // Render the view only when there is a change in the drawing data.
//        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

    }
    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                previousX = x
                previousY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = previousX - x
                val deltaY = previousY - y
                queueEvent(Runnable {
                    renderer.handleTouchDrag( -deltaX, -deltaY)
                })
                previousX = x
                previousY = y
            }
        }

        return true
    }
}
