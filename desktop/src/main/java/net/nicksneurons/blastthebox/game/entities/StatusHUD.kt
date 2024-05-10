package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.tools.UpdateListener
import org.joml.*
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.graphics.geometry.Square
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.game.Fonts
import net.nicksneurons.blastthebox.graphics.text.TextSprite
import net.nicksneurons.blastthebox.graphics.textures.Texture2D

class StatusHUD(val player: Player, name: String? = null) : Entity(name) {

    private lateinit var ammoSlot: CounterSlot
    private lateinit var pierceSlot : CounterSlot
    private lateinit var strengthSlot: StrengthSlot
    private lateinit var tripleFireSlot: ToggleSlot
    private lateinit var unlimitedAmmoSlot: ToggleSlot
    private val slots = mutableListOf<Slot>()

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        ammoSlot = CounterSlot("/textures/ammo_counter.png") { player.gun.ammo }
        pierceSlot = CounterSlot("/textures/piercing_counter.png") { player.gun.piercingBullets }
        strengthSlot = StrengthSlot { player.strength }
        tripleFireSlot = ToggleSlot("/textures/powerups/powerup_triplefire.png")
        unlimitedAmmoSlot = ToggleSlot("/textures/powerups/powerup_unlimited_ammo.png")

        slots.add(ammoSlot)
        slots.add(pierceSlot)
        slots.add(unlimitedAmmoSlot)
        slots.add(strengthSlot)
        slots.add(tripleFireSlot)

        slots.forEach {
            it.transform.parent = this.transform
        }

        scene.addEntities(slots)
    }

    override fun onUpdate(delta: Double) {

        pierceSlot.isVisible = (player.gun.piercingBullets > 0)
        unlimitedAmmoSlot.isVisible = (player.gun.unlimitedAmmo)
        strengthSlot.isVisible = (player.strength != 1)
        tripleFireSlot.isVisible = (player.hasTripleFire)

        var index = 0
        slots.forEach {
            if (it.isVisible) {
                it.transform.position.y = ((index) * 64.0f)
                index++
            }
        }
    }

    open class Slot : Entity(), UpdateListener { }
    class CounterSlot(baseImageResource: String, val value: (() -> Int)): Slot() {

        private val baseMesh = Mesh(Square(Vector3d(0.0), Vector2d(64.0, 64.0)), Texture2D(baseImageResource))

        private val textSprite = TextSprite(Fonts.alphanumeric)
        private val textEntity = Entity().also {
            it.addComponent(textSprite)
            it.transform.parent = this.transform
            it.transform.position = Vector3f(32.0f, 2.0f, 0.0f)
        }

        override fun onAddedToScene(scene: Scene) {
            super.onAddedToScene(scene)

            textSprite.sizePx = 24
            addComponent(baseMesh)

            scene.addEntity(textEntity)
        }

        override fun onUpdate(delta: Double) {
            textSprite.text = value().toString()
        }
    }

    class StrengthSlot(val value: (() -> Int)): Slot() {

        private val baseMesh = Mesh(Square(Vector3d(-4.0, 0.0, 0.0), Vector2d(64.0)))

        private val greenTexture = Texture2D("/textures/powerups/powerup_strength1.png")
        private val blueTexture = Texture2D("/textures/powerups/powerup_strength2.png")


        override fun onAddedToScene(scene: Scene) {
            super.onAddedToScene(scene)

            addComponent(baseMesh)
        }

        override fun onUpdate(delta: Double) {
            when(value()) {
                2 -> { baseMesh.texture = greenTexture }
                3 -> { baseMesh.texture = blueTexture }
            }
        }
    }

    class ToggleSlot(textureResourcePath: String) : Slot() {
        private val baseMesh = Mesh(Square(Vector3d(-4.0, 0.0, 0.0), Vector2d(64.0)), Texture2D(textureResourcePath))

        init {
            addComponent(baseMesh)
        }
    }
}