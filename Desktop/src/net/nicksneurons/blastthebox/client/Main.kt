package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.GLWindow
import com.fractaldungeon.tools.GLWindowListener
import com.fractaldungeon.tools.GameWindow

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwInit

fun main() {

    glfwInit()
    val window = GameWindow("My Game", 600, 480)

    window.showDialog()
}

class Main : GLWindowListener {



    override fun onWindowOpened(source: GLWindow?) {
        TODO("Not yet implemented")
    }

    override fun onWindowClosing(source: GLWindow?) {
        TODO("Not yet implemented")
    }

    override fun onWindowClosed(source: GLWindow?) {
        TODO("Not yet implemented")
    }
}