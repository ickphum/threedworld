package com.ickphum.threedworld.objects

import com.ickphum.threedworld.data.VertexArray
import com.ickphum.threedworld.objects.ObjectBuilder.DrawCommand
import com.ickphum.threedworld.programs.ColorShaderProgram
import com.ickphum.threedworld.util.Geometry
import com.ickphum.threedworld.util.Geometry.Cylinder

private const val POSITION_COMPONENT_COUNT = 3
class Puck(public val radius: Float, public val height: Float, numPointsAroundPuck: Int) {

    private val generatedData = ObjectBuilder.createPuck(
        Cylinder(
            Geometry.Point(0f, 0f, 0f), radius, height
        ), numPointsAroundPuck
    )
    private val vertexArray: VertexArray = VertexArray(generatedData.vertexData)
    private val drawList: List<DrawCommand> = generatedData.drawList

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        for (drawCommand in drawList) {
            drawCommand.draw()
        }
    }

}

