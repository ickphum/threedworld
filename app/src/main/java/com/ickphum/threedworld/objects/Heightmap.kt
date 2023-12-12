package com.ickphum.threedworld.objects

import android.graphics.Bitmap
import android.graphics.Color
import android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.GL_UNSIGNED_SHORT
import android.opengl.GLES20.glBindBuffer
import android.opengl.GLES20.glDrawElements
import com.ickphum.threedworld.data.IndexBuffer
import com.ickphum.threedworld.data.VertexBuffer
import com.ickphum.threedworld.programs.HeightmapShaderProgram




class Heightmap( bitmap: Bitmap ) {
    private val POSITION_COMPONENT_COUNT = 3
    private val width = bitmap.width
    private val height = bitmap.height
    private val numElements = calculateNumElements()
    private lateinit var vertexBuffer: VertexBuffer
    private lateinit var indexBuffer: IndexBuffer

    init {
        if ( width * height > 65536 )
            throw RuntimeException( "Heightmap is too large for the index buffer." )
        vertexBuffer = VertexBuffer(loadBitmapData(bitmap));
        indexBuffer = IndexBuffer(createIndexData());
    }

    private fun loadBitmapData(bitmap: Bitmap): FloatArray {
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        bitmap.recycle()
        val heightmapVertices = FloatArray(width * height * POSITION_COMPONENT_COUNT)
        var offset = 0
        for (row in 0 until height) {
            for (col in 0 until width) {
                val xPosition = col.toFloat() / (width - 1).toFloat() - 0.5f
                val yPosition = Color.red(pixels[row * height + col]).toFloat() / 255f
                val zPosition = row.toFloat() / (height - 1).toFloat() - 0.5f
                heightmapVertices[offset++] = xPosition
                heightmapVertices[offset++] = yPosition
                heightmapVertices[offset++] = zPosition
            }
        }
        return heightmapVertices
    }

    private fun calculateNumElements(): Int {
        return (width - 1) * (height - 1) * 2 * 3
    }

    private fun createIndexData(): ShortArray {
        val indexData = ShortArray(numElements)
        var offset = 0
        for (row in 0 until height - 1) {
            for (col in 0 until width - 1) {
                val topLeftIndexNum = (row * width + col).toShort()
                val topRightIndexNum = (row * width + col + 1).toShort()
                val bottomLeftIndexNum = ((row + 1) * width + col).toShort()
                val bottomRightIndexNum = ((row + 1) * width + col + 1).toShort()
                // Write out two triangles.
                indexData[offset++] = topLeftIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = topRightIndexNum
                indexData[offset++] = topRightIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = bottomRightIndexNum
            }
        }
        return indexData
    }

    fun bindData(heightmapProgram: HeightmapShaderProgram) {
        vertexBuffer.setVertexAttribPointer(
            0,
            heightmapProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.getBufferId())
        glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_SHORT, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }
}