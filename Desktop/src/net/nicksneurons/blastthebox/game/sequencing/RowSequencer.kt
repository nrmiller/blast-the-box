package net.nicksneurons.blastthebox.game.sequencing

import net.nicksneurons.blastthebox.game.Game.settings
import net.nicksneurons.blastthebox.game.entities.Row
import net.nicksneurons.blastthebox.game.entities.StandardRow
import net.nicksneurons.blastthebox.utils.RouletteWheel
import net.nicksneurons.blastthebox.utils.S

class RowSequencer {

    val sequences = RouletteWheel<(() -> Sequence?)>(mapOf(
            { TunnelSequence() } to 0.007, // .7% chance
            { CheckerSequence() } to 0.002, // .2% chance
            { null } to 0.991
    ))

    private var currentSequence: Sequence? = null

    fun generateRow(): Row {
        if (currentSequence == null) {
            currentSequence = sequences.spin(S.ran)?.invoke()
        }
        else if (currentSequence!!.isDone) {
            currentSequence = null
        }

        return currentSequence?.generateRow() ?: StandardRow(settings)
    }
}
