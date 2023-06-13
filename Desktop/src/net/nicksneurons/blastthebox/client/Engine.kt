package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.GLEventListener
import com.fractaldungeon.tools.UpdateListener
import com.fractaldungeon.tools.input.KeyListener
import com.fractaldungeon.tools.input.MouseListener
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.audio.AudioPlayer
import net.nicksneurons.blastthebox.ecs.components.*
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC11.*
import org.lwjgl.openal.ALCapabilities
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.cos
import kotlin.math.sin


class Engine private constructor(): GLEventListener, UpdateListener, MouseListener, KeyListener {

    private lateinit var matrixBuffer: FloatBuffer

    private val alContext: Long
    private val alCaps: ALCapabilities

    private var width: Int = 0
    private var height: Int = 0

    private lateinit var defaultMaterial: Material

    private val distance = 20.0f
    private val eyeHeight = 5.0f
    private val angularSpeed = Math.PI

    private var angle = Math.PI * 0.5 //0.0

    init {
        val device = alcOpenDevice(null as String?)
        alContext = alcCreateContext(device, null as IntBuffer?)
        println("Device: ${alcGetString(device, ALC_ALL_DEVICES_SPECIFIER)}")
        alcMakeContextCurrent(alContext)

        alCaps = AL.createCapabilities(ALC.createCapabilities(device), MemoryUtil::memCallocPointer)

        println("ALC_FREQUENCY     : " + alcGetInteger(device, ALC_FREQUENCY) + "Hz")
        println("ALC_REFRESH       : " + alcGetInteger(device, ALC_REFRESH) + "Hz")
        println("ALC_SYNC          : " + (alcGetInteger(device, ALC_SYNC) == ALC_TRUE))
        println("ALC_MONO_SOURCES  : " + alcGetInteger(device, ALC_MONO_SOURCES))
        println("ALC_STEREO_SOURCES: " + alcGetInteger(device, ALC_STEREO_SOURCES))

        //See https://en.wikipedia.org/wiki/Head-related_transfer_function
//        val num_hrtf = alcGetInteger(device, ALC_NUM_HRTF_SPECIFIERS_SOFT)
//        println("ALC_NUM_HRTF_SPEICIFIERS_SOFT: " + num_hrtf)
//        val attr = BufferUtils.createIntBuffer(10)
//                .put(ALC_HRTF_SOFT)
//                .put(ALC_TRUE)
//                .put(ALC_HRTF_ID_SOFT)
//                .put(0)
//                .flip()
//
//        alcResetDeviceSOFT(device, attr)


//        val hrtf_state = alcGetInteger(device, ALC_HRTF_SOFT)
//        if (hrtf_state == 0) {
//            System.out.format("HRTF not enabled!\n")
//        } else {
//            val name = alcGetString(device, ALC_HRTF_SPECIFIER_SOFT)
//            System.out.format("HRTF enabled, using %s\n", name)
//        }

//        alEnable(AL_STEREO_ANGLES)


        alListenerf(AL_GAIN, 1.0f) // global volume
        alListener3f(AL_POSITION, 0.0f, 0.0f, 0.0f)
        alListener3f(AL_VELOCITY, 0.0f, 0.0f, 0.0f)
        alListenerfv(AL_ORIENTATION, floatArrayOf(
                0.0f, 0.0f, -1.0f, // forward
                0.0f, 1.0f, 0.0f, // up
        ))
        alDistanceModel(AL_INVERSE_DISTANCE_CLAMPED)
//        alDopplerVelocity(1.0f)
//        alDopplerFactor(0.1f)
    }

    override fun onSurfaceCreated() {

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        defaultMaterial = Material("/shaders/default_shader.vert", "/shaders/default_shader.frag")

        matrixBuffer = MemoryUtil.memAllocFloat(16)
    }

    override fun onDrawFrame() {
        val scene = currentScene ?: return

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        val renderables = scene.entities.filter { it.hasComponent<Mesh>() && it.hasComponent<Transform>() }
        var zOrderedRenderables = renderables.sortedBy { it.getComponent<Transform>()!!.position.z }
        for (renderable in zOrderedRenderables) {
            var transform = renderable.getComponent<Transform>()!!
            var primitive = renderable.getComponent<Mesh>()!!.primitive

            val material = chooseMaterial(renderable)
            material.program.use()

            Matrix4f()
                    .perspective(Math.toRadians(45.0).toFloat(), aspect, 0.01f, 100.0f) // projection
                    .lookAt((distance * cos(angle)).toFloat(), eyeHeight, (distance * sin(angle)).toFloat(), // view
                            0.0f, 0.0f, 0.0f,
                            0.0f, 1.0f, 0.0f)
                    .get(matrixBuffer)
            glUniformMatrix4fv(glGetUniformLocation(material.program.id, "projectionView"), false, matrixBuffer)
            matrixBuffer.clear()

            Matrix4f()
                    .rotate(transform.rotation.x, Vector3f(1.0f, 0.0f, 0.0f))
                    .rotate(transform.rotation.y, Vector3f(0.0f, 1.0f, 0.0f))
                    .rotate(transform.rotation.z, Vector3f(0.0f, 0.0f, 1.0f))
                    .translate(transform.position.x, transform.position.y, transform.position.z)
//                    .rotate(transform.rotation.x, transform.rotation.y, transform.rotation.z) // todo need quaternions
                    .scale(transform.scale.x, transform.scale.y, transform.scale.z)
                    .get(matrixBuffer)
            glUniformMatrix4fv(glGetUniformLocation(material.program.id, "model"), false, matrixBuffer)

            if (renderable.hasComponent<Texture>()) {
                val texture = renderable.getComponent<Texture>()!!

                texture.bind()
            } else if (renderable.hasComponent<TextureAtlas>()) {
                val texture = renderable.getComponent<TextureAtlas>()!!

                texture.bind()
            }

            primitive.draw()
        }

//        val free = getRuntime().freeMemory()
//        System.gc()
//        System.runFinalization()
//        println("${getRuntime().freeMemory() - free} bytes freed")
    }

    private fun chooseMaterial(renderable: Entity): Material {
        return if (renderable.hasComponent<Material>()) {
            renderable.getComponent()!!
        } else {
            defaultMaterial
        }
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        this.width = width
        this.height = height

        glViewport(0, 0, width, height)
    }

    private val aspect: Float
        get() = if (height == 0) 1.0f else width.toFloat() / height

    override fun onSurfaceDestroyed() {
        MemoryUtil.memFree(matrixBuffer)

        // Finalize OpenAL
        MemoryUtil.memFree(alCaps.addressBuffer)
        alcDestroyContext(alContext)
        ALC.destroy()
    }


    var currentScene: Scene? = null
        private set

    private var requestedScene: Scene? = null
    fun setScene(scene: Scene) {
        requestedScene = scene
    }

    override fun onUpdate(delta: Double) {
        val scene = currentScene

        AudioPlayer.pollForCompletedSounds()

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
                entity.removeComponentAt(index)
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

    companion object {
        @JvmStatic val instance = Engine()
    }
}