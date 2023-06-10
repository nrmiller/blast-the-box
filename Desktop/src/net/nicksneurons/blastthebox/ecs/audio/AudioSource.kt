package net.nicksneurons.blastthebox.ecs.audio

import org.joml.Vector3f
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.SOFTSourceSpatialize.AL_AUTO_SOFT
import org.lwjgl.openal.SOFTSourceSpatialize.AL_SOURCE_SPATIALIZE_SOFT

class AudioSource(
        val clip: AudioClip) {

    var relative: Boolean = true
        set(value) {
            field = value
            updateParameters()
        }

    var gain: Float = DEFAULT_GAIN
        set(value) {
            field = value
            updateParameters()
        }

    var pitch: Float = DEFAULT_PITCH
        set(value) {
            field = value
            updateParameters()
        }

    var position = Vector3f()
        set(value) {
            field = value
            updateParameters()
        }

    var velocity = Vector3f()
        set(value) {
            field = value
            updateParameters()
        }

    var isDirectional: Boolean = false
        set(value) {
            field = value
            updateParameters()
        }

    var direction = Vector3f()
        set(value) {
            field = value.normalize()
            updateParameters()
        }

    var maxDistance = Vector3f()
        set(value) {
            field = value
            updateParameters()
        }

    var spatialization: Spatialization = Spatialization.Enabled
        set(value) {
            field = value
            updateParameters()
        }

    val sourceId: Int

    init {
        sourceId = alGenSources()

        updateParameters()

        alSourcei(sourceId, AL_BUFFER, clip.bufferId)
        val error = alGetError()
    }

    private fun updateParameters() {
        alSourcei(sourceId, AL_SOURCE_RELATIVE, if (relative) AL_TRUE else AL_FALSE)
        alSourcef(sourceId, AL_PITCH, pitch)
        alSourcef(sourceId, AL_GAIN, gain)
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z)
        alSource3f(sourceId, AL_VELOCITY, velocity.x, velocity.y, velocity.z)

        // See attenuation curves here: https://indiegamedev.net/2020/04/12/the-complete-guide-to-openal-with-c-part-3-positioning-sounds/#:~:text=How%20to%20Position%20Audio%20in,sounds%20are%20in%20your%20world.
//        alSourcef(sourceId, AL_MAX_DISTANCE, 10.0f)
//        alSourcef(sourceId, AL_REFERENCE_DISTANCE, 3.33333f)
//        alSourcef(sourceId, AL_ROLLOFF_FACTOR, 1.0f)

        // This is needed for spatializing stereo tracks, by default it is set to AL_AUTO_SOFT
        // which spatializes only mono tracks
        alSourcei(sourceId, AL_SOURCE_SPATIALIZE_SOFT, spatialization.value)

        // Directional sound attributes:
        // See https://playcontrol.net/iphonegamebook/book-assets/openal-directional-cones.html
//        alSourcei(sourceId, AL_CONE_INNER_ANGLE, 90)
//        alSourcei(sourceId, AL_CONE_OUTER_ANGLE, 180)
//        alSourcef(sourceId, AL_CONE_OUTER_GAIN, 0.5f)

        if (isDirectional) {
            alSource3f(sourceId, AL_DIRECTION, direction.x, direction.y, direction.z)
        } else {
            alSource3f(sourceId, AL_DIRECTION, 0.0f, 0.0f, 0.0f)
        }
    }

    fun play() {
        alSourcePlay(sourceId)
    }

    fun pause() {
        alSourcePause(sourceId)
    }

    fun stop() {
        alSourceStop(sourceId)
    }

    fun free() {
        stop()
        alDeleteSources(sourceId)
        clip.free()
    }

    companion object {
        const val DEFAULT_GAIN = 1.0f
        const val DEFAULT_PITCH = 1.0f
    }
}

enum class Spatialization(val value: Int) {
    Enabled(AL_TRUE),
    Disabled(AL_FALSE),
    Automatic(AL_AUTO_SOFT)
}