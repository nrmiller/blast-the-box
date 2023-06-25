package net.nicksneurons.blastthebox.game.scenes

import net.nicksneurons.blastthebox.audio.AudioClip
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.audio.AudioSource
import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.game.Game.settings
import net.nicksneurons.blastthebox.game.GameSettings
import net.nicksneurons.blastthebox.game.entities.*
import net.nicksneurons.blastthebox.game.sequencing.RowSequencer
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

    private val sequencer = RowSequencer()

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

            val row = sequencer.generateRow()
            rows.add(addEntity(row).apply {
                transform.position.z = -40.0f
            })

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
}