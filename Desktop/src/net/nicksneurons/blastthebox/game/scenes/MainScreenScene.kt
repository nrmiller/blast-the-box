package net.nicksneurons.blastthebox.game.scenes

import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.ecs.components.*
import net.nicksneurons.blastthebox.game.Fonts
import net.nicksneurons.blastthebox.graphics.geometry.Square
import net.nicksneurons.blastthebox.graphics.text.BitmapFont
import net.nicksneurons.blastthebox.graphics.text.TextSprite
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import net.nicksneurons.blastthebox.graphics.textures.TextureAtlas
import net.nicksneurons.blastthebox.graphics.textures.TextureFilter
import net.nicksneurons.blastthebox.physics.shapes.RectangleCollider
import net.nicksneurons.blastthebox.utils.Camera2D
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import kotlin.random.Random

class MainScreenScene: Scene() {

    lateinit var alphabet: Entity
    lateinit var texture: TextureAtlas

    lateinit var click1Source: AudioSource
    lateinit var click2Source: AudioSource
    lateinit var explosionSource: AudioSource

    lateinit var title: TextSprite

    override fun onSceneBegin() {

        click1Source = AudioSource(AudioClip("/audio/sounds/click.ogg"))
        click2Source = AudioSource(AudioClip("/audio/sounds/click2.ogg"))
        explosionSource = AudioSource(AudioClip("/audio/sounds/boom.ogg"))

        texture = TextureAtlas("/textures/alphabet.png", Vector2i(64, 64)).apply {
            index = 7
            minFilter = TextureFilter.NEAREST
            isMipmap = false
        }

        title = TextSprite(Fonts.alphanumeric).apply {

            text = "Hello World! 777"
        }

        camera = Camera2D()

//        texture = TextureAtlas("/textures/uv_checker.png", Vector2i(128, 128)).apply {
//            index = 37
//            minFilter = TextureFilter.LINEAR
//        }

        alphabet = Entity().apply {
            transform.position = Vector3f(0.0f, 0.0f, 25.0f)

            addComponent(Mesh(Square(), texture).apply {
                material = Material("/shaders/atlas.vert","/shaders/atlas.frag")
            })
        }

        addEntity(alphabet)

        addEntity(Entity().apply {
            transform.position = Vector3f(150.0f, 500.0f, 24.0f)

            addComponent(title)
        })

        addEntity(Entity().apply {
            transform.position = Vector3f(100.0f, 100.0f, 24.0f)
            transform.scale = Vector3f(100.0f, 100.0f, 0.0f)

            addComponent(Mesh(Square(), texture.copyTextureAt(5)))
        })

        addEntity(Entity().apply {
            transform.position = Vector3f(0.0f, 0.0f, 0.0f)
            transform.scale = Vector3f(Engine.instance.width.toFloat(), Engine.instance.height.toFloat(), 0.0f)

            addComponent(Mesh(Square(), Texture2D("/textures/menu_main.png")))
        })

        // todo need to make this resizable
        val buttonW = Engine.instance.width * .22f
        val buttonH = Engine.instance.height * .3f
        val buttonX = (Engine.instance.width - 3 * buttonW) / 4
        val buttonY = (Engine.instance.height - buttonH) / 2
        val gap = (Engine.instance.width - buttonW * 3) / 4

        addEntity(Entity().apply {
            transform.position = Vector3f(buttonX, buttonY, 1.0f)
            transform.scale = Vector3f(buttonW, buttonH, 0.0f)

            val mesh = Mesh(Square(), Texture2D("/textures/button_easymode.png"));
            addComponent(mesh)
            addComponent(ClickableBody2D(RectangleCollider(Vector2f(buttonW, buttonH))).apply {

                mouseEvents().filter { it is MouseEnter }.subscribe() {
                    playClick1()
                    mesh.texture.free()
                    mesh.texture = Texture2D("/textures/button_easymode_hover.png")
                }

                mouseEvents().filter { it is MouseExit }.subscribe() {
                    playClick1()
                    mesh.texture.free()
                    mesh.texture = Texture2D("/textures/button_easymode.png")
                }

                mouseEvents().filter { it is MouseDown }.subscribe() {
                    playClick2()
                    Engine.instance.choreographer.end(scene!!)
                }
            })
        })

        addEntity(Entity().apply {
            transform.position = Vector3f(buttonX + buttonW + gap, buttonY, 1.0f)
            transform.scale = Vector3f(buttonW, buttonH, 0.0f)

            val mesh = Mesh(Square(), Texture2D("/textures/button_mediummode.png"))
            addComponent(mesh)
            addComponent(ClickableBody2D(RectangleCollider(Vector2f(buttonW, buttonH))).apply {

                mouseEvents().filter { it is MouseEnter }.subscribe() {
                    playClick1()
                    mesh.texture.free()
                    mesh.texture = Texture2D("/textures/button_mediummode_hover.png")
                }

                mouseEvents().filter { it is MouseExit }.subscribe() {
                    playClick1()
                    mesh.texture.free()
                    mesh.texture = Texture2D("/textures/button_mediummode.png")
                }

                mouseEvents().filter { it is MouseDown }.subscribe() {
                    playClick2()
                    Engine.instance.choreographer.end(scene!!)
                }
            })
        })

        addEntity(Entity().apply {
            transform.position = Vector3f(buttonX + (buttonW + gap) * 2, buttonY, 1.0f)
            transform.scale = Vector3f(buttonW, buttonH, 0.0f)

            val mesh = Mesh(Square(), Texture2D("/textures/button_hardmode.png"))
            addComponent(mesh)
            addComponent(ClickableBody2D(RectangleCollider(Vector2f(buttonW, buttonH))).apply {

                mouseEvents().filter { it is MouseEnter }.subscribe() {
                    playClick1()
                    mesh.texture.free()
                    mesh.texture = Texture2D("/textures/button_hardmode_hover.png")
                }

                mouseEvents().filter { it is MouseExit }.subscribe() {
                    playClick1()
                    mesh.texture.free()
                    mesh.texture = Texture2D("/textures/button_hardmode.png")
                }

                mouseEvents().filter { it is MouseDown }.subscribe() {
                    playClick2()
                    Engine.instance.choreographer.end(scene!!)
                }
            })
        })
    }

    fun spawnExplosion() {
        val explosion = Entity().also {entity ->
            entity.transform.position = Vector3f(-2.0f, 0.0f, 0.0f)
            entity.transform.scale = Vector3f(2.0f)

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

        addEntity(explosion)

        AudioPlayer.playSound(explosionSource.apply {
            pitch = 1.0f + 0.1f * Random.nextFloat() - 0.05f
            gain = 0.8f
        })
    }


    var index = 0

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        if (key == GLFW_KEY_RIGHT) {
            texture.index = ((texture.index + 1).mod(27))
            playClick1()
            (camera as Camera2D).zoom *= 2
        }
        if (key == GLFW_KEY_LEFT) {
            texture.index = ((texture.index - 1).mod(27))
            playClick1()
            (camera as Camera2D).zoom *= 0.5f
        }
        if (key == GLFW_KEY_UP) {
            alphabet.transform.scale = alphabet.transform.scale.mul(1.1f)
            playClick1()

            title.sizePx++
        }
        if (key == GLFW_KEY_DOWN) {
            alphabet.transform.scale = alphabet.transform.scale.mul(0.9f)
            playClick1()

            title.sizePx--
        }

        if (key== GLFW_KEY_C) {
            (camera as Camera2D).isCentered = !(camera as Camera2D).isCentered
        }
        if (key== GLFW_KEY_Y) {
            (camera as Camera2D).invertY = !(camera as Camera2D).invertY
        }

        if (key == GLFW_KEY_SPACE) {
            spawnExplosion()
        }

        keyEntry(key, scancode)
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        if (key == GLFW_KEY_RIGHT) {
            texture.index = ((texture.index + 1).mod(27))
            playClick1()
        }
        if (key == GLFW_KEY_LEFT) {
            texture.index = ((texture.index - 1).mod(27))
            playClick1()
        }
        if (key == GLFW_KEY_UP) {
            alphabet.transform.scale = alphabet.transform.scale.mul(1.1f)
            playClick1()
        }
        if (key == GLFW_KEY_DOWN) {
            alphabet.transform.scale = alphabet.transform.scale.mul(0.9f)
            playClick1()
        }

        keyEntry(key, scancode)
    }

    private fun keyEntry(key: Int, scancode: Int) {
        val char = glfwGetKeyName(key, scancode)
        if (char != null) {
            title.text += char
        }
        if (key == GLFW_KEY_SPACE) {
            title.text += " "
        }
        if (key == GLFW_KEY_ENTER) {
            title.text += "\n"
        }
        if (key == GLFW_KEY_BACKSPACE) {
            if (title.text.isNotEmpty()) {
                title.text = title.text.substring(0 until title.text.length - 1)
            }
        }
    }

    private fun playClick1() {
        AudioPlayer.playSound(click1Source.apply {
            pitch = 1.0f + 0.1f * Random.nextFloat() - 0.05f
        })
    }

    private fun playClick2() {
        AudioPlayer.playSound(click2Source.apply {
            pitch = 1.0f + 0.1f * Random.nextFloat() - 0.05f
        })
    }

    override fun onSceneEnd() {

    }
}