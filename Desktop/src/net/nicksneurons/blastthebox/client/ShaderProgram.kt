package net.nicksneurons.blastthebox.client

import org.lwjgl.opengl.GL33.*

class ShaderProgram constructor(vertexShaderSource: String, fragmentShaderSource: String) {

    private var program: Int = 0

    init {
        program = glCreateProgram()
        val vertexShader = glCreateShader(GL_VERTEX_SHADER)
        val fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(vertexShader, vertexShaderSource)
        glCompileShader(vertexShader)
        val vStatus = glGetShaderi(vertexShader, GL_COMPILE_STATUS)
        if (vStatus == GL_FALSE) // failed to compile
        {
            println("Vertex shader failed to compile!\n" + "=".repeat(80))
            println(glGetShaderInfoLog(vertexShader).prependIndent("    "))
        }
        glAttachShader(program, vertexShader)
        glShaderSource(fragmentShader, fragmentShaderSource)
        glCompileShader(fragmentShader)
        val fStatus = glGetShaderi(fragmentShader, GL_COMPILE_STATUS)
        if (fStatus == GL_FALSE) // failed to compile
        {
            println("Fragment shader failed to compile!\n" + "=".repeat(80))
            println(glGetShaderInfoLog(fragmentShader).prependIndent("    "))
        }
        glAttachShader(program, fragmentShader)
        glLinkProgram(program)
        val lStatus = glGetProgrami(program, GL_LINK_STATUS)
        if (lStatus == GL_FALSE)
        {
            println("Shader program failed to link!\n" + "=".repeat(80))
            println(glGetProgramInfoLog(program).prependIndent("    "))
        }

        // Delete the shader objects
        glDetachShader(program, vertexShader)
        glDetachShader(program, fragmentShader)
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
    }

    fun use() {
        glUseProgram(program)
    }

    fun delete() {
        glDeleteProgram(program)
    }
}