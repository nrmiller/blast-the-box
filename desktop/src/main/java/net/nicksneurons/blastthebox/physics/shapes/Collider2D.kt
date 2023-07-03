package net.nicksneurons.blastthebox.physics.shapes

import net.nicksneurons.blastthebox.ecs.Entity
import org.joml.Vector2f
import java.lang.ref.WeakReference

abstract class Collider2D {
    // needed for grabbing a reference to the transform
    // WeakReference is used to prevent cyclical dependencies
    // Entity -> Component -> Collider --(Weak)--> Entity
    var entity = WeakReference<Entity?>(null)

    abstract fun intersectsWith(other: Collider2D): Boolean
    abstract fun intersectsWith(point: Vector2f): Boolean
}

