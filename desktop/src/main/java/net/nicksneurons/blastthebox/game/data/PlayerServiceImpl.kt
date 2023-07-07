package net.nicksneurons.blastthebox.game.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import net.nicksneurons.blastthebox.game.entities.Player
import javax.inject.Inject

class PlayerServiceImpl @Inject constructor() : PlayerService {

    private var player : Player? = null

    override fun setPlayer(player: Player) {
        this.player = player
    }

    override fun getPlayer(): Player? {
        return player
    }
}
