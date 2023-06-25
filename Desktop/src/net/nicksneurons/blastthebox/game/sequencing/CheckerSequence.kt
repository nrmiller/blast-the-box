package net.nicksneurons.blastthebox.game.sequencing

import net.nicksneurons.blastthebox.game.entities.EvenOddRow
import net.nicksneurons.blastthebox.game.entities.Row
import net.nicksneurons.blastthebox.utils.S

class CheckerSequence : Sequence {
    override var isDone: Boolean = false
        private set

    private var checkerSize: Int
    private var checkerCooldown: Int
    private var isEven: Boolean
    private var checkerOffset = 0
    private var remainingChunks: Int

    init {
        checkerSize = S.ran.nextInt(4) + 1
        checkerCooldown = checkerSize + 3
        isEven = S.ran.nextBoolean()
        remainingChunks = S.ran.nextInt(5, 10)
    }

    override fun generateRow(): Row {
        checkerCooldown -= 1
        if (checkerCooldown in 0..3) {
            return Row()
        }
        else if (checkerCooldown < 0) {
            checkerCooldown = checkerSize + 3

            if (remainingChunks-- > 0) {
                if (S.ran.nextBoolean()) {
                    isEven = !isEven
                } else {
                    checkerOffset = S.ran.nextInt(2) * 1 * if (S.ran.nextBoolean()) -1 else 1
                }

                // 10% chance to add another chunk of boxes
                if (S.ran.nextInt(100) > 50) {
                    remainingChunks++
                }
            } else {
                isDone = true
            }
        }

        return EvenOddRow(isEven, checkerOffset, checkerSize)
    }
}
