package net.nicksneurons.blastthebox.physics.shapes

import net.nicksneurons.blastthebox.ecs.Transform
import org.joml.Vector3f
import kotlin.math.max
import kotlin.math.min

class BoxCollider(var size: Vector3f = Vector3f(1.0f), var isCentered: Boolean = false) : Collider3D() {

    val transform
        get() = entity.get()?.transform ?: Transform()

    val left
        get() = if (isCentered) (transform.getWorldPosition().x - size.x / 2) else (transform.getWorldPosition().x)

    val right
        get() = if (isCentered) (transform.getWorldPosition().x + size.x / 2) else (transform.getWorldPosition().x + size.x)

    val bottom
        get() = if (isCentered) (transform.getWorldPosition().y - size.y / 2) else (transform.getWorldPosition().y)

    val top
        get() = if (isCentered) (transform.getWorldPosition().y + size.y / 2) else (transform.getWorldPosition().y + size.y)

    val back
        get() = if (isCentered) (transform.getWorldPosition().z - size.z / 2) else (transform.getWorldPosition().z)

    val front
        get() = if (isCentered) (transform.getWorldPosition().z + size.z / 2) else (transform.getWorldPosition().z + size.z)

    override fun intersectsWith(other: Collider3D): Boolean {

        if (other is BoxCollider) {

            val xOverlap = max(0.0f, min(right, other.right) - max(left, other.left))
            val yOverlap = max(0.0f, min(top, other.top) - max(bottom, other.bottom))
            val zOverlap = max(0.0f, min(front, other.front) - max(back, other.back))

            return (xOverlap > 0 && yOverlap > 0 && zOverlap > 0)
        } else {
            return false
        }
    }

    override fun intersectsWith(point: Vector3f): Boolean {
        return (point.x in left..right && point.y in bottom..top && point.z in back..front)
    }
}
