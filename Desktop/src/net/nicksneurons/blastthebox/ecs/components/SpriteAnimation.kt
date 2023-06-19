package net.nicksneurons.blastthebox.ecs.components

import com.fractaldungeon.tools.UpdateListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import net.nicksneurons.blastthebox.geometry.Square
import org.joml.Math.floor

class SpriteAnimation(
        val textureAtlas: TextureAtlas,
        var frames: Int,
        var frameDurationMs: Int): RenderableComponent(), UpdateListener {

    private val primitive = Square()

    val totalDuration
        get() = frames * (frameDurationMs / 1000.0)

    var animationState: AnimationState = AnimationState.Initial
        private set(value) {
            field = value
            subject.onNext(field)
        }

    var loopMode: LoopMode = LoopMode.Once
        private set

    var elapsedTime = 0.0
        private set

    var currentFrame: Int = 0
        set(value) {
            field = value
            textureAtlas.index = currentFrame
        }

    private var subject: Subject<AnimationState> = PublishSubject.create()

    fun animationStateChanges(): Observable<AnimationState> {
        return subject
    }

    override fun onUpdate(delta: Double) {
        if (animationState != AnimationState.Running)
            return

        elapsedTime += delta

        if (loopMode == LoopMode.Once && elapsedTime > totalDuration) {
            animationState = AnimationState.Completed
            elapsedTime = 0.0
            currentFrame = 0
            return
        }

        if (loopMode == LoopMode.ClampForever && elapsedTime > totalDuration) {
            animationState = AnimationState.Completed
            currentFrame = frames - 1
            return
        }

        if (loopMode == LoopMode.Loop && elapsedTime > totalDuration) {
            elapsedTime -= totalDuration
        }

        val frame = floor(elapsedTime / (frameDurationMs / 1000.0)).toInt()
        currentFrame = frame.mod(frames)
    }

    fun play(mode: LoopMode = LoopMode.Loop) {
        currentFrame = 0
        animationState = AnimationState.Running
        elapsedTime = 0.0
        loopMode = mode
    }

    fun pause() {
        animationState = AnimationState.Paused
    }

    fun resume() {
        animationState = AnimationState.Running
    }

    fun stop() {
        currentFrame = 0
        animationState = AnimationState.Initial
    }

    override fun draw() {
        textureAtlas.bind()
        primitive.draw()
    }

    override fun free() {
        super.free()

        primitive.free()
        textureAtlas.free()
        subject.onComplete()
    }
}

enum class LoopMode {
    Once,
    Loop,
    ClampForever
}

enum class AnimationState {
    Initial,
    Running,
    Paused,
    Completed
}