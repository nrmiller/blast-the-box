package net.nicksneurons.blastthebox.game.entities

import miller.opengl.Dimension3d
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.ecs.components.Texture
import net.nicksneurons.blastthebox.ecs.components.Transform
import net.nicksneurons.blastthebox.geometry.Circle
import net.nicksneurons.blastthebox.geometry.Cube
import net.nicksneurons.blastthebox.geometry.Cuboid
import net.nicksneurons.blastthebox.geometry.Square
import org.joml.Vector3f

class BoxFactory {
    fun createGrayBox(): Box {
        val box = Box(1)
        box.components.add(Texture("/textures/block_gray.png"))
        return box
    }

    fun createGreenBox(): Box {
        val box = Box(2)
        box.components.add(Texture("/textures/block_green.png"))
        return box
    }

    fun createBlueBox(): Box {
        val box = Box(3)
        box.components.add(Texture("/textures/block_blue.png"))
        return box
    }

    fun createRedBox(): Box {
        val box = Box(0, true)
        box.components.add(Texture("/textures/block_red.png"))
        return box
    }
}

class Box(val health: Int, val isIndestructible: Boolean = false): Entity() {

    init {
        components.addAll(listOf(
                Mesh(Cube()),
                Transform(),
        ))
    }
}