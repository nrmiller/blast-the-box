package net.nicksneurons.blastthebox.ecs

import com.fractaldungeon.tools.UpdateListener
import com.fractaldungeon.tools.input.KeyListener
import com.fractaldungeon.tools.input.MouseListener
import java.lang.ref.WeakReference
import java.util.*

open class Entity(var name: String? = null) : GameObject() {

    val transform = Transform().also {
        it.entity = WeakReference(this)
    }

    private val mutableComponents = mutableListOf<Component>()
    val components: List<Component> = Collections.unmodifiableList(mutableComponents)

    var isMarkedForDeletion: Boolean = false
    fun queueFree() {
        isMarkedForDeletion = true
    }

    fun <T : Component> addComponent(component: T): T {
        component.onAttached(this)
        mutableComponents.add(component)
        return component
    }

    fun addComponents(components: Iterable<Component>) {
        components.forEach { addComponent(it) }
    }

    fun removeComponent(component: Component) {
        if (mutableComponents.contains(component)) {
            mutableComponents.remove(component)
            component.onDetached()
        }
    }

    fun removeComponentAt(index: Int): Component? {
        if (components.isNotEmpty() && index >= 0 && index < components.size){
            val component = mutableComponents.removeAt(index)
            component.onDetached()
            return component
        }
        return null
    }

    inline fun <reified T: Component> getComponent(name: String? = null): T? {
        return components.firstOrNull {
            // Return first matching component of the type, or additionally with the matching name
            it is T && (name == null || it.name == name)
        } as T?
    }

    fun getComponentAt(index: Int): Component? {
        if (components.isNotEmpty() && index >= 0 && index < components.size) {
            return components[index]
        }
        return null
    }

    inline fun <reified T: Component> getAllComponents(): Iterable<T> {
        return components.filterIsInstance<T>()
    }

    inline fun <reified T: Component> hasComponent(): Boolean {
        return components.any { it is T }
    }

    override fun onUpdate(delta: Double) {
        for (component in components) {
            if (component is UpdateListener) {
                component.onUpdate(delta)
            }
        }
    }

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        components.filterIsInstance<KeyListener>().forEach {
            it.onKeyDown(key, scancode, modifiers)
        }
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        components.filterIsInstance<KeyListener>().forEach {
            it.onKeyRepeat(key, scancode, modifiers)
        }
    }

    override fun onKeyUp(key: Int, scancode: Int, modifiers: Int) {
        components.filterIsInstance<KeyListener>().forEach {
            it.onKeyUp(key, scancode, modifiers)
        }
    }

    override fun onMouseButtonDown(button: Int, modifiers: Int, x: Double, y: Double) {
        components.filterIsInstance<MouseListener>().forEach {
            it.onMouseButtonDown(button, modifiers, x, y)
        }
    }

    override fun onMouseButtonUp(button: Int, modifiers: Int, x: Double, y: Double) {
        components.filterIsInstance<MouseListener>().forEach {
            it.onMouseButtonUp(button, modifiers, x, y)
        }
    }

    override fun onMouseMove(deltaX: Double, deltaY: Double) {
        components.filterIsInstance<MouseListener>().forEach {
            it.onMouseMove(deltaX, deltaY)
        }
    }

    open fun free() {
        for (component in components) {
            component.free()
        }
        mutableComponents.clear()
    }

    var scene: Scene? = null
    open fun onAddedToScene(scene: Scene) {
        this.scene = scene
    }

    open fun onRemovedFromScene() {
        queueFree()
    }
}
