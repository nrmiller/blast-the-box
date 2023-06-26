package net.nicksneurons.blastthebox.utils

import net.nicksneurons.blastthebox.client.Engine
import miller.util.jomlextensions.*
import net.nicksneurons.blastthebox.ecs.Transform
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f

interface Camera {

    // todo consider exposing view matrix as a transform object
//    val transform : Transform

    fun createViewMatrix() : Matrix4f
    fun createProjectionMatrix(): Matrix4f

    companion object {

        @JvmStatic fun Camera.createProjectionViewMatrix(): Matrix4f {
            val projectionMatrix = createProjectionMatrix()
            val viewMatrix = createViewMatrix()

            val projViewMatrix = Matrix4f()
            return projViewMatrix.mul(viewMatrix, projectionMatrix)
        }

        @JvmStatic fun Camera.createInverseProjectionViewMatrix(): Matrix4f {
            val projViewMatrix = createProjectionViewMatrix()
            return projViewMatrix.invert()
        }

        @JvmStatic fun Camera.screenToWorld2D(point: Vector2f): Vector2f {
            val invProjViewMatrix = createInverseProjectionViewMatrix()
            val result = invProjViewMatrix.transform(Vector4f(point.x, (Engine.instance.height - point.y), 0.0f, 0.0f))
            return result.toVector2f()
        }

        @JvmStatic fun Camera.screenToWorld3D(point: Vector3f): Vector3f {
            val invProjViewMatrix = createInverseProjectionViewMatrix()
            val result = invProjViewMatrix.transform(Vector4f(point.x, (Engine.instance.height - point.y), point.z, 0.0f))
            return result.toVector3f()
        }
    }
    
}