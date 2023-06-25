package net.nicksneurons.blastthebox.game.sequencing

import net.nicksneurons.blastthebox.game.CubePopulator
import net.nicksneurons.blastthebox.game.entities.Row
import net.nicksneurons.blastthebox.game.entities.SequencedRow
import net.nicksneurons.blastthebox.utils.S

class TunnelSequence : Sequence {
    override var isDone: Boolean = false
        private set

    private var isFirstRow = true
    private var seq_width: Int
    private var seq_index: Int
    private var emptyRowsRemaining: Int

    init {
        seq_width = S.ran.nextInt(3) + 8
        seq_index = S.ran.nextInt(CubePopulator.FIELD_WIDTH)
        if (seq_index > CubePopulator.FIELD_WIDTH - seq_width) //If index is beyond right side of field, change it.
        {
            seq_index = CubePopulator.FIELD_WIDTH - seq_width
        }
        emptyRowsRemaining = S.ran.nextInt(5, 10)
    }

    override fun generateRow(): Row {
        if (emptyRowsRemaining-- > 0) {
            return Row()
        }
        if (isFirstRow) {
            val result = SequencedRow(seq_width, seq_index, isFirstRow)
            isFirstRow = false
            return result
        } else {
            if (S.ran.nextInt(50) >= 30 && seq_width > 2) {
                seq_width -= 1
            }
            val n: Int = S.ran.nextInt(100)
            if (n >= 80 && seq_width > 2) {
                seq_index += 1
            } else if (n >= 60 && n < 80 && seq_width > 2) {
                seq_index -= 1
            }
            if (seq_width <= 3) //If width becomes small enough, create opportunity to stop sequencing.
            {
                if (S.ran.nextInt(100) > 60) {
                    isDone = true
                }
            }
            if (seq_index < 0) //Make sure that the index and width stay in bounds. (Left-side)
            {
                seq_index = 0
                if (seq_width <= 1) {
                    seq_width = 2
                }
            }

            return SequencedRow(seq_width, seq_index, isFirstRow)
        }
    }
}
