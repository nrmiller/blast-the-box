package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.tools.UpdateListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.Component
import org.lwjgl.glfw.GLFW

class DoubleTapGestureRecognizer: Component(), UpdateListener {

    private val subject : Subject<DoubleTappedEvent> = PublishSubject.create()

    // If the key is released for too long, reset gesture recognition
    val timeReleasedUpperLimit = 75.0 / 1000.0

    // If the key is held too long, reset gesture recognition
    val timeHeldUpperLimit = 300.0 / 1000.0

    // When key is held too long, we need to wait for a period before processing
    // gesture recognition again
    val timeToReleaseForReset = 300.0 / 1000.0

    private var timeLeftHeld = 0.0
    private var timeLeftReleased = 0.0
    private var needsToDebounceLeftRelease = false
    var isLeftDoubleTapped = false
        private set(value) {
            if (field != value) {
                field = value
                if (field) {
                    subject.onNext(DoubleTappedLeft())
                }
            }
        }

    private var timeRightHeld = 0.0
    private var timeRightReleased = 0.0
    private var needsToDebounceRightRelease = false
    var isRightDoubleTapped = false
        private set(value) {
            if (field != value) {
                field = value
                if (field) {
                    subject.onNext(DoubleTappedRight())
                }
            }
        }

    fun doubleTappedEvents() : Observable<DoubleTappedEvent> {
        return subject
    }

    override fun onUpdate(delta: Double) {
        if (GLFW.glfwGetKey(Engine.instance.window.handle, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_TRUE) {
            // reset right
            timeRightHeld = 0.0
            timeRightReleased = 0.0
            isRightDoubleTapped = false

            if (isLeftDoubleTapped || (!needsToDebounceLeftRelease && timeLeftHeld > 0 && timeLeftReleased > 0)) {
                // hold the double tap until it is released
                timeLeftReleased = 0.0
                timeLeftHeld = 0.0
                isLeftDoubleTapped = true
            } else {
                timeLeftHeld += delta

                if (timeLeftHeld > timeHeldUpperLimit) {
                    // reset gesture
                    needsToDebounceLeftRelease = true
                    timeLeftReleased = 0.0
                }
            }
        }
        else {
            isLeftDoubleTapped = false

            if (needsToDebounceLeftRelease) {
                // needed for resetting the gesture
                timeLeftReleased += delta
                if (timeLeftReleased > timeToReleaseForReset) {
                    needsToDebounceLeftRelease = false
                    timeLeftHeld = 0.0
                    timeLeftReleased = 0.0
                }
            } else if (timeLeftHeld > 0) {

                timeLeftReleased += delta

                if (timeLeftReleased > timeReleasedUpperLimit) {
                    // reset gesture
                    timeLeftHeld = 0.0
                    timeLeftReleased = 0.0
                }
            }
        }


        if (GLFW.glfwGetKey(Engine.instance.window.handle, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_TRUE) {
            // reset left
            timeLeftHeld = 0.0
            timeLeftReleased = 0.0
            isLeftDoubleTapped = false

            if (isRightDoubleTapped || (!needsToDebounceRightRelease && timeRightHeld > 0 && timeRightReleased > 0)) {
                // hold the double tap until it is released
                timeRightReleased = 0.0
                timeRightHeld = 0.0
                isRightDoubleTapped = true
            } else {
                timeRightHeld += delta

                if (timeRightHeld > timeHeldUpperLimit) {
                    // reset gesture
                    needsToDebounceRightRelease = true
                    timeRightReleased = 0.0
                }
            }
        }
        else {
            isRightDoubleTapped = false

            if (needsToDebounceRightRelease) {
                // needed for resetting the gesture
                timeRightReleased += delta
                if (timeRightReleased > timeToReleaseForReset) {
                    needsToDebounceRightRelease = false
                    timeRightHeld = 0.0
                    timeRightReleased = 0.0
                }
            } else if (timeRightHeld > 0) {

                timeRightReleased += delta

                if (timeRightReleased > timeReleasedUpperLimit) {
                    // reset gesture
                    timeRightHeld = 0.0
                    timeRightReleased = 0.0
                }
            }
        }
    }

    override fun free() {
        super.free()

        subject.onComplete()
    }
}

open class DoubleTappedEvent { }
class DoubleTappedLeft : DoubleTappedEvent() { }
class DoubleTappedRight : DoubleTappedEvent() { }