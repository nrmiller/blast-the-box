package net.nicksneurons.blastthebox.ecs

import miller.util.jomlextensions.format
import miller.util.jomlextensions.toVector3f
import miller.util.jomlextensions.toVector4f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import java.util.*

data class Transform(
        var position: Vector3f = Vector3f(),
        var rotation: Vector3f = Vector3f(),
        var scale: Vector3f = Vector3f(1.0f, 1.0f, 1.0f)) {


    /**
     * Mimic NEZ for now. Godot uses a Node tree, but a transform tree also makes sense.
     * Seeing as the need for parenting came from needing to group entities relative to one another
     * this seems like a more minimalistic approach.
     * That said, the general solution that Godot employs may also be good too.
     */
    var parent: Transform? = null
        set(value) {
            if (value != field) {
                require(value != this) { "Transform cannot parent to itself!" }

                // Detach from old parent
                field?.mutableChildren?.remove(this)

                // Attach to new parent
                value?.mutableChildren?.add(this)

                field = value
            }
        }


    private val mutableChildren = mutableListOf<Transform>()
    val children : List<Transform> = Collections.unmodifiableList(mutableChildren)

    fun getWorldTransform(): Matrix4f {

        val localTransform = Matrix4f()
                .translate(position.x, position.y, position.z)
                .rotate(rotation.x, Vector3f(1.0f, 0.0f, 0.0f))
                .rotate(rotation.y, Vector3f(0.0f, 1.0f, 0.0f))
                .rotate(rotation.z, Vector3f(0.0f, 0.0f, 1.0f))
//                .rotate(transform.rotation.x, transform.rotation.y, transform.rotation.z) // todo need quaternions
                .scale(scale.x, scale.y, scale.z)

        val parentTransform = parent?.getWorldTransform() ?: Matrix4f()

        val result = Matrix4f()
        parentTransform.mul(localTransform, result)
        return result
    }

    fun getWorldPosition(): Vector4f {
        return getWorldTransform().transform(position.toVector4f())
    }

    override fun toString(): String {
        return "[pos=${getWorldPosition().toVector3f().format()}, rot=${rotation.format()}, scale=${scale.format()}]"
    }
}
