package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.*

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback

fun main() {

    glfwInit()

    // Show GLFW errors if debugging is enabled.
//    if (BuildConfig.DEBUG_MODE && BuildConfig.GLFW_DEBUGGING_ENABLED) {
        GLFWErrorCallback.createPrint(System.err).set()
//    }

    val engine = Engine()

    val window = GameWindow("My Game", 1280, 720)
//    window.animator = DefaultAnimator() // todo this doesn't work on MacOS since we need to run on MainThread
//    window.setGLProfile(GLProfile.OPENGL_COMPAT_PROFILE)
    window.setGLClientAPI(GLClientAPI.OPENGL_API)
    window.setGLVersion(3, 3)
    window.glEventListener = engine
    window.updateListener = engine
    window.addWindowListener(WindowListener())

    window.showDialog()
}

class WindowListener : GLWindowListener {

    override fun onWindowOpened(source: GLWindow?) {

    }

    override fun onWindowClosing(source: GLWindow?) {

    }

    override fun onWindowClosed(source: GLWindow?) {
//        glfwTerminate()
    }
}