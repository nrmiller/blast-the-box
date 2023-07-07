package net.nicksneurons.blastthebox.game.entities

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.StaticBody3D
import net.nicksneurons.blastthebox.physics.shapes.PointCollider3D
import net.nicksneurons.blastthebox.utils.S
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import kotlin.math.abs

class Player : Entity() {

    private val subject : PublishSubject<PlayerEvent> = PublishSubject.create()

    lateinit var gun : Gun

    var requestedMovementSpeed = 20.0f
    var movementSpeedMultipier = 1.0f

    val movementSpeed: Float
        get() = requestedMovementSpeed * movementSpeedMultipier

    var maximumHealth = 10
    var health = maximumHealth
        set(value) {
            if (value > maximumHealth)
                return // cannot exceed maximum health

            if (field != value) {
                field = value
                if (value <= 0) {
                    field = 0
                }
                subject.onNext(HealthUpdatedEvent(this))
            }
        }

    var strength = 1

    val isDead: Boolean
        get() = (health == 0)

    var hasShield = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    // give shield a chance to exist
                    invincible = true
                    timeInvincible = invincibleCooldown // seconds
                }
                subject.onNext(ShieldUpdatedEvent(this))
            }
        }

    var hasTripleFire = false

    private var invincible = false

    private var invincibleCooldown = 1.5
    private var timeInvincible = invincibleCooldown

    val eyeHeight = 1.2f//0.7f

    // Player movement physics
    val strafeSpeed = 10.0f // m/s
    val dashSpeed = 30.0f
    var acceleration = 60.0f
    var friction = 80.0f
    var xVelocity = Vector3f(0.0f)

    var dashComponent = Vector3f(0.0f)

    init {
        transform.position.y = eyeHeight
        addComponent(StaticBody3D(PointCollider3D().apply {
            offset = Vector3f(0.0f, -0.7f, 0.0f)
        }))
        addComponent(DoubleTapGestureRecognizer().apply {
            doubleTappedEvents().subscribe {
                if (it is DoubleTappedLeft) {
                    // add instantaneous acceleration
                    dashComponent.sub(Vector3f(dashSpeed, 0.0f, 0.0f))
                    AudioPlayer.playSound(dash)
                }

                if (it is DoubleTappedRight) {
                    // add instantaneous acceleration
                    dashComponent.add(Vector3f(dashSpeed, 0.0f, 0.0f))
                    AudioPlayer.playSound(dash)
                }
            }
        })
    }

    fun watch() : Observable<PlayerEvent> {
        return subject
    }

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        gun = scene.addEntity(Gun(this))
    }

    fun doDamage(amount: Int) {
        if (!invincible) {
            if (hasShield) {
                hasShield = false
                AudioPlayer.playSound(shieldOff)

                //need to recover
                invincible = true
                timeInvincible = invincibleCooldown
            } else {
                health -= amount
                playAhh()

                strength = 1

                if (hasTripleFire) {
                    hasTripleFire = false
                }
                if (health > 0) {
                    //need to recover
                    invincible = true
                    timeInvincible = invincibleCooldown
                }
            }
        }
    }

    private var shieldOff: AudioSource = AudioSource(AudioClip("/audio/sounds/shield_off.ogg")).apply {
        pitch = 1.0f
        gain = 0.8f
    }
    private var ahhs = mutableListOf(
        AudioSource(AudioClip("/audio/sounds/ahh1.ogg")).apply {
            pitch = 1.0f
            gain = 0.8f
        },
        AudioSource(AudioClip("/audio/sounds/ahh2.ogg")).apply {
            pitch = 1.0f
            gain = 0.8f
        },
        AudioSource(AudioClip("/audio/sounds/ahh3.ogg")).apply {
            pitch = 1.0f
            gain = 0.8f
        }
    )

    private val dash  = AudioSource(AudioClip("/audio/sounds/dash.ogg")).apply {
        pitch = 1.1f
        gain = 1.0f
    }

    override fun onUpdate(delta: Double) {
        super.onUpdate(delta)

        var xInput = 0.0f
        if (GLFW.glfwGetKey(Engine.instance.window.handle, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_TRUE) {
            xInput -= 1.0f
        }
        if (GLFW.glfwGetKey(Engine.instance.window.handle, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_TRUE) {
            xInput += 1.0f
        }

        if (dashComponent.length() < Float.MIN_VALUE) {

            val targetSpeed = xInput * strafeSpeed
            if (abs(xInput) > Float.MIN_VALUE) {
                xVelocity = xVelocity.moveToward(Vector3f(targetSpeed, 0.0f, 0.0f), delta * acceleration)
            } else {
                xVelocity = xVelocity.moveToward(Vector3f(0.0f), delta * friction)
            }
        } else {

            // is dashing
            // the idea is that while dashing, there is an extra velocity component contributing
            // to maximum speed; we move towards this instead.
            // Once the instantaneous dash velocity comes close to zero due to friction, we are no longer dashing.
            // During this entire time, we can still respond to xInput requests
            val targetSpeed = xInput * strafeSpeed + dashComponent.x
            xVelocity = xVelocity.moveToward(Vector3f(targetSpeed, 0.0f, 0.0f), delta * acceleration * 1.2f)
            dashComponent = dashComponent.moveToward(Vector3f(0.0f), delta * friction)
        }

        transform.position.x += (delta * xVelocity.x).toFloat()

        if (invincible) {

            timeInvincible -= delta
            if (timeInvincible <= 0) {
                invincible = false
            }
        }
    }

    private fun playAhh() {
        val index = S.ran.nextInt(3)
        AudioPlayer.playSound(ahhs[index])
    }

    override fun free() {
        super.free()

        shieldOff.free()
        ahhs.forEach {
            it.free()
        }
        subject.onComplete()
    }

    fun Vector3f.moveToward(to: Vector3f, delta: Double): Vector3f {
        val diff = Vector3f()
        to.sub(this, diff)

        return if (diff.length() <= delta || diff.length() < 0.00001) to else Vector3f().add(this).add(diff.normalize().mul(delta.toFloat()))
    }
}

open class PlayerEvent(val player: Player) { }
class HealthUpdatedEvent(player : Player) : PlayerEvent(player) { }
class ShieldUpdatedEvent(player : Player) : PlayerEvent(player) { }