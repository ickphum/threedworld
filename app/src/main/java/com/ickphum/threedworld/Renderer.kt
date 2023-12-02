package com.ickphum.threedworld

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20.GL_BLEND
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_ONE
import android.opengl.GLES20.glBlendFunc
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDisable
import android.opengl.GLES20.glEnable
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.translateM
import android.util.Log
import com.ickphum.threedworld.objects.ParticleShooter
import com.ickphum.threedworld.objects.ParticleSystem
import com.ickphum.threedworld.objects.Skybox
import com.ickphum.threedworld.programs.ParticleShaderProgram
import com.ickphum.threedworld.programs.SkyboxShaderProgram
import com.ickphum.threedworld.util.Geometry
import com.ickphum.threedworld.util.TextureHelper
import javax.microedition.khronos.opengles.GL10


private const val TAG = "3DRenderer"

class Renderer(context: Context) : GLSurfaceView.Renderer {

    private val context = context;

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)
    private val invertedViewProjectionMatrix = FloatArray(16)

    private lateinit var particleProgram: ParticleShaderProgram
    private lateinit var particleSystem: ParticleSystem
    private lateinit var redParticleShooter: ParticleShooter
    private lateinit var greenParticleShooter: ParticleShooter
    private lateinit var blueParticleShooter: ParticleShooter
    private var globalStartTime: Long = 0

    lateinit var particleDirection: Geometry.Vector

    private var particleTexture = 0

    private lateinit var skyboxProgram: SkyboxShaderProgram
    private lateinit var skybox: Skybox
    private var skyboxTexture = 0

    private var xRotation = 0f
    private var yRotation = 0f

    override fun onSurfaceCreated(glUnused: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        particleProgram = ParticleShaderProgram(context);
        particleSystem = ParticleSystem(10000);
        globalStartTime = System.nanoTime();

        particleDirection = Geometry.Vector(0f, 0.5f, 0f)

        val angleVarianceInDegrees = 5f;
        val speedVariance = 1f;

        redParticleShooter = ParticleShooter(
            Geometry.Point(-0.8f, 0f, 0f),
            particleDirection,
            Color.rgb(255, 50, 5),
            angleVarianceInDegrees,
            speedVariance
        )
        greenParticleShooter = ParticleShooter(
            Geometry.Point(0f, 0.5f, 0f),
            particleDirection,
            Color.rgb(25, 255, 25),
            angleVarianceInDegrees,
            speedVariance
        )
        blueParticleShooter = ParticleShooter(
            Geometry.Point(0.8f, 1f, 0f),
            particleDirection,
            Color.rgb(5, 50, 255),
            angleVarianceInDegrees,
            speedVariance
        )

        particleTexture = TextureHelper.loadTexture(context, R.drawable.particle_texture)

        val i = R.drawable.particle_texture

        skyboxProgram = SkyboxShaderProgram( context )
        skybox = Skybox()
        skyboxTexture = TextureHelper.loadCubeMap( context,
            intArrayOf(R.drawable.left, R.drawable.right,
                R.drawable.bottom, R.drawable.top,
                R.drawable.front, R.drawable.back))
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        MatrixHelper.perspectiveM(
            projectionMatrix, 45f,
            width.toFloat() / height.toFloat(), 1f, 10f
        )
    }

    override fun onDrawFrame(glUnused: GL10?) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT)

        drawSkybox()
        drawParticles()
    }

    private fun drawSkybox() {
        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        skyboxProgram.useProgram()
        skyboxProgram.setUniforms(viewProjectionMatrix, skyboxTexture)
        skybox.bindData(skyboxProgram)
        skybox.draw()
    }
    private fun drawParticles() {
        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f
        redParticleShooter.addParticles(particleSystem, currentTime, 1)
        greenParticleShooter.addParticles(particleSystem, currentTime, 1)
        blueParticleShooter.addParticles(particleSystem, currentTime, 1)

        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f)
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f)
        translateM(viewMatrix, 0, 0f, -1.5f, -5f)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        glEnable(GL_BLEND)
        glBlendFunc(GL_ONE, GL_ONE)

        particleProgram.useProgram()
        particleProgram.setUniforms(viewProjectionMatrix, currentTime, particleTexture)
        particleSystem.bindData(particleProgram)
        particleSystem.draw()
        glDisable(GL_BLEND)
    }

    fun handleTouchDrag(deltaX: Float, deltaY: Float) {
        Log.d( TAG, "Move by $deltaX $deltaY")
        xRotation += deltaX / 16f;
        yRotation += deltaY / 16f;
        if (yRotation < -90)
            yRotation = -90f;
        else if (yRotation > 90)
            yRotation = 90f;
    }

}
