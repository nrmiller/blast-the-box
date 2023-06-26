package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.ecs.Component
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.physics.shapes.Collider3D
import java.lang.ref.WeakReference

class StaticBody3D(var shape: Collider3D) : Component() {

    override fun onAttached(entity: Entity) {
        super.onAttached(entity)

        shape.entity = WeakReference(entity)
    }
}