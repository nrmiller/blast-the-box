package net.nicksneurons.blastthebox.client

import org.lwjgl.opengl.GL33.*

class ShaderProgram constructor(vertexShaderSource: String, fragmentShaderSource: String) {

    var id: Int = 0
        private set

    init {
        id = glCreateProgram()
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
        glAttachShader(id, vertexShader)
        glShaderSource(fragmentShader, fragmentShaderSource)
        glCompileShader(fragmentShader)
        val fStatus = glGetShaderi(fragmentShader, GL_COMPILE_STATUS)
        if (fStatus == GL_FALSE) // failed to compile
        {
            println("Fragment shader failed to compile!\n" + "=".repeat(80))
            println(glGetShaderInfoLog(fragmentShader).prependIndent("    "))
        }
        glAttachShader(id, fragmentShader)
        glLinkProgram(id)
        val lStatus = glGetProgrami(id, GL_LINK_STATUS)
        if (lStatus == GL_FALSE)
        {
            println("Shader program failed to link!\n" + "=".repeat(80))
            println(glGetProgramInfoLog(id).prependIndent("    "))
        }

        // Delete the shader objects
        glDetachShader(id, vertexShader)
        glDetachShader(id, fragmentShader)
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
    }

    fun use() {
        glUseProgram(id)
    }

    fun delete() {
        glDeleteProgram(id)
    }
}