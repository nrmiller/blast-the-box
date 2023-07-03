package net.nicksneurons.blastthebox.game.sequencing

import net.nicksneurons.blastthebox.game.entities.Row

interface Sequence {

    val isDone : Boolean

    fun generateRow(): Row
}
