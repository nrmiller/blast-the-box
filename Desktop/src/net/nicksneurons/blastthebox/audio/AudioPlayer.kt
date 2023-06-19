package net.nicksneurons.blastthebox.audio

import org.lwjgl.openal.AL10.*

object AudioPlayer {

    private val sources = mutableListOf<AudioSource>()

    fun playSound(source: AudioSource) {
        sources.add(source)
        source.play()
    }

    fun loopSound(source: AudioSource) {
        sources.add(source)
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
        sources.remove(source)
    }

    /**
     * Stops all non-looping audio sources.
     * @param shouldStopLooping - set to true to also stop looping sources
     */
    fun stopAllSounds(shouldStopLooping: Boolean = false) {
        for (index in sources.size - 1 downTo 0) {
            if (shouldStopLooping || alGetSourcei(sources[index].sourceId, AL_LOOPING) == AL_FALSE) {
                stopSound(sources[index])
            }
        }
    }

    /**
     * Checks each non-looping audio source to see if it is completed
     */
    @Deprecated("Shouldn't free stopped sounds as this destroys the audio source")
    fun pollForCompletedSounds() {
        for (index in sources.size - 1 downTo 0) {
            val state = alGetSourcei(sources[index].sourceId, AL_SOURCE_STATE)
            if (state == AL_INITIAL || state == AL_STOPPED) {
                stopSound(sources[index])
            }
        }
    }
}