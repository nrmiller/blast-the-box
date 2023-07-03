package net.nicksneurons.blastthebox.game.data

import io.reactivex.rxjava3.core.Observable
import net.nicksneurons.blastthebox.game.entities.Player


interface PlayerService {

    fun watchPlayer(player: Player) : Observable<PlayerEvent>

    fun getPlayer(): Player?
}

open class PlayerEvent(val player: Player) { }
class HealthUpdatedEvent(player : Player) : PlayerEvent(player) { }
class ShieldUpdatedEvent(player : Player) : PlayerEvent(player) { }
