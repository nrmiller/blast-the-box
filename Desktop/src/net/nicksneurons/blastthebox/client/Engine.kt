package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.GLEventListener
import com.fractaldungeon.tools.input.KeyListener
import com.fractaldungeon.tools.UpdateListener
import com.fractaldungeon.tools.input.MouseListener
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.ecs.components.Texture
import net.nicksneurons.blastthebox.ecs.components.Transform
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL33.*
import kotlin.math.cos
import kotlin.math.sin

class Engine : GLEventListener, UpdateListener, MouseListener, KeyListener {

    private var width: Int = 0
    private var height: Int = 0

    private lateinit var program: ShaderProgram
//    private val square = Square()

    private val distance = 20.0f
    private val eyeHeight = 5.0f
    private val angularSpeed = Math.PI

    private var angle = Math.PI * 0.5 //0.0

    override fun onSurfaceCreated() {

        glEnable(GL_DEPTH_TEST)

        val vertexShaderSource = javaClass.getResource("/shaders/shader.vert").readText()
        val fragmentShaderSource = javaClass.getResource("/shaders/shader.frag").readText()
        program = ShaderProgram(vertexShaderSource, fragmentShaderSource)
        program.use()
    }

    override fun onDrawFrame() {
        val scene = currentScene ?: return

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        val buffer = BufferUtils.createFloatBuffer(16)
        Matrix4f()
                .perspective(Math.toRadians(45.0).toFloat(), aspect, 0.01f, 100.0f) // projection
                .lookAt((distance * cos(angle)).toFloat(), eyeHeight, (distance * sin(angle)).toFloat(), // view
                    0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f)
                .get(buffer)
        glUniformMatrix4fv(glGetUniformLocation(program.id, "projectionView"), false, buffer)

        buffer.clear()

        val renderables = scene.entities.filter { it.hasComponent<Mesh>() && it.hasComponent<Transform>() }
        var zOrderedRenderables = renderables.sortedBy { it.getComponent<Transform>()!!.position.z }
        for (renderable in zOrderedRenderables) {
            var transform = renderable.getComponent<Transform>()!!
            var primitive = renderable.getComponent<Mesh>()!!.primitive

            Matrix4f()
                    .rotate(transform.rotation.x, Vector3f(1.0f, 0.0f, 0.0f))
                    .rotate(transform.rotation.y, Vector3f(0.0f, 1.0f, 0.0f))
                    .rotate(transform.rotation.z, Vector3f(0.0f, 0.0f, 1.0f))
                    .translate(transform.position.x.toFloat(), transform.position.y.toFloat(), transform.position.z.toFloat())
//                    .rotate(transform.rotation.x, transform.rotation.y, transform.rotation.z) // todo need quaternions
                    .scale(transform.scale.x, transform.scale.y, transform.scale.z)
                    .get(buffer)
            glUniformMatrix4fv(glGetUniformLocation(program.id, "model"), false, buffer)

            if (renderable.hasComponent<Texture>()) {
                val texture = renderable.getComponent<Texture>()!!

                texture.bind()
            }

            primitive.draw()
        }
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    private val aspect: Float
        get() = if (height == 0) 1.0f else width.toFloat() / height

    override fun onSurfaceDestroyed() {

    }


    var currentScene: Scene? = null
        private set

    private var requestedScene: Scene? = null
    fun setScene(scene: Scene) {
        requestedScene = scene
    }

    override fun onUpdate(delta: Double) {
        val scene = currentScene

        angle += angularSpeed * delta
        if (scene != null) {
            scene.onUpdate(delta)
            for (entity in scene.entities) {
                entity.onUpdate(delta)
            }

            freeMarkedEntities(currentScene)
        }

        // Perform scene change at the end
        if (requestedScene != null) {
            onSceneChanged(currentScene, requestedScene)
            requestedScene = null
        }
    }

    private fun onSceneChanged(oldScene: Scene?, newScene: Scene?) {
        oldScene?.onSceneEnd()
        freeScene(oldScene)

        currentScene = newScene

        newScene?.onSceneBegin()
    }

    private fun freeScene(scene: Scene?) {
        if (scene == null) return

        for (entity in scene.entities) {
            entity.free()
        }
        scene.entities.clear()
    }
    private fun freeMarkedEntities(scene: Scene?) {
        if (scene == null) return

        for (index in scene.entities.size - 1 downTo 0) {
            val entity = scene.entities[index]
            if (entity.isMarkedForDeletion) {
                entity.free()
                scene.entities.removeAt(index)
            } else {
                freeMarkedComponents(entity)
            }
        }
    }

    private fun freeMarkedComponents(entity: Entity) {
        for (index in entity.components.size - 1 downTo 0) {
            val component = entity.components[index]
            if (component.isMarkedForDeletion) {
                component.free()
                entity.components.removeAt(index)
            }
        }
    }

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        val scene = currentScene ?: return

        scene.onKeyDown(key, scancode, modifiers)
        for (entity in scene.entities) {
            entity.onKeyDown(key, scancode, modifiers)
        }
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        val scene = currentScene ?: return

        scene.onKeyRepeat(key, scancode, modifiers)
        for (entity in scene.entities) {
            entity.onKeyRepeat(key, scancode, modifiers)
        }
    }

    override fun onKeyUp(key: Int, scancode: Int, modifiers: Int) {
        val scene = currentScene ?: return

        scene.onKeyUp(key, scancode, modifiers)
        for (entity in scene.entities) {
            entity.onKeyUp(key, scancode, modifiers)
        }
    }

    override fun onMouseButtonDown(button: Int, modifiers: Int, x: Double, y: Double) {
        println("Mouse Event: ${if (button == GLFW_MOUSE_BUTTON_LEFT) "Left" else "Right"} pressed at ($x, $y)")

        val scene = currentScene ?: return

        scene.onMouseButtonDown(button, modifiers, x, y)
        for (entity in scene.entities) {
            entity.onMouseButtonDown(button, modifiers, x, y)
        }
    }

    override fun onMouseButtonUp(button: Int, modifiers: Int, x: Double, y: Double) {
        println("Mouse Event: ${if (button == GLFW_MOUSE_BUTTON_LEFT) "Left" else "Right"} released at ($x, $y)")

        val scene = currentScene ?: return

        scene.onMouseButtonUp(button, modifiers, x, y)
        for (entity in scene.entities) {
            entity.onMouseButtonUp(button, modifiers, x, y)
        }
    }
}