package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.game.CubePopulator
import net.nicksneurons.blastthebox.game.Game
import net.nicksneurons.blastthebox.game.Powerup
import net.nicksneurons.blastthebox.utils.S

class EvenOddRow(
        val isEven: Boolean,
        val startPos: Int,
        var blockSize: Int) : Row() {

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        blockSize += 2 // add edges

        for (slot in 0 until CubePopulator.FIELD_WIDTH) {

            val offset = if (isEven) 0 else blockSize
            var filled = ((slot + startPos + offset) / blockSize).mod(2) == 0

            val blockIndex = (slot + startPos + offset).mod(blockSize)
            if (filled && (blockIndex == 0 || blockIndex == blockSize - 1)) {
                filled = false
            }



            val xLoc = slot - CubePopulator.FIELD_WIDTH / 2
            if (filled) {
                mutableBoxes.add(scene.addEntity(Box.createRandom(slot = xLoc).also {
                    it.getComponent<Mesh>()!!.renderLayer = 1
                    it.transform.parent = this.transform
                }))
            } else {
                // chance of power up
                val chance = S.ran.nextFloat()
                if (chance <= .05f) { //5% chance of spawning a powerup.
                    val type = Game.powerups.spin(S.ran)
                    if (type != null) {
                        mutablePowerups.add(scene.addEntity(Powerup(slot = xLoc, type = type).also {
                            it.transform.parent = this.transform
                        }))
                    }
                }
            }
        }
    }
}