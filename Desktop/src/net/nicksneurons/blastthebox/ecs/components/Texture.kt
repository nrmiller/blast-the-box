package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.ecs.Component
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import org.lwjgl.opengl.GL44.GL_MIRROR_CLAMP_TO_EDGE
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil

class Texture(val resourcePath: String) : Component() {

    val id: Int

    var wrapS: TextureWrap = TextureWrap.REPEAT
        set(value) {
            field = value
            glBindTexture(GL_TEXTURE_2D, id)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS.value)
        }

    var wrapT: TextureWrap = TextureWrap.REPEAT
        set(value) {
            field = value
            glBindTexture(GL_TEXTURE_2D, id)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT.value)
        }

    var wrapR: TextureWrap = TextureWrap.REPEAT
        set(value) {
            field = value
            glBindTexture(GL_TEXTURE_2D, id)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, wrapR.value)
        }

    var borderColor: Vector4f = Vector4f()
        set(value) {
            field = value
            stackPush().use() {
                glBindTexture(GL_TEXTURE_2D, id)
                val buffer = BufferUtils.createFloatBuffer(4)
                glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, value.get(buffer))
            }
        }

    init {
        stackPush().use() {
            val w = MemoryUtil.memAllocInt(1)
            val h = MemoryUtil.memAllocInt(1)
            val comp = MemoryUtil.memAllocInt(1)
            id = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, id)

            val data = javaClass.getResourceAsStream(resourcePath).readAllBytes()
            val bb = BufferUtils.createByteBuffer(data.size).put(data).flip()
            val pixels = stbi_load_from_memory(bb, w, h, comp, 4)

            stbi_load_from_memory(bb, w, h, comp, 4)

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(0), h.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels)

            stbi_image_free(pixels)
        }

        // Apply defaults
        updateTextureFilter()
        wrapS = wrapS
        wrapT = wrapT
        wrapR = wrapR
        borderColor = borderColor
    }

    var minFilter: TextureFilter = TextureFilter.NEAREST
        set(value) {
            field = value
            updateTextureFilter()
        }

    var magFilter: TextureFilter = TextureFilter.NEAREST
        set(value) {
            field = value
            updateTextureFilter()
        }

    var isMipmap: Boolean = false
        set(value) {
            field = value
            glBindTexture(GL_TEXTURE_2D, id)
            glGenerateMipmap(GL_TEXTURE_2D)
            updateTextureFilter()
        }

    var mipmapFilter: TextureFilter = TextureFilter.LINEAR
        set(value) {
            field = value
            updateTextureFilter()
        }

    private  fun updateTextureFilter() {
        glBindTexture(GL_TEXTURE_2D, id)


        val minFilterMode = if (isMipmap) {
            when {
                // run minification filter on closest mipmap
                (minFilter == TextureFilter.NEAREST && mipmapFilter == TextureFilter.NEAREST) -> GL_NEAREST_MIPMAP_NEAREST
                (minFilter == TextureFilter.LINEAR && mipmapFilter == TextureFilter.NEAREST) -> GL_LINEAR_MIPMAP_NEAREST
                // run minification filter on two closest mipmaps, then average
                (minFilter == TextureFilter.NEAREST && mipmapFilter == TextureFilter.LINEAR) -> GL_NEAREST_MIPMAP_LINEAR
                (minFilter == TextureFilter.LINEAR && mipmapFilter == TextureFilter.LINEAR) -> GL_LINEAR_MIPMAP_LINEAR
                else -> throw Exception("Unknown filter mode.")
            }
        } else {
            if (minFilter == TextureFilter.NEAREST) GL_NEAREST else GL_LINEAR
        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilterMode)

        val magFilterMode: Int = if (magFilter == TextureFilter.NEAREST) GL_NEAREST else GL_LINEAR
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilterMode)
    }


    override fun free() {
        glDeleteTextures(id)
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }
}

enum class TextureFilter {
    NEAREST,
    LINEAR,
}

enum class TextureWrap(val value: Int) {
    CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
    MIRRORED_REPEAT(GL_MIRRORED_REPEAT),
    REPEAT(GL_REPEAT),
    MIRROR_CLAMP_TO_EDGE(GL_MIRROR_CLAMP_TO_EDGE)
}