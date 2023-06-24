package net.nicksneurons.blastthebox.game.scenes

import miller.util.jomlextensions.toVector2f
import miller.util.jomlextensions.toVector3f
import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.*
import net.nicksneurons.blastthebox.graphics.ui.Button
import net.nicksneurons.blastthebox.game.Fonts
import net.nicksneurons.blastthebox.graphics.geometry.Square
import net.nicksneurons.blastthebox.graphics.text.TextSprite
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import net.nicksneurons.blastthebox.graphics.textures.TextureAtlas
import net.nicksneurons.blastthebox.graphics.ui.FocusScope
import net.nicksneurons.blastthebox.graphics.ui.Focused
import net.nicksneurons.blastthebox.physics.shapes.RectangleCollider
import net.nicksneurons.blastthebox.utils.Camera2D
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import kotlin.math.sin
import kotlin.random.Random

class MainScreenScene: Scene() {

    lateinit var bgMusic: AudioSource
    lateinit var click1Source: AudioSource
    lateinit var click2Source: AudioSource
    lateinit var explosionSource: AudioSource

    lateinit var title: TextSprite
    lateinit var titleEntity: Entity

    lateinit var focusScope: FocusScope

    override fun onSceneBegin() {

        bgMusic = AudioSource(AudioClip("/audio/tracks/loop_two.ogg")).apply {
            pitch = 1.0f
            gain = 0.8f
        }
        AudioPlayer.loopSound(bgMusic)

        click1Source = AudioSource(AudioClip("/audio/sounds/click.ogg"))
        click2Source = AudioSource(AudioClip("/audio/sounds/click2.ogg"))
        explosionSource = AudioSource(AudioClip("/audio/sounds/boom.ogg"))

        camera = Camera2D()

        addEntity(Entity("background").apply {
            transform.position = Vector3f(0.0f, 0.0f, 0.0f)

            addComponent(Mesh(Square(), Texture2D("/textures/menu_main.png")))
        })

        addEntity(Button("btn-easy").apply {
            defaultTexturePath = "/textures/button_easymode.png"
            hoverTexturePath = "/textures/button_easymode_hover.png"
            onEnter = { focusScope.focus(this) }
            onExit = { focusScope.clearFocus() }
            onDown = { navigateToGame() }
            focusEvents().filter { it is Focused }.subscribe { playClick1() }
        })

        addEntity(Button("btn-medium").apply {
            defaultTexturePath = "/textures/button_mediummode.png"
            hoverTexturePath = "/textures/button_mediummode_hover.png"
            onEnter = { focusScope.focus(this) }
            onExit = { focusScope.clearFocus() }
            onDown = { navigateToGame() }
            focusEvents().filter { it is Focused }.subscribe { playClick1() }
        })

        addEntity(Button("btn-hard").apply {
            defaultTexturePath = "/textures/button_hardmode.png"
            hoverTexturePath = "/textures/button_hardmode_hover.png"
            onEnter = { focusScope.focus(this) }
            onExit = { focusScope.clearFocus() }
            onDown = { navigateToGame() }
            focusEvents().filter { it is Focused }.subscribe { playClick1() }
        })

        title = TextSprite(Fonts.alphanumeric).apply {
            text = "Blast the Box!™"
        }
        titleEntity = addEntity(Entity().apply {
            addComponent(title)
        })

//        addEntity(Entity("btn-exit").apply {
//            addComponent(Mesh(Square(), Texture2D("/textures/button_exit.png")))
//        })

        addEntity(Button("btn-options").apply {
            defaultTexturePath = "/textures/button_options.png"
            hoverTexturePath = "/textures/button_options_hover.png"
            size = Vector2f(64.0f)
            onEnter = { playClick1() }
            onDown = { navigateToOptions() }
        })

        addEntity(Button("btn-achievements").apply {
            defaultTexturePath = "/textures/button_achievements.png"
            hoverTexturePath = "/textures/button_achievements_hover.png"
            size = Vector2f(64.0f)
            onEnter = { playClick1() }
        })

        addEntity(Button("btn-howtoplay").apply {
            defaultTexturePath = "/textures/button_howtoplay.png"
            hoverTexturePath = "/textures/button_howtoplay_hover.png"
            onEnter = { playClick1() }
        })

        focusScope = FocusScope(
                this["btn-easy"] as Button,
                this["btn-medium"] as Button,
                this["btn-hard"] as Button)
        focusScope.focus()
    }

    private fun navigateToGame() {
        playClick2()
        transitionTo(::GameScene)
    }

    private fun navigateToOptions() {
        playClick2()
        finishScene()
    }

    private var angle = 0.0f
    private var angularVelocity = 2.0f * Math.PI.toFloat()
    private val maxAdditionalScale = 0.05f
    override fun onUpdate(delta: Double) {
        super.onUpdate(delta)

        val width = Engine.instance.width
        val height = Engine.instance.height
        val buttonW = width * .22f
        val buttonH = height * .3f
        val buttonX = (width - 3 * buttonW) / 4
        val buttonY = (height - buttonH) / 2
        val gap = (width - buttonW * 3) / 4
        val padding = ((height - buttonH) / 2 - title.sizePx) / 2

        title.sizePx = (0.1f * height).toInt()

        this["background"].apply {
            transform.scale = Vector3f(width.toFloat(), height.toFloat(), 0.0f)
        }

        this["btn-easy"].apply {
            transform.position = Vector3f(buttonX, buttonY, 1.0f)
            transform.scale = Vector3f(buttonW, buttonH, 0.0f)

            (getComponent<ClickableBody2D>()!!.shape as RectangleCollider).apply {
                size = Vector2f(buttonW, buttonH)
            }
        }
        this["btn-medium"].apply {
            transform.position = Vector3f(buttonX + buttonW + gap, buttonY, 1.0f)
            transform.scale = Vector3f(buttonW, buttonH, 0.0f)

            (getComponent<ClickableBody2D>()!!.shape as RectangleCollider).apply {
                size = Vector2f(buttonW, buttonH)
            }
        }
        this["btn-hard"].apply {
            transform.position = Vector3f(buttonX + (buttonW + gap) * 2, buttonY, 1.0f)
            transform.scale = Vector3f(buttonW, buttonH, 0.0f)

            (getComponent<ClickableBody2D>()!!.shape as RectangleCollider).apply {
                size = Vector2f(buttonW, buttonH)
            }
        }

        // todo don't show this on desktop
//        this["btn-exit"].apply {
//            transform.scale = Vector3f(0.125f * height, 0.125f * height, 0.0f)
//            transform.position = Vector3f(width - transform.scale.x, height - transform.scale.y, 1.0f)
//        }

        this["btn-options"].apply {
            transform.scale = Vector3f(64.0f)
            transform.position = Vector3f(width - transform.scale.x, 0.0f, 1.0f)

            (getComponent<ClickableBody2D>()!!.shape as RectangleCollider).apply {
                size = Vector2f(64.0f)
            }
        }

        (this["btn-achievements"] as Button).apply {
            transform.scale = size.toVector3f()
            transform.position = Vector3f(width - transform.scale.x, scene!!["btn-options"].transform.scale.y, 1.0f)
        }

        val howtoplayW = width * .2f
        val howtoplayX = (width - howtoplayW) / 2
        val howtoplayY = (buttonY - howtoplayW / 2) / 2
        (this["btn-howtoplay"] as Button).apply {
            transform.scale = Vector3f(howtoplayW, howtoplayW / 2.0f, 0.0f)
            transform.position = Vector3f(howtoplayX, howtoplayY, 1.0f)

            size = transform.scale.toVector2f()
        }

        angle += (angularVelocity * delta).toFloat()
        val additionalScale = maxAdditionalScale * sin(angle.toDouble()).toFloat()
        titleEntity.transform.scale = Vector3f(1.0f + additionalScale, 1.0f + additionalScale, 0.0f)
        titleEntity.transform.position = Vector3f((width - title.fontMetrics.width * titleEntity.transform.scale.x) / 2.0f, buttonY + buttonH + padding, 24.0f)
    }

    fun spawnExplosion() {
        val explosion = Entity().also {entity ->
            entity.transform.position = Vector3f(0.0f, 0.0f, 1.0f)
            entity.transform.scale = Vector3f(64.0f)

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
        super.onKeyDown(key, scancode, modifiers)

        if (key == GLFW_KEY_LEFT) {
            focusScope.previous()
        }
        if (key == GLFW_KEY_RIGHT) {
            focusScope.next()
        }

        if (key == GLFW_KEY_SPACE) {
            spawnExplosion()
        }
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        super.onKeyRepeat(key, scancode, modifiers)

        if (key == GLFW_KEY_LEFT) {
            focusScope.previous()
        }
        if (key == GLFW_KEY_RIGHT) {
            focusScope.next()
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
        super.onSceneEnd()

        AudioPlayer.stopSound(bgMusic)
    }
}