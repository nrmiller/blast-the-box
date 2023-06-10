package net.nicksneurons.blastthebox.game.entities

import miller.opengl.Dimension3d
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.ecs.components.Texture
import net.nicksneurons.blastthebox.ecs.components.Transform
import net.nicksneurons.blastthebox.geometry.Cube

class BoxFactory {
    fun createGrayBox(): Box {
        val box = Box(1)
        box.addComponent(Texture("/textures/block_gray.png"))
        return box
    }

    fun createGreenBox(): Box {
        val box = Box(2)
        box.addComponent(Texture("/textures/block_green.png"))
        return box
    }

    fun createBlueBox(): Box {
        val box = Box(3)
        box.addComponent(Texture("/textures/block_blue.png"))
        return box
    }

    fun createRedBox(): Box {
        val box = Box(0, true)
        box.addComponent(Texture("/textures/block_red.png"))
        return box
    }
}

class Box(val health: Int, val isIndestructible: Boolean = false): Entity() {

    init {
        addComponents(listOf(
                Mesh(Cube()),
                Transform(),
        ))
    }
}