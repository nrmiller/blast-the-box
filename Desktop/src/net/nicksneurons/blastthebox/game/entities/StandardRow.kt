package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.game.CubePopulator
import net.nicksneurons.blastthebox.game.Game
import net.nicksneurons.blastthebox.game.GameSettings
import net.nicksneurons.blastthebox.game.Powerup
import net.nicksneurons.blastthebox.utils.S

class StandardRow(
        private val settings: GameSettings) : Row() {

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        val c = S.ran.nextFloat() * (CubePopulator.FIELD_WIDTH * CubePopulator.DENSITY * 10 * settings.cube_density / 100).toFloat()

        var numCubes = 0
        numCubes = if (c >= 0 && c < 0.5) {
            0
        } else {
            1
        }

        val slotFilled = BooleanArray(CubePopulator.FIELD_WIDTH)
        var slotsFilled = 0

        while (slotsFilled < numCubes) { //Loop until all cubes slots are picked.
            val slot = S.ran.nextInt(CubePopulator.FIELD_WIDTH)
            if (!slotFilled[slot]) {
                slotFilled[slot] = true
                slotsFilled++
            }
        }

        var i = 0
        for (k in slotFilled.indices) { //instantiate Cube objects.
            var xLoc: Int
            if (slotFilled[k]) {
                xLoc = k - CubePopulator.FIELD_WIDTH / 2
                val chance = S.ran.nextFloat()
                if (chance <= .1f) { //10% chance of spawning a powerup.
                    powerups.add(scene.addEntity(Powerup(slot = xLoc, type = Game.powerups.spin(S.ran)).also {
                        it.transform.parent = this.transform
                    }))
                } else { //create a cube
                    boxes.add(scene.addEntity(Box.createRandom(slot = xLoc).also {
                        it.getComponent<Mesh>()!!.renderLayer = 1
                        it.transform.parent = this.transform
                    }))
                }
                i++
            }
        }
    }
}
