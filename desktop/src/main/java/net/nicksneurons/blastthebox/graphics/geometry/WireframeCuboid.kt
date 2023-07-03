package net.nicksneurons.blastthebox.graphics.geometry

import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.GL_LINES
import org.lwjgl.opengl.GL11.glLineWidth

// todo this class needs better testing
class WireframeCuboid(
        val location : Vector3f = Vector3f(),
        val size : Vector3f = Vector3f(1.0f),
        val color : Vector3f = Vector3f(1.0f, 0.0f, 0.0f),
        val lineWidth: Float = 2.0f) : Primitive() {

    init {
        init()
    }

    override fun getRenderingMode(): Int {
        return GL_LINES
    }

    override fun getVertexArray(): FloatArray {
        val x: Float = location.x
        val y: Float = location.y
        val z: Float = location.z
        val w: Float = size.x
        val h: Float = size.y
        val d: Float = size.z
        return floatArrayOf(
                // FRONT
                x, y + h, z + d,
                x, y, z + d,
                x + w, y, z + d,
                x + w, y + h, z + d,

                // BACK
                x, y + h, z,
                x, y, z,
                x + w, y, z,
                x + w, y + h, z,
        )
    }

    override fun getColorArray(): FloatArray {
        return MutableList(8) { color }.flatMap {
            floatArrayOf(it.x, it.y, it.z).asIterable()
        }.toFloatArray()
    }

    override fun getIndexArray(): ShortArray {
        return shortArrayOf(
            0, 1, 1, 2, 2, 3, 3, 0, // FRONT
            4, 5, 5, 6, 6, 7, 4, // BACK
            0, 4, 1, 5, 2, 6, 3, 7, // MIDDLE CONNECTIONS
        )
    }

    override fun getTexCoordArray(): FloatArray {
        return MutableList(8) { Vector2f(0.0f, 0.0f) }.flatMap {
            floatArrayOf(it.x, it.y).asIterable()
        }.toFloatArray()
    }

    override fun getNormalArray(): FloatArray {
        return MutableList(8) { Vector2f(0.0f, 0.0f) }.flatMap {
            floatArrayOf(it.x, it.y).asIterable()
        }.toFloatArray()
    }

    override fun draw() {
        glLineWidth(lineWidth)
        super.draw()
    }
}