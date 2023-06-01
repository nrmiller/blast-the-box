package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.GLEventListener
import com.fractaldungeon.tools.UpdateListener
import miller.opengl.Point3d
import net.nicksneurons.blastthebox.geometry.Square
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import kotlin.math.cos
import kotlin.math.sin

class Engine : GLEventListener, UpdateListener {

    private var width: Int = 0
    private var height: Int = 0

    private lateinit var program: ShaderProgram
    private val square = Square()

    private val distance = 20.0f
    private val eyeHeight = 5.0f
    private val angularSpeed = Math.PI

    private var angle = 0.0

    override fun onSurfaceCreated() {

        val vertexShaderSource = javaClass.getResource("/shaders/shader.vert").readText()
        val fragmentShaderSource = javaClass.getResource("/shaders/shader.frag").readText()
        program = ShaderProgram(vertexShaderSource, fragmentShaderSource)
        program.use()

        square.init()
        square.location = Point3d(-0.5, -0.5, 0.0)
    }

    override fun onUpdate(delta: Double) {
        angle += angularSpeed * delta
    }

    override fun onDrawFrame() {
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
        Matrix4f()
                .translate(square.location.x.toFloat(), square.location.y.toFloat(), square.location.z.toFloat())
                .scale(square.dim.width.toFloat(), square.dim.height.toFloat(), 1.0f)
                .get(buffer)
        glUniformMatrix4fv(glGetUniformLocation(program.id, "model"), false, buffer)
        square.draw()
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    private val aspect: Float
        get() = if (height == 0) 1.0f else width.toFloat() / height

    override fun onSurfaceDestroyed() {

    }
}