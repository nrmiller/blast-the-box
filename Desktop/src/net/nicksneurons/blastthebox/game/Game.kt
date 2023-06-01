package net.nicksneurons.blastthebox.game

import net.nicksneurons.blastthebox.utils.RouletteWheel

object Game {

    val settings = GameSettings()
    val powerups: RouletteWheel = settings.createPowerupRoulette()
    val cube_health: RouletteWheel = settings.createCubeHealthRoulette()
    val indestructible: RouletteWheel = settings.createIndestructibleRoulette()

    init {
        // todo
    }
}