package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.ecs.Component
import net.nicksneurons.blastthebox.geometry.Primitive

class Mesh(val primitive: Primitive) : Component() {
    override fun free() {
        primitive.free()
    }
}
