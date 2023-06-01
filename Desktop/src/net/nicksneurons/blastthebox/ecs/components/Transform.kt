package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.ecs.Component
import org.joml.Vector3f

data class Transform(
        var position: Vector3f = Vector3f(),
        var rotation: Vector3f = Vector3f(),
        var scale: Vector3f = Vector3f(1.0f, 1.0f, 1.0f),
        ) : Component()
