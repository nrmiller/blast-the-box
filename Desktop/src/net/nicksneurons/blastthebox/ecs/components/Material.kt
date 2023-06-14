package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.client.ShaderProgram
import net.nicksneurons.blastthebox.ecs.Component

class Material(
        val vertexShaderResourcePath: String,
        val fragmentShaderResourcePath: String) {

    val program: ShaderProgram

    init {

        val vertexShaderSource = javaClass.getResource(vertexShaderResourcePath).readText()
        val fragmentShaderSource = javaClass.getResource(fragmentShaderResourcePath).readText()

        program = ShaderProgram(vertexShaderSource, fragmentShaderSource)
    }

    companion object {
        @JvmStatic val DEFAULT = Material("/shaders/default_shader.vert", "/shaders/default_shader.frag")
    }
}