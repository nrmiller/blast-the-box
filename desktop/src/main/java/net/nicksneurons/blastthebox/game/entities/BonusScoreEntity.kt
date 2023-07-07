package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.game.Fonts
import net.nicksneurons.blastthebox.graphics.text.TextSprite
import org.joml.Vector3f

class BonusScoreEntity(val amount: Int) : Entity() {

    var anchor = Vector3f()

    var animationSpeed = 256.0 // px per s
    var animationDuration = 0.5  // seconds

    private var timeRemaining = 0.0

    val sprite: TextSprite = addComponent(TextSprite(Fonts.yellowNumbers))

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        animateBonus(amount)
    }

    fun animateBonus(amount: Int) {
        sprite.text = "+$amount"

        // Reset
        timeRemaining = animationDuration
        isVisible = true
    }

    override fun onUpdate(delta: Double) {
        super.onUpdate(delta)

        val percentComplete = if (animationDuration > 0) (1 - timeRemaining / animationDuration) else 1.0
        val lerp = percentComplete * (animationSpeed * animationDuration)

        transform.position = Vector3f(anchor.x, (anchor.y - lerp).toFloat(), anchor.z)

        timeRemaining -= delta
        if (timeRemaining <= 0.0) {
            timeRemaining = 0.0
            isVisible = false
            queueFree()
        }
    }
}