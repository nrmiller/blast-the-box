package net.nicksneurons.blastthebox.ecs

import com.fractaldungeon.tools.UpdateListener
import com.fractaldungeon.tools.input.KeyListener
import com.fractaldungeon.tools.input.MouseListener
import java.util.*

/**
 * This class is used to control which scenes are currently being rendered.
 *
 * A single instance of it exists on the [net.nicksneurons.blastthebox.client.Engine].
 *
 *
 */
class Choreographer: UpdateListener, MouseListener, KeyListener {

    private val scenesToBegin = mutableListOf<Scene>()
    private val scenesToEnd = mutableListOf<Scene>()

    private val mutableScenes = mutableListOf<Scene>()
    val scenes: List<Scene> = Collections.unmodifiableList(mutableScenes)

    /**
     * Convenient syntax to begin a new scene
     * @return the scene of type [T]
     */
    fun <T: Scene> begin(factory: () -> T): Scene {
        val scene = factory()
        begin(scene)
        return scene
    }

    fun begin(scene: Scene) {
        if (mutableScenes.contains(scene) || scenesToBegin.contains(scene))
            throw Exception("Scene already began!")

        scenesToBegin.add(scene)
    }

    fun end(scene: Scene) {
        if (!mutableScenes.contains(scene) || scenesToEnd.contains(scene))
            throw Exception("Scene already ended!")

        scenesToEnd.add(scene)
    }

    fun finish() {
        scenesToBegin.clear()
        scenesToEnd.clear()
        mutableScenes.reversed().forEach { scene ->
            scene.free()
            mutableScenes.remove(scene)
        }
    }

    override fun onUpdate(delta: Double) {
        scenes.forEach {
            it.onUpdate(delta)
        }

        for (scene in scenesToEnd) {
            scene.onSceneEnd()
            scene.free()
            mutableScenes.remove(scene)
        }
        scenesToEnd.clear()

        for (scene in scenesToBegin) {
            mutableScenes.add(scene)
            scene.onSceneBegin()
        }
        scenesToBegin.clear()
    }

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        scenes.forEach {
            it.onKeyDown(key, scancode, modifiers)
        }
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        scenes.forEach {
            it.onKeyRepeat(key, scancode, modifiers)
        }
    }

    override fun onKeyUp(key: Int, scancode: Int, modifiers: Int) {
        scenes.forEach {
            it.onKeyUp(key, scancode, modifiers)
        }
    }

    override fun onMouseButtonDown(button: Int, modifiers: Int, x: Double, y: Double) {
        scenes.forEach {
            it.onMouseButtonDown(button, modifiers, x, y)
        }
    }

    override fun onMouseButtonUp(button: Int, modifiers: Int, x: Double, y: Double) {
        scenes.forEach {
            it.onMouseButtonUp(button, modifiers, x, y)
        }
    }

    override fun onMouseMove(deltaX: Double, deltaY: Double) {
        scenes.forEach {
            it.onMouseMove(deltaX, deltaY)
        }
    }
}