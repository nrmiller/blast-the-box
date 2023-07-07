package net.nicksneurons.blastthebox.client.dagger

import dagger.*
import net.nicksneurons.blastthebox.game.data.*
import net.nicksneurons.blastthebox.game.entities.Box
import net.nicksneurons.blastthebox.game.scenes.GameScene
import net.nicksneurons.blastthebox.game.scenes.HUDScene
import javax.inject.Singleton

@Singleton
@Component(modules = [
    GameModule::class
])
interface AppComponent {

     fun inject(instance: HUDScene)
     fun inject(instance: GameScene)

     fun inject(instance: Box)
}

@Module()
abstract class GameModule {

    @Singleton
    @Binds
    abstract fun bindPlayerService(playerService : PlayerServiceImpl) : PlayerService

    @Singleton
    @Binds
    abstract fun bindScoreService(scoreServiceImpl: ScoreServiceImpl) : ScoreService

    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideHighscoresRepository() : HighscoreRepository {
            return HighscoreRepositoryImpl()
        }
    }
}