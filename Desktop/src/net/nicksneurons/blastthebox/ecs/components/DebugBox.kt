package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.graphics.geometry.WireframeCuboid
import net.nicksneurons.blastthebox.physics.shapes.BoxCollider
import org.joml.Vector3f

class DebugBox : RenderableComponent {

    val wireframe : WireframeCuboid

    constructor(offset: Vector3f = Vector3f(), size: Vector3f = Vector3f(), color: Vector3f = Vector3f(1.0f, 0.0f, 0.0f), lineWidth: Float = 2.0f) : super() {
        wireframe = WireframeCuboid(location = offset, size = size, color = color, lineWidth)
    }

    constructor(boxCollider: BoxCollider) : super() {

        val position = if (boxCollider.isCentered) {
            Vector3f().sub(Vector3f(boxCollider.size).mul(0.5f))
        } else {
            Vector3f()
        }
        position.add(boxCollider.offset)

        wireframe = WireframeCuboid(location = position, size = boxCollider.size)
    }

    override fun draw() {
        super.draw()

        wireframe.draw()
    }

    override fun free() {
        super.free()

        wireframe.free()
    }
}