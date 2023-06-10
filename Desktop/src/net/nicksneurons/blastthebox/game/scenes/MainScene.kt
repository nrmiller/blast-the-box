package net.nicksneurons.blastthebox.game.scenes

import miller.util.TimedThread
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.audio.AudioClip
import net.nicksneurons.blastthebox.ecs.audio.AudioPlayer
import net.nicksneurons.blastthebox.ecs.components.Transform
import net.nicksneurons.blastthebox.ecs.audio.AudioSource
import net.nicksneurons.blastthebox.ecs.audio.Spatialization
import net.nicksneurons.blastthebox.game.entities.BoxFactory
import org.joml.Vector3f

class MainScene : Scene() {

    override fun onSceneBegin() {

        // Setup scene
        val factory = BoxFactory()

        val grayBox = factory.createGrayBox()
        entities.add(grayBox)

        grayBox.getComponent<Transform>()!!.position = Vector3f(1.0f - 0.5f, -0.5f, 0.0f)

        val grayBox1 = factory.createGreenBox()
        entities.add(grayBox1)

        grayBox1.getComponent<Transform>()!!.position = Vector3f(-1.0f - 0.5f, -0.5f, 0.0f)

        val grayBox2 = factory.createRedBox()
        entities.add(grayBox2)

        grayBox2.getComponent<Transform>()!!.position = Vector3f(-0.5f, -0.5f, 0.0f)
        grayBox2.getComponent<Transform>()!!.rotation = Vector3f(Math.PI.toFloat() / 2.0f, 0.0f, 0.0f);

        val grayBox4 = factory.createBlueBox()
        entities.add(grayBox4)

        grayBox4.getComponent<Transform>()!!.position = Vector3f(-0.5f, -0.5f, 0.5f)
        grayBox4.getComponent<Transform>()!!.rotation = Vector3f(0.0f, 0.0f, Math.PI.toFloat() / 4.0f)

        val source = AudioSource(AudioClip("/audio/tracks/loop_two.ogg")).apply {
            relative = true
            pitch = 1.0f
            gain = 0.8f
        }
        AudioPlayer.loopSound(source)

        // hacky way of getting a one-shot timer
        object: TimedThread() {
            override fun update() {
                Thread.sleep(2000)
                grayBox4.queueFree()

                AudioPlayer.playSound(AudioSource(AudioClip("/audio/sounds/shield.ogg")).apply {
                    relative = true
                    pitch = 1.0f
                    gain = 1.0f
                    spatialization = Spatialization.Enabled
//                    position = Vector3f(1.0f, 0.0f, 0.0f)
//                    isDirectional = true
//                    direction = Vector3f(1.0f, 1.0f, 0.0f)
                })

                stop()
            }
        }.start()
    }
}