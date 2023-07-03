package net.nicksneurons.blastthebox.game

import net.nicksneurons.blastthebox.utils.RouletteWheel

object Game {

    val settings = GameSettings()
    val powerups: RouletteWheel<PowerupType> = settings.createPowerupRoulette()
    val cube_health: RouletteWheel<Int> = settings.createCubeHealthRoulette()
    val indestructible: RouletteWheel<Int> = settings.createIndestructibleRoulette()

    init {
        // todo
    }
}