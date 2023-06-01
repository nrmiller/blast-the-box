package net.nicksneurons.blastthebox.ecs

import com.fractaldungeon.tools.UpdateListener
import com.fractaldungeon.tools.input.*

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
}

open class Scene : GameObject() {
    var name: String? = null

    val entities = mutableListOf<Entity>()

    open fun onSceneBegin() {}
    open fun onSceneEnd() {}

    inline fun <reified T: Entity> getEntity(name: String? = null): T? {
        return entities.firstOrNull {
            // Return first matching entity of the type, or additionally with the matching name
            it is T && (name == null || it.name == name)
        } as T?
    }
}

open class Entity : GameObject() {
    var name: String? = null

    val components = mutableListOf<Component>()

    var isMarkedForDeletion: Boolean = false
    fun queueFree() {
        isMarkedForDeletion = true
    }
    inline fun <reified T: Component> getComponent(name: String? = null): T? {
        return components.firstOrNull {
            // Return first matching component of the type, or additionally with the matching name
            it is T && (name == null || it.name == name)
        } as T?
    }

    inline fun <reified T: Component> hasComponent(): Boolean {
        return components.any { it is T }
    }

    open fun free() {
        for (component in components) {
            component.free()
        }
        components.clear()
    }
}

open class Component {
    var name: String?  = null

    var isMarkedForDeletion: Boolean = false
    fun queueFree() {
        isMarkedForDeletion = true
    }

    /**
     * Should be overridden to free any resources associated with the component
     */
    open fun free() {

    }
}