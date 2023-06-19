package net.nicksneurons.blastthebox.ecs.components

import com.fractaldungeon.tools.input.MouseListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import net.nicksneurons.blastthebox.client.input.Mouse
import net.nicksneurons.blastthebox.ecs.Component
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.physics.shapes.Collider2D
import net.nicksneurons.blastthebox.utils.Camera.Companion.screenToWorld2D
import org.joml.Vector2f
import java.lang.ref.WeakReference

class ClickableBody2D(var shape: Collider2D) : Component(), MouseListener {

    private var subject: Subject<MouseEvent> = PublishSubject.create()

    fun mouseEvents(): Observable<MouseEvent> {
        return subject
    }

    override fun onAttached(entity: Entity) {
        super.onAttached(entity)

        shape.entity = WeakReference(entity)
    }

    override fun onMouseButtonDown(button: Int, modifiers: Int, x: Double, y: Double) {
        val result = entity!!.scene!!.camera.screenToWorld2D(Vector2f(x.toFloat(), y.toFloat()))

        if (shape.intersectsWith(Vector2f(result.x, result.y))) {
            subject.onNext(MouseDown(button, modifiers, x, y))
        }
    }

    override fun onMouseButtonUp(button: Int, modifiers: Int, x: Double, y: Double) {
        val result = entity!!.scene!!.camera.screenToWorld2D(Vector2f(x.toFloat(), y.toFloat()))

        if (shape.intersectsWith(Vector2f(result.x, result.y))) {
            subject.onNext(MouseUp(button, modifiers, x, y))
        }
    }

    var isHovered: Boolean = false
        private set(value) {
            if (field != value) {

                if (value) {
                    subject.onNext(MouseEnter())
                } else {
                    subject.onNext(MouseExit())
                }

                field = value
            }
        }

    override fun onMouseMove(deltaX: Double, deltaY: Double) {
        val result = entity!!.scene!!.camera.screenToWorld2D(Vector2f(Mouse.position.x.toFloat(), Mouse.position.y.toFloat()))

        isHovered = shape.intersectsWith(Vector2f(result.x, result.y))
        if (isHovered) {
            subject.onNext(MouseMove(deltaX, deltaY))
        }
    }

    override fun free() {
        subject.onComplete()
    }
}

interface MouseEvent
class MouseDown(val button: Int, val modifiers: Int, val x: Double, val y: Double): MouseEvent
class MouseUp(val button: Int, val modifiers: Int, val x: Double, val y: Double): MouseEvent
class MouseMove(val deltaX: Double, val deltaY: Double): MouseEvent
class MouseEnter(): MouseEvent
class MouseExit(): MouseEvent
