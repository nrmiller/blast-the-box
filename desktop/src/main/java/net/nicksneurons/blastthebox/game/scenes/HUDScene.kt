package net.nicksneurons.blastthebox.game.scenes

import io.reactivex.rxjava3.disposables.Disposable
import net.nicksneurons.blastthebox.client.Engine
import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.SortMode
import net.nicksneurons.blastthebox.game.Fonts
import net.nicksneurons.blastthebox.game.data.*
import net.nicksneurons.blastthebox.game.entities.BonusScoreEntity
import net.nicksneurons.blastthebox.game.entities.HealthHUD
import net.nicksneurons.blastthebox.graphics.text.TextSprite
import net.nicksneurons.blastthebox.utils.Camera2D
import org.joml.Vector3f
import javax.inject.Inject

class HUDScene : Scene() {

    @Inject
    lateinit var highscoreRepository : HighscoreRepository
    @Inject
    lateinit var scoreService: ScoreService
    @Inject
    lateinit var playerService: PlayerService

    lateinit var subscription: Disposable

    lateinit var scoreSprite: TextSprite
    lateinit var scoreEntity: Entity
    lateinit var healthHUDEntity : HealthHUD

    override fun onSceneBegin() {
        super.onSceneBegin()
        Engine.instance.di.inject(this)

        camera = Camera2D()
        ySortingEnabled = true
        ySortMode = SortMode.Ascending

        scoreSprite = TextSprite(Fonts.alphanumeric)
        scoreEntity = addEntity(Entity().apply {
            addComponent(scoreSprite)
        })

        healthHUDEntity = addEntity(HealthHUD(playerService.getPlayer()!!))

        subscription = scoreService.scoreUpdates().subscribe {
            if (it is BonusScoreEvent) {
                queueAdd(BonusScoreEntity(it.amount.toInt()))
            }
        }
    }

    override fun onUpdate(delta: Double) {

        val width = Engine.instance.width
        val height = Engine.instance.height

        val playPauseWidth = 64.0f
        val playPauseHeight = 64.0f

        scoreSprite.sizePx = 64
        scoreSprite.text = scoreService.getCurrentScore().toInt().toString()
        scoreEntity.transform.position = Vector3f(playPauseWidth, height - playPauseHeight, 1.0f)

        getAllEntities<BonusScoreEntity>().forEach {
            it.sprite.sizePx = 64
            it.anchor = Vector3f(scoreEntity.transform.position).apply {
                y = height - playPauseHeight * 2
            }
        }

        healthHUDEntity.transform.position = Vector3f((width - healthHUDEntity.component.desiredWidth).toFloat(), (height - healthHUDEntity.component.desiredHeight).toFloat(), 1.0f)


        // Since we are mutating the entities in this scene, we should call this last.
        super.onUpdate(delta)
    }

    override fun onSceneEnd() {
        super.onSceneEnd()

        subscription.dispose()
    }

}
