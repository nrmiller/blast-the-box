package net.nicksneurons.blastthebox.game.scenes

import miller.util.jomlextensions.toVector3f
import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.game.Powerup
import net.nicksneurons.blastthebox.game.PowerupType
import net.nicksneurons.blastthebox.game.data.HighscoreRepository
import net.nicksneurons.blastthebox.game.data.PlayerService
import net.nicksneurons.blastthebox.game.data.ScoreService
import net.nicksneurons.blastthebox.game.entities.*
import net.nicksneurons.blastthebox.game.sequencing.RowSequencer
import net.nicksneurons.blastthebox.physics.Physics
import net.nicksneurons.blastthebox.utils.Camera3D
import net.nicksneurons.blastthebox.utils.S
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.glClearColor
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

class GameScene: Scene() {

    @Inject
    lateinit var highscoreRepository : HighscoreRepository
    @Inject
    lateinit var scoreService: ScoreService
    @Inject
    lateinit var playerService: PlayerService

    val stopwatchTracker = StopwatchTracker()

    lateinit var bgMusic: AudioSource

    val floors = arrayListOf<FloorTile>()
    val rows = mutableListOf<Row>()

    val scoreSpeed = 100.0f // 100 pts per second

    lateinit var player: Player
    lateinit var hudScene: HUDScene

    private val sequencer = RowSequencer()

    override fun onSceneBegin() {
        super.onSceneBegin()

        Engine.instance.di.inject(this)

        player = addEntity(Player())

        // Before showing the HUD, set the player
        playerService.setPlayer(player)

        hudScene = choreographer.begin(::HUDScene)

        bgMusic = AudioSource(AudioClip("/audio/tracks/loop_one.ogg")).apply {
            pitch = 1.0f
            gain = 0.8f
        }
        AudioPlayer.loopSound(bgMusic)

        (camera as Camera3D).apply {
            setPosition(0.0f, player.eyeHeight, 0.0f)
            setFocusPoint(0.0f, 0f, -10.0f)
            moveBackward(0.0f)
            fov = 45.0f
        }

        floors.add(addEntity(FloorTile().apply {
            transform.position = Vector3f(-10.0f, 0.0f, 0.0f)
        }))
        floors.add(addEntity(FloorTile().apply {
            transform.position = Vector3f(-10.0f, 0.0f, -20.0f)
        }))
        floors.add(addEntity(FloorTile().apply {
            transform.position = Vector3f(-10.0f, 0.0f, -40.0f)
        }))
        floors.add(addEntity(FloorTile().apply {
            transform.position = Vector3f(-10.0f, 0.0f, -60.0f)
        }))

    }

    private var accumulatedDistance: Double = 0.0

    override fun onUpdate(delta: Double) {
        super.onUpdate(delta)

        if (glfwGetKey(Engine.instance.window.handle, GLFW_KEY_ESCAPE) == GLFW_TRUE) {
            Engine.instance.choreographer.end(this)
            Engine.instance.choreographer.end(hudScene)
            Engine.instance.choreographer.begin(::MainScreenScene)
            return
        }
        if (glfwGetKey(Engine.instance.window.handle, GLFW_KEY_SPACE) == GLFW_TRUE) {
            player.gun.fire()
        }


        val bobAngle = Math.toRadians(3.0).toFloat() * (player.xVelocity.length() / player.strafeSpeed) * (player.xVelocity.x.sign)

        (camera as Camera3D).apply {
            setPosition(player.transform.position.x, position.y, position.z)
            setFocusPoint(player.transform.position.x, lookAt.y, lookAt.z)

            val angleOffUp = (Math.PI.toFloat() / 2.0f) - bobAngle.toDouble()
            val targetUp = Vector3f(cos(angleOffUp).toFloat(), sin(angleOffUp).toFloat(), 0.0f)
            setUpVector(targetUp.x, targetUp.y, targetUp.z)
        }


        // to spawn a row at every meter
        // the spawn time should be inversely correlated to the movement speed
        accumulatedDistance += delta * player.movementSpeed
        if (accumulatedDistance > 1.0) {
            accumulatedDistance -= 1.0

            val row = sequencer.generateRow() // TestRow()
            rows.add(addEntity(row).apply {
                transform.position.z = -40.0f
            })

        }

        moveCloser(delta.toFloat() * player.movementSpeed)
        scoreService.addScore(delta.toFloat() * scoreSpeed)

        stopwatchTracker.onUpdate(delta)
        glClearColor(stopwatchTracker.clearColor.x, stopwatchTracker.clearColor.y, stopwatchTracker.clearColor.z, stopwatchTracker.clearColor.w)
        player.movementSpeedMultipier = stopwatchTracker.movementSpeedMultiplier

        val powerups = getAllEntities<Powerup>()
        Physics.checkCollisionsWith(player, powerups, ::onCollectPowerup)

        val boxes = getAllEntities<Box>()
        Physics.checkCollisionsWith(player, boxes, ::onTouchedBox)
    }

    private fun onCollectPowerup(powerup: Powerup) {
        when (powerup.type) {
            PowerupType.HEART -> {
                AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/heartbeat.ogg")), true)
                player.health++
            }
            PowerupType.SHIELD -> {
                AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/shield.ogg")), true)
                player.hasShield = true
            }
            PowerupType.STRENGTH_ONE -> {
                AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/green_up.ogg")), true)
                player.strength = 2
            }
            PowerupType.STRENGTH_TWO -> {
                AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/blue_up.ogg")), true)
                player.strength = 3
            }
            PowerupType.AMMO -> {
                AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/reload.ogg")).apply {
                    gain = 4.0f
                }, true)
                player.gun.ammo += S.ran.nextInt(4) + 4
            }
            PowerupType.TRIPLE_FIRE -> {
                AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/triple.ogg")), true)
                player.hasTripleFire = true
            }
            PowerupType.PIERCE -> {
                AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/pierce.ogg")), true)
                player.gun.piercingBullets += S.ran.nextInt(2)  + 2
            }
            PowerupType.NUKE -> {
                performNuclearExplosion()
            }
            PowerupType.STOPWATCH -> {
                performStopwatch()
            }
            else -> { }
        }
        powerup.queueFree()
    }

    private fun performNuclearExplosion() {
        val tallies = mutableMapOf<BoxType, Int>()
        AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/nuclear_explosion.ogg")), true)
        for (row in rows) {
            for (box in row.boxes) {
                if (box.isMarkedForDeletion)
                    continue

                val position = box.transform.getWorldPosition().toVector3f()
                spawnExplosion(Vector3f(position.x, position.y, position.z + 1.0f))
                tallies[box.initialType] = (tallies[box.initialType] ?: 0) + 1
                box.queueFree()
            }
        }

        val points = tallies.entries.sumOf {
            it.key.points * it.value
        }

        scoreService.addBonusScore(points.toFloat())
    }

    private fun spawnExplosion(position: Vector3f) {
        val explosion = Explosion(player, position, shouldPlaySound = false)
        queueAdd(explosion)
    }

    private fun performStopwatch() {
        AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/stopwatch.ogg")), true)
        stopwatchTracker.slowTime()
    }

    private fun onTouchedBox(box: Box) {
        player.doDamage(1)
    }

    fun moveCloser(amount: Float) {
        floors.forEach {
            it.transform.position.z += amount

            if (it.transform.position.z > 20.0f) {
                it.transform.position.z -= floors.size * 20.0f
            }
        }

        rows.reversed().forEach {
            it.transform.position.z += amount

            if (it.transform.position.z > 1.0f) {
                rows.remove(it)
                it.free()
            }
        }
    }

    override fun onSceneEnd() {
        super.onSceneEnd()

        AudioPlayer.stopSound(bgMusic)
    }

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        super.onKeyDown(key, scancode, modifiers)

        if (key == GLFW_KEY_N) {
            performNuclearExplosion()
        }
        if (key == GLFW_KEY_S) {
            performStopwatch()
        }
    }

    companion object {
        @JvmStatic val clearColor : Vector4f
            get() = Vector4f(0.086f, 0.173f, 0.380f, 1.0f)
    }
}

class TestRow() : Row() {
    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        if (S.ran.nextInt(100) > 95) {
            scene.addEntity(Box.createRandom(0)).also {
                it.getComponent<Mesh>()!!.renderLayer = 1
                it.transform.parent = this.transform
            }
        }
    }
}