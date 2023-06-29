package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.game.Powerup
import java.util.*

open class Row() : Entity() {

    protected val mutableBoxes = mutableListOf<Box>()
    val boxes = Collections.unmodifiableCollection(mutableBoxes)

    protected var mutablePowerups = mutableListOf<Powerup>()
    val powerups = Collections.unmodifiableCollection(mutablePowerups)

    override fun free() {
        super.free()

        mutableBoxes.reversed().forEach {
            it.free()
        }
        mutablePowerups.reversed().forEach {
            it.free()
        }
    }
}
