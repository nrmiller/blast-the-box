package net.nicksneurons.blastthebox.audio

import org.lwjgl.openal.AL10.*

object AudioPlayer {

    private val sources = mutableMapOf<AudioSource, Boolean>()

    fun playSound(source: AudioSource, freeOnStop: Boolean = false) {
        sources[source] = freeOnStop
        source.play()
    }

    fun loopSound(source: AudioSource) {
        sources[source] = false
        alSourcei(source.sourceId, AL_LOOPING, AL_TRUE)
        source.play()
    }

    fun pauseSound(source: AudioSource) {
        source.pause()
    }

    /**
     * Stops the sound and frees any associated memory.
     */
    fun stopSound(source: AudioSource) {
        source.stop()
        val shouldFree = sources.remove(source) ?: false
        if (shouldFree) {
            source.free()
        }
    }

    /**
     * Stops all non-looping audio sources.
     * @param shouldStopLooping - set to true to also stop looping sources
     */
    fun stopAllSounds(shouldStopLooping: Boolean = false) {
        for (source in sources.keys.reversed()) {
            if (shouldStopLooping || alGetSourcei(source.sourceId, AL_LOOPING) == AL_FALSE) {
                stopSound(source)
            }
        }
    }

    /**
     * Checks each non-looping audio source to see if it is completed
     */
    fun pollForCompletedSounds() {
        for (source in sources.keys.reversed()) {
            val state = alGetSourcei(source.sourceId, AL_SOURCE_STATE)
            if (state == AL_INITIAL || state == AL_STOPPED) {
                stopSound(source)
            }
        }
    }
}