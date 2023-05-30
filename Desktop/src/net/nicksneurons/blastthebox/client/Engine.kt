package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.GLEventListener
import com.fractaldungeon.tools.UpdateListener
import net.nicksneurons.blastthebox.geometry.Square
import org.lwjgl.opengl.GL11

class Engine : GLEventListener, UpdateListener {

    val square = Square()

    override fun onSurfaceCreated() {
        square.init()
    }

    override fun onDrawFrame() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

        square.draw();
    }

    override fun onSurfaceChanged(width: Int, height: Int) {

    }

    override fun onSurfaceDestroyed() {

    }

    override fun onUpdate() {

    }
}