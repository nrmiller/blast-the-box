package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.graphics.geometry.Primitive
import net.nicksneurons.blastthebox.graphics.textures.Texture

class Mesh(var primitive: Primitive,
           var texture: Texture? = null) : RenderableComponent() {

    override fun draw() {
        texture?.bind()
        primitive.draw()
    }

    override fun free() {
        primitive.free()
        texture?.free()
    }
}
