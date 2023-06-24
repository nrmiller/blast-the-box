package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.*
import net.nicksneurons.blastthebox.ecs.Viewport
import net.nicksneurons.blastthebox.game.scenes.MainScreenScene
import org.lwjgl.system.Configuration

fun main() {

    Configuration.DEBUG.set(true)
    Configuration.DEBUG_STACK.set(true)
    Configuration.DEBUG_MEMORY_ALLOCATOR.set(true)

    val engine = Engine.instance

    engine.choreographer.begin(::MainScreenScene).apply {
        viewport = Viewport.DEFAULT
    }

    engine.window.showDialog()
}

class WindowListener : GLWindowListener {

    override fun onWindowOpened(source: GLWindow?) {

    }

    override fun onWindowClosing(source: GLWindow?) {

    }

    override fun onWindowClosed(source: GLWindow?) {

    }
}