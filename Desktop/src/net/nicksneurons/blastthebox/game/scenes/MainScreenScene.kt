package net.nicksneurons.blastthebox.game.scenes

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.audio.AudioClip
import net.nicksneurons.blastthebox.ecs.audio.AudioPlayer
import net.nicksneurons.blastthebox.ecs.audio.AudioSource
import net.nicksneurons.blastthebox.ecs.components.*
import net.nicksneurons.blastthebox.game.entities.BoxFactory
import net.nicksneurons.blastthebox.geometry.Point
import net.nicksneurons.blastthebox.geometry.Square
import org.joml.Vector2i
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import kotlin.random.Random

class MainScreenScene: Scene() {

    lateinit var alphabet: Entity
    lateinit var texture: TextureAtlas

    lateinit var source: AudioSource
    lateinit var explosionSource: AudioSource

    override fun onSceneBegin() {

        source = AudioSource(AudioClip("/audio/sounds/click2.ogg"))
        explosionSource = AudioSource(AudioClip("/audio/sounds/boom.ogg"))

        texture = TextureAtlas("/textures/alphabet.png", Vector2i(64, 64)).apply {
            index = 7
            minFilter = TextureFilter.NEAREST
            isMipmap = false
        }

//        texture = TextureAtlas("/textures/uv_checker.png", Vector2i(128, 128)).apply {
//            index = 37
//            minFilter = TextureFilter.LINEAR
//        }

        alphabet = Entity().apply {
            transform.position = Vector3f(0.0f, 0.0f, -1.0f)

            addComponent(Mesh(Square(), texture).apply {
                material = Material("/shaders/atlas.vert","/shaders/atlas.frag")
            })
        }

        entities.add(alphabet)

        val factory = BoxFactory()
        val grayBox1 = factory.createGreenBox()
        grayBox1.transform.position = Vector3f(1.0f - 0.5f, -0.5f, 0.0f)
        entities.add(grayBox1)
    }

    fun spawnExplosion() {
        val explosion = Entity().also {entity ->
            entity.transform.position = Vector3f(-2.0f, 0.0f, 0.0f)
            entity.transform.scale = Vector3f(2.0f)

//            addComponent(Mesh(Square(), texture).apply {
//                material = Material("/shaders/atlas.vert", "/shaders.atlas.frag")
//            })

            entity.addComponent(SpriteAnimation(TextureAtlas("/textures/explosion.png", Vector2i(64, 64)), 10, 40).apply {
                material = Material("/shaders/atlas.vert", "/shaders/atlas.frag")

                animationStateChanges().subscribe() { state ->
                    if (state == AnimationState.Completed) {
                        entity.queueFree()
                    }
                }

                play(LoopMode.ClampForever)
            })
        }

        entities.add(explosion)

        AudioPlayer.playSound(explosionSource.apply {
            pitch = 1.0f + 0.1f * Random.nextFloat() - 0.05f
            gain = 0.8f
        })
    }


    var index = 0

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        if (key == GLFW_KEY_RIGHT) {
            texture.index = ((texture.index + 1).mod(27))
            playClick()
        }
        if (key == GLFW_KEY_LEFT) {
            texture.index = ((texture.index - 1).mod(27))
            playClick()
        }
        if (key == GLFW_KEY_UP) {
            alphabet.transform.scale = alphabet.transform.scale.mul(1.1f)
            playClick()
        }
        if (key == GLFW_KEY_DOWN) {
            alphabet.transform.scale = alphabet.transform.scale.mul(0.9f)
            playClick()
        }

        if (key == GLFW_KEY_SPACE) {
            spawnExplosion()
        }
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        if (key == GLFW_KEY_RIGHT) {
            texture.index = ((texture.index + 1).mod(27))
            playClick()
        }
        if (key == GLFW_KEY_LEFT) {
            texture.index = ((texture.index - 1).mod(27))
            playClick()
        }
        if (key == GLFW_KEY_UP) {
            alphabet.transform.scale = alphabet.transform.scale.mul(1.1f)
            playClick()
        }
        if (key == GLFW_KEY_DOWN) {
            alphabet.transform.scale = alphabet.transform.scale.mul(0.9f)
            playClick()
        }
    }

    private fun playClick() {
        AudioPlayer.playSound(source.apply {
            pitch = 1.0f + 0.1f * Random.nextFloat() - 0.05f
            gain = 0.8f
        })
    }

    override fun onSceneEnd() {

    }
}