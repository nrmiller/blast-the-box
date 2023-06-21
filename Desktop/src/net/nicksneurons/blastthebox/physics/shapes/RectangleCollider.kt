package net.nicksneurons.blastthebox.physics.shapes

import net.nicksneurons.blastthebox.ecs.components.Transform
import org.joml.Vector2f
import kotlin.math.*

class RectangleCollider(var size: Vector2f = Vector2f(1.0f), var isCentered: Boolean = false) : Collider2D() {

    val transform
        get() = entity.get()?.transform ?: Transform()

    val left
        get() = if (isCentered) (transform.position.x - size.x / 2) else (transform.position.x)

    val right
        get() = if (isCentered) (transform.position.x + size.x / 2) else (transform.position.x + size.x)

    val bottom
        get() = if (isCentered) (transform.position.y - size.y / 2) else (transform.position.y)

    val top
        get() = if (isCentered) (transform.position.y + size.y / 2) else (transform.position.y + size.y)

    override fun intersectsWith(other: Collider2D): Boolean {

        if (other is RectangleCollider) {
            // todo If the rectangle are rotated, we need to use the Separating Axis theorem:
            // https://www.codeproject.com/Articles/15573/2D-Polygon-Collision-Detection
            // this will work for any two convex polygons

            val xOverlap = max(0.0f, min(right, other.right) - max(left, other.left))
            val yOverlap = max(0.0f, min(top, other.top) - max(bottom, other.bottom))
            return (xOverlap > 0 && yOverlap > 0)
        } else {
            return false
        }
    }

    override fun intersectsWith(point: Vector2f): Boolean {
        return (point.x in left..right && point.y in bottom..top)
    }

}