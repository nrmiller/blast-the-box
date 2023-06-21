package net.nicksneurons.blastthebox.ecs

import com.fractaldungeon.tools.UpdateListener
import com.fractaldungeon.tools.input.*
import net.nicksneurons.blastthebox.ecs.components.RenderableComponent
import net.nicksneurons.blastthebox.ecs.components.Transform
import net.nicksneurons.blastthebox.utils.Camera
import net.nicksneurons.blastthebox.utils.Camera3D
import org.joml.Matrix4f
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL33
import org.lwjgl.system.MemoryUtil
import java.util.*

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

open class Scene : GameObject() {
    var name: String? = null

    private val mutableEntities = mutableListOf<Entity>()
    val entities: List<Entity> = Collections.unmodifiableList(mutableEntities)

    var camera: Camera = Camera3D()

    var renderLayer: Int = 0
    var viewport: Viewport = Viewport.DEFAULT

    private val matrixBuffer = MemoryUtil.memAllocFloat(16)

    open fun onSceneBegin() {}
    open fun onSceneEnd() {}

    fun addEntity(entity: Entity): Entity {
        entity.onAddedToScene(this)
        mutableEntities.add(entity)
        return entity
    }

    fun addEntities(entities: Iterable<Entity>) {
        entities.forEach { addEntity(it) }
    }

    fun removeEntity(entity: Entity) : Boolean {
        if (mutableEntities.contains(entity)) {
            mutableEntities.remove(entity)
            entity.onRemovedFromScene()
            return true
        }
        return false
    }

    fun removeEntity(name: String) {
        val result = entities.firstOrNull { name == it.name }
        if (result != null) {
            removeEntity(result)
        }
    }

    operator fun get(name: String): Entity {
        return entities.first {it.name == name }
    }

    inline fun <reified T: Entity> getEntity(name: String? = null): T? {
        return entities.firstOrNull {
            // Return first matching entity of the type, or additionally with the matching name
            it is T && (name == null || it.name == name)
        } as T?
    }

    override fun onUpdate(delta: Double) {
        // todo move into a scene with a real camera
        angle += angularSpeed * delta


        for (entity in entities) {
            entity.onUpdate(delta)
        }

        freeMarkedEntities()
    }

    // todo move into a scene with a real camera
    private val distance = 20.0f
    private val eyeHeight = 5.0f
    private val angularSpeed = Math.PI / 4
    private var angle = Math.PI * 0.5 //0.0
    fun drawScene(screenSize: Vector2i) {

        val aspect = if (screenSize.y == 0) 1.0f else screenSize.x.toFloat() / screenSize.y

        glViewport(
                (viewport.x * screenSize.x).toInt(),
                (viewport.y * screenSize.y).toInt(),
                (viewport.width * screenSize.x).toInt(),
                (viewport.height * screenSize.y).toInt())

        val projectionMatrix = camera.createProjectionMatrix()
        val viewMatrix = camera.createViewMatrix()


        val projViewMatrix = Matrix4f()
        val invProjViewMatrix = Matrix4f()
        projectionMatrix.mul(viewMatrix, projViewMatrix)
        projViewMatrix.invert(invProjViewMatrix)

        val renderables = entities.flatMap { it.getAllComponents<RenderableComponent>() }

        // we need to transform the entity to projection-view space before we can sort,
        // effectively this sorts against the distance from the camera.
        val zOrderedRenderables = renderables.sortedByDescending { invProjViewMatrix.transform(Vector4f(it.entity!!.transform.position, 1.0f)).z }

        for (renderable in zOrderedRenderables) {
            val transform = renderable.entity!!.transform

            val material = renderable.material
            material.program.use()


            projectionMatrix.get(matrixBuffer)
            GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(material.program.id, "projection"), false, matrixBuffer)
            viewMatrix.get(matrixBuffer)
            GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(material.program.id, "view"), false, matrixBuffer)
            projViewMatrix.get(matrixBuffer)
            GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(material.program.id, "projectionView"), false, matrixBuffer)
            matrixBuffer.clear()

            Matrix4f()
                    .rotate(transform.rotation.x, Vector3f(1.0f, 0.0f, 0.0f))
                    .rotate(transform.rotation.y, Vector3f(0.0f, 1.0f, 0.0f))
                    .rotate(transform.rotation.z, Vector3f(0.0f, 0.0f, 1.0f))
                    .translate(transform.position.x, transform.position.y, transform.position.z)
//                    .rotate(transform.rotation.x, transform.rotation.y, transform.rotation.z) // todo need quaternions
                    .scale(transform.scale.x, transform.scale.y, transform.scale.z)
                    .get(matrixBuffer)
            GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(material.program.id, "model"), false, matrixBuffer)

            renderable.draw()
        }
    }

    private fun freeMarkedEntities() {
        for (index in entities.size - 1 downTo 0) {
            val entity = entities[index]
            if (entity.isMarkedForDeletion) {
                entity.free()
                mutableEntities.removeAt(index)
            } else {
                freeMarkedComponents(entity)
            }
        }
    }

    private fun freeMarkedComponents(entity: Entity) {
        for (index in entity.components.size - 1 downTo 0) {
            val component = entity.components[index]
            if (component.isMarkedForDeletion) {
                component.free()
                entity.removeComponentAt(index)
            }
        }
    }

    fun free() {
        for (entity in entities) {
            entity.free()
        }
        mutableEntities.clear()

        MemoryUtil.memFree(matrixBuffer)
    }

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        entities.forEach {
            it.onKeyDown(key, scancode, modifiers)
        }
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        entities.forEach {
            it.onKeyRepeat(key, scancode, modifiers)
        }
    }

    override fun onKeyUp(key: Int, scancode: Int, modifiers: Int) {
        entities.forEach {
            it.onKeyUp(key, scancode, modifiers)
        }
    }

    override fun onMouseButtonDown(button: Int, modifiers: Int, x: Double, y: Double) {
        entities.forEach {
            it.onMouseButtonDown(button, modifiers, x, y)
        }
    }

    override fun onMouseButtonUp(button: Int, modifiers: Int, x: Double, y: Double) {
        entities.forEach {
            it.onMouseButtonUp(button, modifiers, x, y)
        }
    }

    override fun onMouseMove(deltaX: Double, deltaY: Double) {
        entities.forEach {
            it.onMouseMove(deltaX, deltaY)
        }
    }
}

open class Entity(var name: String? = null) : GameObject() {

    val transform = Transform()

    private val mutableComponents = mutableListOf<Component>()
    val components: List<Component> = Collections.unmodifiableList(mutableComponents)

    var isMarkedForDeletion: Boolean = false
    fun queueFree() {
        isMarkedForDeletion = true
    }

    fun addComponent(component: Component) {
        component.onAttached(this)
        mutableComponents.add(component)
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

    var entity: Entity? = null

    open fun onAttached(entity: Entity) {
        this.entity = entity
    }

    open fun onDetached() {
        queueFree()
    }
}