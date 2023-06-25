package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.game.Powerup

open class Row() : Entity() {

    protected val boxes = mutableListOf<Box>()
    protected var powerups = mutableListOf<Powerup>()

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
