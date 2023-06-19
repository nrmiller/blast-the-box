package net.nicksneurons.blastthebox.client

import net.nicksneurons.blastthebox.graphics.geometry.Square
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl.opengl.GL33.*

fun main() {
    HelloWorld()
}

class HelloWorld() {

    private var program: ShaderProgram

    private val square: Square
    private var window: Long = 0

    init {
        init()
        square = Square()

        val vertexShaderSource = javaClass.getResource("/shaders/default_shader.vert").readText()
        val fragmentShaderSource = javaClass.getResource("/shaders/default_shader.frag").readText()
        program = ShaderProgram(vertexShaderSource, fragmentShaderSource)

        loop()
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
    }

    private fun init() {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)

        window = glfwCreateWindow(1280, 720, "Hello World", NULL, NULL)
        if (window == NULL)
            throw RuntimeException("Failed to create the GLFW window")

        glfwSetKeyCallback(window) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
        }

        stackPush().use {
            val pWidth = it.mallocInt(1)
            val pHeight = it.mallocInt(1)

            glfwGetWindowSize(window, pWidth, pHeight)

            val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
            if (vidMode != null) {
                glfwSetWindowPos(
                        window,
                        (vidMode.width() - pWidth.get(0)) / 2,
                        (vidMode.height() - pHeight.get(0)) / 2)
            }
        }

        glfwMakeContextCurrent(window)
        GL.createCapabilities()

        glfwSwapInterval(1)
        glfwShowWindow(window)
    }

    private fun loop() {

        var v = Vector3f(0.0f, 1.0f, 0.0f)
        v.add(Vector3f(1.0f, 0.0f, 0.0f))

        v = Matrix4f().translate(2.0f, 0.0f, 0.0f).scale(0.5f).transformPosition(v)

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        glPointSize(20.0f)
        glLineWidth(5.0f)

        program.use();
        square.init()

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            square.draw()

            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }
}