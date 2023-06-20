package net.nicksneurons.blastthebox.graphics.textures

import org.joml.Vector4f
import org.lwjgl.opengl.GL33.*
import org.lwjgl.opengl.GL44.GL_MIRROR_CLAMP_TO_EDGE
import org.lwjgl.system.MemoryStack.stackPush

abstract class Texture() {

    val id: Int = glGenTextures()

    abstract val target: Int

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
            glBindTexture(target, id)
            glGenerateMipmap(target)
            updateTextureFilter()
        }

    var mipmapFilter: TextureFilter = TextureFilter.LINEAR
        set(value) {
            field = value
            updateTextureFilter()
        }

    var wrapS: TextureWrap = TextureWrap.REPEAT
        set(value) {
            field = value
            glBindTexture(target, id)
            glTexParameteri(target, GL_TEXTURE_WRAP_S, wrapS.value)
        }

    var wrapT: TextureWrap = TextureWrap.REPEAT
        set(value) {
            field = value
            glBindTexture(target, id)
            glTexParameteri(target, GL_TEXTURE_WRAP_T, wrapT.value)
        }

    var wrapR: TextureWrap = TextureWrap.REPEAT
        set(value) {
            field = value
            glBindTexture(target, id)
            glTexParameteri(target, GL_TEXTURE_WRAP_R, wrapR.value)
        }

    var borderColor: Vector4f = Vector4f()
        set(value) {
            field = value
            stackPush().use() {
                glBindTexture(target, id)

                stackPush().use() {
                    val buffer = it.mallocFloat(4)
                    glTexParameterfv(target, GL_TEXTURE_BORDER_COLOR, value.get(buffer))
                }
            }
        }

    protected fun updateTextureFilter() {
        glBindTexture(target, id)


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

        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilterMode)

        val magFilterMode: Int = if (magFilter == TextureFilter.NEAREST) GL_NEAREST else GL_LINEAR
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilterMode)
    }


    fun free() {
        glDeleteTextures(id)
    }

    open fun bind() {
        glBindTexture(target, id)
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