package net.nicksneurons.blastthebox.game

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.ecs.components.StaticBody3D
import net.nicksneurons.blastthebox.graphics.geometry.Square
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import net.nicksneurons.blastthebox.physics.shapes.BoxCollider
import org.joml.Vector3f

class Powerup(val slot: Int, val type: PowerupType) : Entity() {
    init {
        transform.scale = Vector3f(0.5f, 0.5f, 1.0f)
        transform.position = Vector3f(slot + 0.25f, 0.0f, 0.0f)

        addComponent(StaticBody3D(BoxCollider(size = Vector3f(0.75f)).apply {
            offset = Vector3f(-0.125f, 0.125f, 0.0f)
        }))

        addComponent(Mesh(Square(), Texture2D(type.path)).apply {
            renderLayer = 1
        })
    }
}

enum class PowerupType(val path: String) {
    HEART("/textures/powerups/powerup_heart.png"),
    STOPWATCH("/textures/powerups/powerup_stopwatch.png"),
    NUKE("/textures/powerups/powerup_nuke.png"),
    SHIELD("/textures/powerups/powerup_shield.png"),
    TRIPLE_FIRE("/textures/powerups/powerup_triplefire.png"),
    AMMO("/textures/powerups/powerup_ammo.png"),
    STRENGTH_ONE("/textures/powerups/powerup_strength1.png"),
    STRENGTH_TWO("/textures/powerups/powerup_strength2.png"),
    PIERCE("/textures/powerups/powerup_pierce.png");

    var textureId: Int = 0
}