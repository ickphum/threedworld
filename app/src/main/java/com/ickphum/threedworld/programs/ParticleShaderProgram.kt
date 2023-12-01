package com.ickphum.threedworld.programs

import android.content.Context
import android.opengl.GLES20.GL_TEXTURE0
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.glActiveTexture
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform1f
import android.opengl.GLES20.glUniform1i
import android.opengl.GLES20.glUniformMatrix4fv


class ParticleShaderProgram(context: Context)
    : ShaderProgram( context, "particle_vertex_shader.glsl",
        "particle_fragment_shader.glsl" )
{
    // Uniform locations
    private val uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
    private val uTimeLocation = glGetUniformLocation(program, U_TIME)
    private val uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

    // Retrieve attribute locations for the shader program.
    private val aPositionLocation = glGetAttribLocation(program, A_POSITION)
    private val aColorLocation = glGetAttribLocation(program, A_COLOR)
    private val aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR)
    private val aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME)

    fun setUniforms(matrix: FloatArray?, elapsedTime: Float, textureId: Int) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform1f(uTimeLocation, elapsedTime)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(uTextureUnitLocation, 0)
    }
    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }

    fun getDirectionVectorAttributeLocation(): Int {
        return aDirectionVectorLocation
    }

    fun getParticleStartTimeAttributeLocation(): Int {
        return aParticleStartTimeLocation
    }
}