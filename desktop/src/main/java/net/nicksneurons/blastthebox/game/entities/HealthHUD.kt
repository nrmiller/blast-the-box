package net.nicksneurons.blastthebox.game.entities

import io.reactivex.rxjava3.disposables.Disposable
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.ecs.components.RenderableComponent
import net.nicksneurons.blastthebox.graphics.geometry.Square
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import org.joml.Vector2d
import org.joml.Vector3d

class HealthHUD(val player: Player, name: String? = null) : Entity(name) {

    val component = addComponent(HealthHUDComponent())

    inner class HealthHUDComponent : RenderableComponent() {

        val healthUnits = 10

        private val healthMeshes = mutableListOf<Mesh>()
        private val shieldMesh: Mesh

        private val textureHeathOn = Texture2D("/textures/health_on.png")
        private val textureHeathOff = Texture2D("/textures/health_off.png")
        private val textureShield = Texture2D("/textures/shield.png")

        private var subscription: Disposable

        val padding = 16.0
        val cellWidth = 64.0 + padding * 2
        val cellHeight = 32.0 + padding * 2
        val desiredWidth = 64.0 + padding * 2
        val desiredHeight = 32.0 * healthUnits + padding * 2

        init {

            val shieldPadding = 8.0
            val shieldWidth = cellWidth - 2 * shieldPadding
            val shieldHeight = 32.0 * healthUnits + 2 * shieldPadding
            shieldMesh = Mesh(Square(
                Vector3d(shieldPadding, shieldPadding, 0.0),
                Vector2d(shieldWidth, shieldHeight)),
                textureShield)

            for (instance in 0 until healthUnits) {
                healthMeshes.add(Mesh(Square(
                    Vector3d(padding, padding + instance * 32.0, 1.0),
                    Vector2d(64.0, 32.0)),
                    textureHeathOn))
            }

            subscription = player.watch().subscribe {
                updateHud()
            }
        }

        private fun updateHud() {
            for (instance in 0 until healthUnits) {
                if (instance + 1 <= player.health)
                    healthMeshes[(healthUnits - 1) - instance].texture = textureHeathOn
                else
                    healthMeshes[(healthUnits - 1) - instance].texture = textureHeathOff
            }
        }

        override fun draw() {
            super.draw()

            if (player.hasShield) {
                shieldMesh.draw()
            }
            healthMeshes.forEach { it.draw() }
        }

        override fun free() {
            super.free()

            healthMeshes.forEach{ it.free() }
            shieldMesh.free()
        }
    }
}