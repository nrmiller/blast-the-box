package net.nicksneurons.blastthebox.utils

import org.joml.Matrix4f
import org.joml.Vector2i

class Camera2D: Camera {

    override fun getViewMatrix(): Matrix4f {
        TODO("Not yet implemented")
    }

    override fun getProjectionMatrix(screenSize: Vector2i): Matrix4f {
        return Matrix4f() // todo ortho2D vs ortho???
    }
}