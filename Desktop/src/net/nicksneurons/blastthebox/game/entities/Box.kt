package net.nicksneurons.blastthebox.game.entities

import miller.opengl.Dimension3d
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.ecs.components.Texture
import net.nicksneurons.blastthebox.ecs.components.Texture2D
import net.nicksneurons.blastthebox.ecs.components.Transform
import net.nicksneurons.blastthebox.geometry.Cube

class BoxFactory {
    fun createGrayBox(): Box {
        val box = Box(BoxType.Gray, 1)
        return box
    }

    fun createGreenBox(): Box {
        val box = Box(BoxType.Green, 2)
        return box
    }

    fun createBlueBox(): Box {
        val box = Box(BoxType.Blue, 3)
        return box
    }

    fun createRedBox(): Box {
        val box = Box(BoxType.Red, 0, true)
        return box
    }
}

class Box(val type: BoxType, val health: Int, val isIndestructible: Boolean = false): Entity() {
    init {
        addComponent(Mesh(Cube(), type.createTexture()))
    }
}

enum class BoxType(val resourcePath: String) {
    Gray("/textures/block_gray.png"),
    Green("/textures/block_green.png"),
    Blue("/textures/block_blue.png"),
    Red("/textures/block_red.png");

    fun createTexture(): Texture {
        return Texture2D(resourcePath)
    }
}