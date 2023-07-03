package net.nicksneurons.blastthebox.game.scenes

import com.fractaldungeon.tools.UpdateListener
import org.joml.Vector4f

class StopwatchTracker : UpdateListener {

    var movementSpeedMultiplier: Float = 1.0f
        private set

    private var slowMotion = false

    fun slowTime() {
        slowMotion = true
        movementSpeedMultiplier = 0.5f
        clearColor = Vector4f(1.0f, 1.0f, 1.0f, 1.0f)
    }

    private var tickTimeRemaining = 0.1

    var clearColor = GameScene.clearColor
    val targetColor = Vector4f(1.0f, 1.0f, 1.0f, 1.0f)

    override fun onUpdate(delta: Double) {
        if (!slowMotion)
            return

        // change variables in 100ms intervals
        tickTimeRemaining -= delta
        if (tickTimeRemaining < 0) {
            tickTimeRemaining = 0.1
            onTick()
        }
    }

    private fun onTick() {
        movementSpeedMultiplier += 0.005f
        clearColor.x -= (targetColor.x - GameScene.clearColor.x) / 100
        clearColor.y -= (targetColor.y - GameScene.clearColor.y) / 100
        clearColor.z -= (targetColor.z - GameScene.clearColor.z) / 100
        clearColor.w -= (targetColor.w - GameScene.clearColor.w) / 100

        // todo animate fog variables

        if (movementSpeedMultiplier > 1) {
            movementSpeedMultiplier = 1.0f
            slowMotion = false
            clearColor = GameScene.clearColor
        }
    }
}