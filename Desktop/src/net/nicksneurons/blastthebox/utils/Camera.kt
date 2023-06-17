package net.nicksneurons.blastthebox.utils

import org.joml.Matrix4f
import org.joml.Vector2i

interface Camera {

    fun getViewMatrix() : Matrix4f
    fun getProjectionMatrix(screenSize: Vector2i): Matrix4f
    
}