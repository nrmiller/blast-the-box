package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.ecs.Component
import net.nicksneurons.blastthebox.geometry.Primitive

class Mesh(val primitive: Primitive,
           val texture: Texture) : RenderableComponent() {

    override fun draw() {
        texture.bind()
        primitive.draw()
    }

    override fun free() {
        primitive.free()
        texture.free()
    }
}
