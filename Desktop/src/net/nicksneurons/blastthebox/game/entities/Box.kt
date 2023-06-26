package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.ecs.components.StaticBody3D
import net.nicksneurons.blastthebox.game.Game.cube_health
import net.nicksneurons.blastthebox.game.Game.indestructible
import net.nicksneurons.blastthebox.graphics.geometry.Cube
import net.nicksneurons.blastthebox.graphics.textures.Texture
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import net.nicksneurons.blastthebox.physics.shapes.BoxCollider
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

class Box(val slot: Int, type: BoxType): Entity() {

    private val mesh: Mesh

    var health = type.health
        private set(value) {
            if (field != value) {
                field = value
                type = BoxType.getCubeType(value, type.isIndestructible)
            }
        }

    var type: BoxType = type
        private set(value) {
            if (field != value) {
                field = value
                mesh.texture = type.createTexture()
            }
        }

    init {
        mesh = addComponent(Mesh(Cube(), type.createTexture()))
        addComponent(StaticBody3D(BoxCollider()))
        transform.position.x = slot.toFloat()
    }

    val indestructible: Boolean
        get() = (type.isIndestructible)

    fun doDamage(damage: Int) {
        if (!indestructible) {
            health -= damage
            if (health <= 0) {
                queueFree()
            }
        }
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