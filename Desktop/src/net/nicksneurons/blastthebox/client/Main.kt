package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.*
import net.nicksneurons.blastthebox.ecs.Viewport
import net.nicksneurons.blastthebox.game.scenes.MainScene
import net.nicksneurons.blastthebox.game.scenes.MainScreenScene
import org.lwjgl.system.Configuration

fun main() {

    Configuration.DEBUG.set(true)
    Configuration.DEBUG_STACK.set(true)
    Configuration.DEBUG_MEMORY_ALLOCATOR.set(true)

    val engine = Engine.instance

    engine.choreographer.begin(::MainScene).apply {
        viewport = Viewport.DEFAULT
    }

    engine.choreographer.begin(::MainScreenScene).apply {
        viewport = Viewport.DEFAULT
    }

    GLWindow("Blast the Box", 1280, 720)
            .setGLProfile(GLProfile.OPENGL_CORE_PROFILE)
            .setGLClientAPI(GLClientAPI.OPENGL_API)
            .setGLVersion(4, 2)
            .setGLEventListener(engine)
            .setUpdateListener(engine)
            .setKeyListener(engine)
            .setMouseListener(engine)
            .addWindowListener(WindowListener())
            .setWindowIconSet("/icon_16.png", "/icon_24.png")
            .showDialog()
}

class WindowListener : GLWindowListener {

    override fun onWindowOpened(source: GLWindow?) {

    }

    override fun onWindowClosing(source: GLWindow?) {

    }

    override fun onWindowClosed(source: GLWindow?) {

    }
}