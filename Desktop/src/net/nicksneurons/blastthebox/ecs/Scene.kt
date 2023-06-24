package net.nicksneurons.blastthebox.ecs

import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.components.RenderableComponent
import net.nicksneurons.blastthebox.utils.Camera
import net.nicksneurons.blastthebox.utils.Camera.Companion.createInverseProjectionViewMatrix
import net.nicksneurons.blastthebox.utils.Camera3D
import org.joml.Matrix4f
import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.GLFW_KEY_F3
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL33
import org.lwjgl.system.MemoryUtil
import java.util.*

open class Scene() : GameObject() {
    var name: String? = null

    val choreographer = Engine.instance.choreographer

    private val mutableEntities = mutableListOf<Entity>()
    val entities: List<Entity> = Collections.unmodifiableList(mutableEntities)

    var camera: Camera = Camera3D()

    var renderLayer: Int = 0
    var viewport: Viewport = Viewport.DEFAULT

    private val matrixBuffer = MemoryUtil.memAllocFloat(16)

    open fun onSceneBegin() {}
    open fun onSceneEnd() {}

    fun <T: Entity> addEntity(entity: T): T {
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
        for (entity in entities) {
            entity.onUpdate(delta)
        }

        freeMarkedEntities()
    }

    fun drawScene(screenSize: Vector2i) {

        glViewport(
                (viewport.x * screenSize.x).toInt(),
                (viewport.y * screenSize.y).toInt(),
                (viewport.width * screenSize.x).toInt(),
                (viewport.height * screenSize.y).toInt())

        val projectionMatrix = camera.createProjectionMatrix()
        val viewMatrix = camera.createViewMatrix()

        val projViewMatrix = Matrix4f()
        projectionMatrix.mul(viewMatrix, projViewMatrix)
        val invProjViewMatrix = camera.createInverseProjectionViewMatrix()

        val renderables = entities.flatMap { it.getAllComponents<RenderableComponent>() }

        // we need to transform the entity to projection-view space before we can sort,
        // effectively this sorts against the distance from the camera.
        val zOrderedRenderables = renderables.sortedBy {

            invProjViewMatrix.transform(it.entity!!.transform.getWorldPosition()).z
        }.sortedBy { it.renderLayer }

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

            transform.getWorldTransform().get(matrixBuffer)
            GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(material.program.id, "model"), false, matrixBuffer)

            renderable.draw()
        }
    }

    private fun printRenderBatch() {

        val invProjViewMatrix = camera.createInverseProjectionViewMatrix()

        val renderables = entities.flatMap { it.getAllComponents<RenderableComponent>() }

        // we need to transform the entity to projection-view space before we can sort,
        // effectively this sorts against the distance from the camera.
        val zOrderedRenderables = renderables.sortedBy {
            invProjViewMatrix.transform(it.entity!!.transform.getWorldPosition()).z
        }.sortedBy { it.renderLayer }

        println("=== START OF BATCH ===")
        for (renderable in zOrderedRenderables) {
            val entity = renderable.entity!!

            val entityName = entity.name ?: entity.javaClass.kotlin.simpleName
            val componentName = renderable.name ?: renderable.javaClass.kotlin.simpleName

            println("(layer=${renderable.renderLayer}) ${entity.transform} $entityName<$componentName>")
        }
        println("=== END OF BATCH ===")
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

        if (key == GLFW_KEY_F3) {
            printRenderBatch()
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

    fun finishScene() {
        choreographer.end(this)
    }

    fun <T : Scene> transitionTo(nextScene: () -> T) {
        finishScene()
        choreographer.begin(nextScene)
    }
}
