package net.nicksneurons.blastthebox.game

import net.nicksneurons.blastthebox.utils.RouletteWheel

enum class Powerup {
    HEART,
    STOPWATCH,
    NUKE,
    SHIELD,
    TRIPLE_FIRE,
    AMMO,
    STRENGTH_ONE,
    STRENGTH_TWO,
    PIERCE;

    var textureId: Int = 0
}