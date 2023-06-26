package net.nicksneurons.blastthebox.game.entities

import miller.util.jomlextensions.toVector3d
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.*
import net.nicksneurons.blastthebox.graphics.geometry.Cuboid
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import net.nicksneurons.blastthebox.physics.shapes.BoxCollider
import org.joml.Vector3d
import org.joml.Vector3f

class Bullet(val gun: Gun, val strength: Int) : Entity() {

    private var timeToLive = 5.0
    private var pierceHits = 0

    var isPiercing = false
    var velocity = Vector3f()

    val size = Vector3f(0.08f, 0.08f, 0.2f)
    val collider: BoxCollider

    init {
        addComponent(Mesh(Cuboid(Vector3d(-0.04, -0.04, -0.1), size.toVector3d(), false),
            Texture2D(chooseTexture(strength))))
        collider = BoxCollider(size, isCentered = true)
        addComponent(StaticBody3D(collider))
    }

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)
    }

    override fun onUpdate(delta: Double) {
        super.onUpdate(delta)

        timeToLive -= delta
        if (timeToLive < 0) {
            queueFree()
            return
        }

        val changeInPosition = Vector3f()
        velocity.mul(delta.toFloat(), changeInPosition)
        transform.position.add(changeInPosition)

        checkCollisions()
    }

    fun checkCollisions() {
        val boxes = scene?.getAllEntities<Box>() ?: emptyList()

        for (box in boxes) {
            if (box.isMarkedForDeletion)
                continue
            if (this.isMarkedForDeletion)
                break

            val shape = box.getComponent<StaticBody3D>()?.shape
            if (shape != null) {
                if (shape.intersectsWith(collider)) {
                    onCollision(box)
                }
            }
        }
    }

    fun onCollision(box: Box) {
        if (!box.indestructible) {
            box.doDamage(strength)
        }

        // todo don't reduce pierceHits for the same red box twice
        // todo reduce pierceHits for green/blue boxes all up front and based on bullet strength

        if (isPiercing) {
            pierceHits++
            spawnExplosion()
            if (pierceHits > 10) {
                queueFree()
            }
        } else {
            spawnExplosion()
            queueFree()
        }
    }

    fun spawnExplosion() {
        val explosion = Explosion(
                gun.player,
                Vector3f(transform.position.x - 0.5f, transform.position.y - 0.5f, transform.position.z + collider.size.z / 2)
        )

        scene!!.queueAdd(explosion)
    }

    fun chooseTexture(strength: Int) : String {
        return when (strength) {
            3 -> "/textures/missile3.png"
            2 -> "/textures/missile2.png"
            else -> "/textures/missile.png"
        }
    }
}