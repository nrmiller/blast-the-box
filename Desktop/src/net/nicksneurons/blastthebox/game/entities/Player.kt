package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.utils.S

class Player : Entity() {

    var movementSpeed = 20.0f

    var health = 1
        set(value) {
            if (field != value) {
                field = value
                if (value <= 0) {
                    field = 0
                }
            }
        }

    var maximumHealth = 10
    var strength = 1

    val isDead: Boolean
        get() = (health == 0)

    var hasShield = false

    var hasTripleFire = false

    private var invincible = false

    private var timeInvincible = 3.0

    fun doDamage(amount: Int) {
        if (!invincible) {
            if (hasShield) {
                hasShield = false
                AudioPlayer.playSound(shieldOff)
            } else {
                health -= amount
                playAhh()

                strength = 1

//                val pde = PlayerDamageEvent(this)
//                for (i in listeners.indices) {
//                    listeners.get(i).onPlayerDamage(pde)
//                }

                if (hasTripleFire) {
                    hasTripleFire = false
                }
                if (health > 0) {
                    //need to recover
                    invincible = true
                    timeInvincible = 3.0 // seconds
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

    override fun onUpdate(delta: Double) {
        super.onUpdate(delta)

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
    }
}