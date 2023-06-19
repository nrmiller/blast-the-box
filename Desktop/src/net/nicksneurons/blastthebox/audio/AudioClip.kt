package net.nicksneurons.blastthebox.audio

import org.lwjgl.openal.AL10.*
import org.lwjgl.stb.STBVorbis
import org.lwjgl.stb.STBVorbisInfo
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.NULL


class AudioClip(
        val resourcePath: String) {

    val bufferId: Int

    init {
        bufferId = alGenBuffers()

        stackPush().use() { stack ->
            STBVorbisInfo.malloc().use() { info ->
                val error = stack.mallocInt(1)

                val data = javaClass.getResourceAsStream(resourcePath).readAllBytes()
                val bb = MemoryUtil.memAlloc(data.size).put(data).flip()

                // Create an OGG Vorbis decoder for the file
                val decoder = STBVorbis.stb_vorbis_open_memory(bb, error, null)

                if (decoder == NULL) {
                    MemoryUtil.memFree(bb)
                    throw RuntimeException("Failed to open Ogg Vorbis file. Error: " + error[0])
                }

                // Get the file info
                STBVorbis.stb_vorbis_get_info(decoder, info)

                val channels = info.channels()
                val sampleRate = info.sample_rate()

                val lengthSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder)
                val pcm = MemoryUtil.memAllocShort(lengthSamples * channels)

                STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm)
                STBVorbis.stb_vorbis_close(decoder)
                MemoryUtil.memFree(bb) // once we are done with the decoder we can release the memory it uses.

                alBufferData(bufferId, if (channels == 1) AL_FORMAT_MONO16 else AL_FORMAT_STEREO16, pcm, sampleRate)
                MemoryUtil.memFree(pcm)
            }
        }
    }

    fun free() {
        alDeleteBuffers(bufferId)
    }
}