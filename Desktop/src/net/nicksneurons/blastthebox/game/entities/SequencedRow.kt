package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.game.CubePopulator
import net.nicksneurons.blastthebox.utils.S
import kotlin.math.max
import kotlin.math.min

class SequencedRow(
        startDistance: Float,
        var openingWidth: Int,
        val startPos: Int,
        val first: Boolean): Row(startDistance) {

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        openingWidth = max(1, min(openingWidth, CubePopulator.FIELD_WIDTH))

        val slotFilled = BooleanArray(CubePopulator.FIELD_WIDTH)
        for (i in slotFilled.indices) {
            if (i < startPos || i > startPos + openingWidth) {
                if (first)
                {
                    //If this is the first row. Then fill all non-openings.
                    slotFilled[i] = true
                } else if (i == startPos - 1 || i == startPos + openingWidth + 1) {
                    slotFilled[i] = true
                } else {
                    slotFilled[i] = S.ran.nextInt(100) > 90
                }
            } else {
                slotFilled[i] = false
            }
        }

        var xLoc = 0
        for (i in slotFilled.indices) {
            if (slotFilled[i]) //IF slot filled, place cube
            {
                xLoc = i - CubePopulator.FIELD_WIDTH / 2

                boxes.add(scene.addEntity(Box.createRandom(slot = xLoc).also {
                    it.getComponent<Mesh>()!!.renderLayer = 1
                    it.transform.parent = this.transform
                }))
            }
        }
    }
}
