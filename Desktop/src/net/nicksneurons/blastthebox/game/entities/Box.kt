package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.game.Game.cube_health
import net.nicksneurons.blastthebox.game.Game.indestructible
import net.nicksneurons.blastthebox.graphics.geometry.Cube
import net.nicksneurons.blastthebox.graphics.textures.Texture
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import net.nicksneurons.blastthebox.utils.S

class BoxFactory {
    fun createGrayBox(): Box {
        val box = Box(0, BoxType.Gray)
        return box
    }

    fun createGreenBox(): Box {
        val box = Box(0, BoxType.Green)
        return box
    }

    fun createBlueBox(): Box {
        val box = Box(0, BoxType.Blue)
        return box
    }

    fun createRedBox(): Box {
        val box = Box(0, BoxType.Red)
        return box
    }
}

class Box(val slot: Int, val type: BoxType): Entity() {
    init {
        addComponent(Mesh(Cube(), type.createTexture()))
        transform.position.x = slot.toFloat()
    }

    companion object {
        @JvmStatic fun createRandom(slot: Int) : Box {
            return Box(slot, BoxType.getCubeType(pickHealth(), pickIndestructible()))
        }

        @JvmStatic fun pickHealth(): Int {
            return cube_health.spin(S.ran)
        }

        @JvmStatic fun pickIndestructible(): Boolean {
            val i = indestructible.spin(S.ran)
            var b = false
            if (i == Cube.INDESTRUCTIBLE) {
                b = true
            }
            return b
        }


    }
}

enum class BoxType(val resourcePath: String, val health: Int, val isIndestructible: Boolean) {
    Gray("/textures/block_gray.png", 1, false),
    Green("/textures/block_green.png", 2, false),
    Blue("/textures/block_blue.png", 3, false),
    Red("/textures/block_red.png", 0, true);

    fun createTexture(): Texture {
        return Texture2D(resourcePath)
    }

    companion object {
        @JvmStatic fun getCubeType(health: Int, indestructible: Boolean): BoxType {
            return if (indestructible) {
                return Red
            } else {
                when (health) {
                    3 -> { Blue }
                    2 -> { Green }
                    else -> { Gray }
                }
            }
        }
    }
}