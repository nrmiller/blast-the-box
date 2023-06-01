package net.nicksneurons.blastthebox.client

import com.fractaldungeon.tools.*
import net.nicksneurons.blastthebox.game.scenes.MainScene

fun main() {

    val engine = Engine()
    engine.setScene(MainScene())

    GLWindow("Blast the Box", 1280, 720)
            .setGLProfile(GLProfile.OPENGL_CORE_PROFILE)
            .setGLClientAPI(GLClientAPI.OPENGL_API)
            .setGLVersion(3, 3)
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