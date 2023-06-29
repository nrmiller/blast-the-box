package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.AnimationState
import net.nicksneurons.blastthebox.ecs.components.LoopMode
import net.nicksneurons.blastthebox.ecs.components.Material
import net.nicksneurons.blastthebox.ecs.components.SpriteAnimation
import net.nicksneurons.blastthebox.graphics.textures.TextureAtlas
import org.joml.Vector2i
import org.joml.Vector3f
import kotlin.random.Random

class Explosion(val player: Player, val position: Vector3f, val shouldPlaySound: Boolean = true) : Entity() {

    init {
        transform.position = position

        addComponent(SpriteAnimation(TextureAtlas("/textures/explosion.png", Vector2i(64, 64)), 10, 40).apply {
            material = Material("/shaders/atlas.vert", "/shaders/atlas.frag")
            renderLayer = 1

            animationStateChanges().subscribe() { state ->
                if (state == AnimationState.Completed) {
                    queueFree()
                }
            }

            play(LoopMode.ClampForever)
        })
    }
    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        if (shouldPlaySound) {
            // Since the explosion may end sooner than the audio clip, the
            // player must manage releasing resources
            AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/boom.ogg")).apply {
                pitch = 1.0f + 0.1f * Random.nextFloat() - 0.05f
                gain = 0.8f
            }, true)
        }
    }

    override fun onUpdate(delta: Double) {
        super.onUpdate(delta)

        moveCloser(delta.toFloat() * player.movementSpeed)
    }

    fun moveCloser(amount: Float) {
        transform.position.z += amount

        if (transform.position.z > 0.0f) {
            queueFree()
        }
    }
}