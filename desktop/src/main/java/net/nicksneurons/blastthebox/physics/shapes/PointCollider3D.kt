package net.nicksneurons.blastthebox.physics.shapes

import miller.util.jomlextensions.toVector3f
import net.nicksneurons.blastthebox.ecs.Transform
import org.joml.Vector3f

class PointCollider3D() : Collider3D() {

    var offset = Vector3f()

    val transform
        get() = entity.get()?.transform ?: Transform()

    val position: Vector3f
        get() = transform.getWorldPosition().toVector3f().add(offset)

    override fun intersectsWith(other: Collider3D): Boolean {
        return if (other is BoxCollider) {
            other.intersectsWith(position)
        } else if (other is PointCollider3D) {
            intersectsWith(other.position)
        } else {
            false
        }
    }

    override fun intersectsWith(point: Vector3f): Boolean {
        return position == point
    }
}