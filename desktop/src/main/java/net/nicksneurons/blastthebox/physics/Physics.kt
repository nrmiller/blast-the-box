package net.nicksneurons.blastthebox.physics

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.StaticBody3D


class Physics {

    companion object {
        @JvmStatic fun <T : Entity, R : Entity> checkCollisionsWith(entity: T, others: Iterable<R>, onCollision : (other : R) -> Unit) {
            val collider = entity.getComponent<StaticBody3D>()?.shape ?: return

            for (other in others) {
                if (other.isMarkedForDeletion)
                    continue
                if (entity.isMarkedForDeletion)
                    break

                val otherCollider = other.getComponent<StaticBody3D>()?.shape
                if (otherCollider != null) {
                    if (otherCollider.intersectsWith(collider)) {
                        onCollision(other)
                    }
                }
            }
        }
    }
}