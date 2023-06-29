package net.nicksneurons.blastthebox.game.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import net.nicksneurons.blastthebox.game.entities.Player

class PlayerServiceImpl : PlayerService {

    private val subject : PublishSubject<PlayerEvent> = PublishSubject.create()

    private var player : Player? = null

    override fun watchPlayer(player: Player): Observable<PlayerEvent> {
        this.player = player
        return subject
    }

    override fun getPlayer(): Player? {
        return player
    }

    fun free() {
        subject.onComplete()
    }
}
