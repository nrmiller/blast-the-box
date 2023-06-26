package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Gun(val player: Player): Entity() {

    private val bulletSpeed = 10.0f

    private var fire: AudioSource = AudioSource(AudioClip("/audio/sounds/shot.ogg")).apply {
        pitch = 1.0f
        gain = 0.8f
    }

    init {
        transform.parent = player.transform
        transform.position = Vector3f(0.0f, -0.7f, 0.0f) // move down from eye height
    }

    var ammo = 100000 // 5
    var piercingBullets = 0
    private val hasPierce : Boolean
        get() = piercingBullets > 0

    fun addAmmo(amount: Int) {
        ammo += amount
        if (ammo > 99) {
            ammo = 99
        }
    }

    fun addPiercingBullets(amount: Int) {
        piercingBullets += amount
        if (piercingBullets > 10) {
            piercingBullets = 10
        }
    }

    private var isRapidFire = true
    private var isOnCooldown = false
    private var cooldownRemaining =  if (isRapidFire) 0.1 else 0.3 // seconds

    fun fire() {
        if (isOnCooldown)
            return
        if (ammo <= 0)
            return

        isOnCooldown = true
        cooldownRemaining = if (isRapidFire) 0.1 else 0.3

        // spawn bullet with velocity
        var isPiercing: Boolean = true
        if (hasPierce) {
            isPiercing = true
            piercingBullets--
        }

        AudioPlayer.playSound(fire)
        val bullet = Bullet(this, player.strength).also {
            it.transform.position = Vector3f(transform.getWorldPosition().x, transform.getWorldPosition().y, 0.0f)
            it.isPiercing = isPiercing
            it.velocity = Vector3f(0.0f, 0.0f, -bulletSpeed)
        }

        scene?.addEntity(bullet)
        ammo--

        if (player.hasTripleFire) {
            if (ammo > 0) {
                val b2 = Bullet(this, player.strength).also {
                    it.transform.position = Vector3f(transform.getWorldPosition().x, transform.getWorldPosition().y, 0.0f)
                    it.transform.rotation = Vector3f(0.0f, Math.toRadians(20.0).toFloat(), 0.0f)
                    it.isPiercing = isPiercing
                    val velocity = Vector3f(cos(Math.PI.toFloat() / 2f + it.transform.rotation.y), 0.0f, -sin(Math.PI.toFloat() / 2f + it.transform.rotation.y))
                    it.velocity = velocity.normalize(bulletSpeed)
                }
                scene?.addEntity(b2)
                ammo -= 1
            }
            if (ammo > 0) {
                val b3 = Bullet(this, player.strength).also {
                    it.transform.position = Vector3f(transform.getWorldPosition().x, transform.getWorldPosition().y, 0.0f)
                    it.transform.rotation = Vector3f(0.0f, Math.toRadians(-20.0).toFloat(), 0.0f)
                    it.isPiercing = isPiercing
                    val velocity = Vector3f(cos(Math.PI.toFloat() / 2f + it.transform.rotation.y), 0.0f, -sin(Math.PI.toFloat() / 2f + it.transform.rotation.y))
                    it.velocity = velocity.normalize(bulletSpeed)
                }
                scene?.addEntity(b3)
                ammo -= 1
            }
        }
    }

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)
    }

    override fun onUpdate(delta: Double) {
        super.onUpdate(delta)

        if (isOnCooldown) {
            cooldownRemaining -= delta
            if (cooldownRemaining <= 0.0) {
                isOnCooldown = false
            }
        }
    }

    override fun free() {
        super.free()

        fire.free()
    }
}