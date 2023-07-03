package net.nicksneurons.blastthebox.ecs

import com.fractaldungeon.tools.UpdateListener
import com.fractaldungeon.tools.input.KeyListener
import com.fractaldungeon.tools.input.MouseListener

/**
 * A GameObject is anything that can receive basic events from the engine.
 */
open class GameObject : UpdateListener, KeyListener, MouseListener {
    override fun onUpdate(delta: Double) { }

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) { }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) { }

    override fun onKeyUp(key: Int, scancode: Int, modifiers: Int) { }

    override fun onMouseButtonDown(button: Int, modifiers: Int, x: Double, y: Double) { }

    override fun onMouseButtonUp(button: Int, modifiers: Int, x: Double, y: Double) { }

    override fun onMouseMove(deltaX: Double, deltaY: Double) { }
}

