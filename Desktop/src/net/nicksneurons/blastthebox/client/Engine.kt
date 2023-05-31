package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.GLEventListener
import com.fractaldungeon.tools.UpdateListener
import net.nicksneurons.blastthebox.geometry.Square
import org.lwjgl.opengl.GL11.*

class Engine : GLEventListener, UpdateListener {

    private lateinit var program: ShaderProgram
    private val square = Square()

    override fun onSurfaceCreated() {

        val vertexShaderSource = javaClass.getResource("/shaders/shader.vert").readText()
        val fragmentShaderSource = javaClass.getResource("/shaders/shader.frag").readText()
        program = ShaderProgram(vertexShaderSource, fragmentShaderSource)
        program.use()

        square.init()
    }

    override fun onDrawFrame() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        square.draw()
    }

    override fun onSurfaceChanged(width: Int, height: Int) {

    }

    override fun onSurfaceDestroyed() {

    }

    override fun onUpdate() {

    }
}