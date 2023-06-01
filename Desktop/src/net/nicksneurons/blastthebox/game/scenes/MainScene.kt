package net.nicksneurons.blastthebox.game.scenes

import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.Transform
import net.nicksneurons.blastthebox.game.entities.BoxFactory
import org.joml.Vector3f

class MainScene : Scene() {

    override fun onSceneBegin() {

        // Setup scene
        val factory = BoxFactory()

        val grayBox = factory.createGrayBox()
        entities.add(grayBox)

        grayBox.getComponent<Transform>()!!.position = Vector3f(1.0f - 0.5f, -0.5f, 0.0f)

        val grayBox1 = factory.createGrayBox()
        entities.add(grayBox1)

        grayBox1.getComponent<Transform>()!!.position = Vector3f(-1.0f - 0.5f, -0.5f, 0.0f)

        val grayBox2 = factory.createGrayBox()
        entities.add(grayBox2)

        grayBox2.getComponent<Transform>()!!.position = Vector3f(-0.5f, -0.5f, 0.0f)
        grayBox2.getComponent<Transform>()!!.rotation = Vector3f(Math.PI.toFloat() / 2.0f, 0.0f, 0.0f);

        val grayBox4 = factory.createGrayBox()
        entities.add(grayBox4)

        grayBox4.getComponent<Transform>()!!.position = Vector3f(-0.5f, -0.5f, 0.5f)
        grayBox4.getComponent<Transform>()!!.rotation = Vector3f(0.0f, 0.0f, Math.PI.toFloat() / 4.0f)
    }
}