package net.nicksneurons.blastthebox.game.scenes

import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.game.Game.settings
import net.nicksneurons.blastthebox.game.GameSettings
import net.nicksneurons.blastthebox.game.entities.*
import net.nicksneurons.blastthebox.utils.Camera3D
import net.nicksneurons.blastthebox.utils.S
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*

class GameScene: Scene() {

    lateinit var bgMusic: AudioSource

    val floors = arrayListOf<FloorTile>()
    val rows = mutableListOf<Row>()

    val eyeHeight = 1.2f//0.7f
    val strafeSpeed = 10.0f // m/s
    val movementSpeed = 20.0f // m/s

    override fun onSceneBegin() {

        bgMusic = AudioSource(AudioClip("/audio/tracks/loop_one.ogg")).apply {
            pitch = 1.0f
            gain = 0.8f
        }
        AudioPlayer.loopSound(bgMusic)

        (camera as Camera3D).apply {
            setPosition(0.0f, eyeHeight, 0.0f)
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

        if (glfwGetKey(Engine.instance.window.handle, GLFW_KEY_LEFT) == GLFW_TRUE) {
            (camera as Camera3D).strafeLeft((delta * strafeSpeed).toFloat())
        }
        if (glfwGetKey(Engine.instance.window.handle, GLFW_KEY_RIGHT) == GLFW_TRUE) {
            (camera as Camera3D).strafeRight((delta * strafeSpeed).toFloat())
        }

        if (glfwGetKey(Engine.instance.window.handle, GLFW_KEY_ESCAPE) == GLFW_TRUE) {
            Engine.instance.choreographer.end(this)
            Engine.instance.choreographer.begin(::MainScreenScene)
        }


        // to spawn a row at every meter
        // the spawn time should be inversely correlated to the movement speed
        accumulatedDistance += delta * movementSpeed
        if (accumulatedDistance > 1.0) {
            accumulatedDistance -= 1.0

            rows.add(addEntity(initializeRow(settings)))
        }

        moveCloser(delta.toFloat() * movementSpeed)
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

    /**
     * The maximum number of cubes that stretch from left to right.
     */
    var FIELD_WIDTH = 20

    /**
     * The multiple specifying how many cubes should appear in a row. (on average)
     * 100 being the highest, 0 being the lowest.
     */
    var DENSITY = 10

    private var seq_width = 0
    private var seq_index = 0
    private var seq_first = true
    private var sequencing = false
    private var cooldown = 0

    private var isChecker = false
    private var checkerSize = 0
    private var checkerCooldown = 0
    private var checkerOffset = 0
    private var isEven = true

    fun initializeRow(settings: GameSettings) : Row {
        val seq: Int = S.ran.nextInt(1000)
        if (seq > 992 && !sequencing && cooldown <= 0) // Every now and then, sequencing will start.
        {
            println("Starting sequence: $seq")
            //cooldown prevents sequencing from happening directly after a sequence.
            cooldown = 100
            sequencing = true
            seq_width = S.ran.nextInt(3) + 8
            seq_index = S.ran.nextInt(FIELD_WIDTH)
            if (seq_index > FIELD_WIDTH - seq_width) //If index is beyond right side of field, change it.
            {
                seq_index = FIELD_WIDTH - seq_width
            }
        }
        else if (seq > 990 && !isChecker) {
            isChecker = true
            checkerSize = S.ran.nextInt(4) + 1
            checkerCooldown = checkerSize + 2
//            checkerOffset = S.ran.nextInt(CubePopulator.FIELD_WIDTH)
            isEven = S.ran.nextBoolean()
        }

        if (sequencing) //Blocks are now in an orderly format. (Paths created within them)
        {
            if (seq_first) {
                seq_first = false
            }
            if (S.ran.nextInt(50) >= 30 && seq_width > 2) {
                seq_width -= 1
            }
            val n: Int = S.ran.nextInt(100)
            if (n >= 80 && seq_width > 2) {
                seq_index += 1
            } else if (n >= 60 && n < 80 && seq_width > 2) {
                seq_index -= 1
            }
            if (seq_width <= 3) //If width becomes small enough, create opportunity to stop sequencing.
            {
                if (S.ran.nextInt(100) > 60) {
                    sequencing = false
                    seq_first = true
                }
            }
            if (seq_index < 0) //Make sure that the index and width stay in bounds. (Left-side)
            {
                seq_index = 0
                if (seq_width <= 1) {
                    seq_width = 2
                }
            }

            return SequencedRow(startDistance = 40.0f, seq_width, seq_index, seq_first)
        }
        else if (isChecker) {
            checkerCooldown -= 1
            if (checkerCooldown in 0..2) {
                return Row(startDistance = 40.0f)
            }
            else if (checkerCooldown < 0) {
                checkerCooldown = checkerSize + 2

                if (S.ran.nextInt(100) > 5) {
                    isEven = !isEven
                    checkerOffset = S.ran.nextInt(2) * 1 * if (S.ran.nextBoolean()) -1 else 1
                } else {
                    isChecker = false
                }
            }

            return EvenOddRow(isEven, checkerOffset, checkerSize, startDistance = 40.0f)
        }
        else {
            cooldown -= 1
            if (cooldown < 0) {
                cooldown = 0
            }

            return StandardRow(settings, startDistance = 40.0f)
        }
    }
}