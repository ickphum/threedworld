package com.ickphum.threedworld.programs

import android.content.Context
import android.opengl.GLES20.glUseProgram
import com.ickphum.threedworld.util.ShaderHelper

open class ShaderProgram( context: Context, vertexShaderResourceId: Int, fragmentShaderResourceId: Int) {
    protected val U_MATRIX = "u_Matrix"
    protected val U_TEXTURE_UNIT = "u_TextureUnit"

    // Attribute constants
    protected val A_POSITION = "a_Position"
    protected val A_COLOR = "a_Color"
    protected val U_TIME = "u_Time"
    protected val A_DIRECTION_VECTOR = "a_DirectionVector"
    protected val A_PARTICLE_START_TIME = "a_ParticleStartTime"

    protected var program =
        ShaderHelper.buildProgram(context, vertexShaderResourceId, fragmentShaderResourceId)

    open fun useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program)
    }
}
