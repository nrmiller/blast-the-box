package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.game.Powerup
import org.joml.Vector3f

open class Row(val startDistance: Float) : Entity() {

    protected val boxes = mutableListOf<Box>()
    protected var powerups = mutableListOf<Powerup>()

    init {
        transform.position = Vector3f(0.0f, 0.0f, -startDistance)
    }

    override fun free() {
        super.free()

        boxes.reversed().forEach {
            it.free()
        }
        powerups.reversed().forEach {
            it.free()
        }
    }
}
