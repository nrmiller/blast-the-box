package net.nicksneurons.blastthebox.utils

interface SoundPlayer : AutoCloseable {
    fun playSound(id: Int, speed: Float)
    fun loopSound(id: Int, speed: Float)
    fun stopSound(id: Int)
    fun stopAllSounds()
}