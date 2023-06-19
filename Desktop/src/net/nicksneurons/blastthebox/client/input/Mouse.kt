package net.nicksneurons.blastthebox.client.input

import net.nicksneurons.blastthebox.client.Engine
import org.joml.Vector2d
import org.lwjgl.glfw.GLFW.glfwGetCursorPos
import org.lwjgl.system.MemoryStack.stackPush

class Mouse {

    companion object {

        /**
         * Gets the position of the mouse cursor on the game window.
         */
        val position: Vector2d
            get() {
                stackPush().use() {
                    val xBuf = it.mallocDouble(1)
                    val yBuf = it.mallocDouble(1)

                    glfwGetCursorPos(Engine.instance.window.handle, xBuf, yBuf)

                    return Vector2d(xBuf.get(0), yBuf.get(0))
                }
            }
    }
}