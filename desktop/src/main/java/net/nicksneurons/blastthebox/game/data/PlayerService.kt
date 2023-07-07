package net.nicksneurons.blastthebox.game.data

import net.nicksneurons.blastthebox.game.entities.Player


interface PlayerService {
    fun setPlayer(player: Player)
    fun getPlayer(): Player?
}


