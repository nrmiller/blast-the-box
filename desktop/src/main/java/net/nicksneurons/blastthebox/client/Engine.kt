package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.*
import com.fractaldungeon.tools.input.KeyListener
import com.fractaldungeon.tools.input.MouseListener
import net.nicksneurons.blastthebox.audio.AudioPlayer
import net.nicksneurons.blastthebox.ecs.Choreographer
import net.nicksneurons.blastthebox.game.Fonts
import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC11.*
import org.lwjgl.openal.ALCapabilities
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import java.nio.IntBuffer


class Engine private constructor(): GLEventListener, UpdateListener, MouseListener, KeyListener {

    private val device: Long
    val choreographer = Choreographer()

    private val alContext: Long
    private val alCaps: ALCapabilities
    val window: GLWindow

    var width: Int = 0
        private set

    var height: Int = 0
        private set

    init {
        device = alcOpenDevice(null as String?)
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


        alListenerf(AL_GAIN, 0.2f) // global volume
        alListener3f(AL_POSITION, 0.0f, 0.0f, 0.0f)
        alListener3f(AL_VELOCITY, 0.0f, 0.0f, 0.0f)
        alListenerfv(AL_ORIENTATION, floatArrayOf(
                0.0f, 0.0f, -1.0f, // forward
                0.0f, 1.0f, 0.0f, // up
        ))
        alDistanceModel(AL_INVERSE_DISTANCE_CLAMPED)
//        alDopplerVelocity(1.0f)
//        alDopplerFactor(0.1f)

        window = GLWindow("Blast the Box", 1280, 720)
                .setGLProfile(GLProfile.OPENGL_CORE_PROFILE)
                .setGLClientAPI(GLClientAPI.OPENGL_API)
                .setGLVersion(4, 3)
                .setGLEventListener(this)
                .setUpdateListener(this)
                .setKeyListener(this)
                .setMouseListener(this)
                .addWindowListener(WindowListener())
                .setWindowIconSet("/icon_16.png", "/icon_24.png")
    }

    override fun onSurfaceCreated(width: Int, height: Int) {
        this.width = width
        this.height = height

        Fonts.loadFonts()

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        glEnable(GL_CULL_FACE)

        glClearColor(0.086f, 0.173f, 0.380f, 1.0f)
    }

    override fun onDrawFrame() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        val zOrderedScenes = choreographer.scenes.sortedBy { it.renderLayer }
        zOrderedScenes.forEach {
            it.drawScene(Vector2i(width, height))
        }

//        val free = getRuntime().freeMemory()
//        System.gc()
//        System.runFinalization()
//        println("${getRuntime().freeMemory() - free} bytes freed")
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        this.width = width
        this.height = height

        glViewport(0, 0, width, height)
    }

    private val aspect: Float
        get() = if (height == 0) 1.0f else width.toFloat() / height

    override fun onSurfaceDestroyed() {
        choreographer.finish()
        Fonts.freeFonts()

        // Finalize OpenAL
        MemoryUtil.memFree(alCaps.addressBuffer)
        alcDestroyContext(alContext)
        ALC.destroy()
    }

    override fun onUpdate(delta: Double) {
        AudioPlayer.pollForCompletedSounds()

        choreographer.onUpdate(delta)
    }

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        choreographer.onKeyDown(key, scancode, modifiers)
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        choreographer.onKeyRepeat(key, scancode, modifiers)
    }

    override fun onKeyUp(key: Int, scancode: Int, modifiers: Int) {
        choreographer.onKeyUp(key, scancode, modifiers)
    }

    override fun onMouseButtonDown(button: Int, modifiers: Int, x: Double, y: Double) {
        println("Mouse Event: ${if (button == GLFW_MOUSE_BUTTON_LEFT) "Left" else "Right"} pressed at ($x, $y)")

        choreographer.onMouseButtonDown(button, modifiers, x, y)
    }

    override fun onMouseButtonUp(button: Int, modifiers: Int, x: Double, y: Double) {
        println("Mouse Event: ${if (button == GLFW_MOUSE_BUTTON_LEFT) "Left" else "Right"} released at ($x, $y)")

        choreographer.onMouseButtonUp(button, modifiers, x, y)
    }

    override fun onMouseMove(deltaX: Double, deltaY: Double) {
        choreographer.onMouseMove(deltaX, deltaY)
    }

    companion object {
        @JvmStatic val instance = Engine()
    }
}