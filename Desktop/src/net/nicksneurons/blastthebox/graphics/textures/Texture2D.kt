package net.nicksneurons.blastthebox.graphics.textures

import org.joml.Vector2i
import org.lwjgl.opengl.GL33.*
import org.lwjgl.opengl.GL42.glTexStorage2D
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.NULL

class Texture2D : Texture {

    override val target: Int = GL_TEXTURE_2D

    val width: Int
    val height: Int

    constructor (resourcePath: String) {
        stackPush().use() { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val comp = stack.mallocInt(1)
            glBindTexture(target, id)

            val data = javaClass.getResourceAsStream(resourcePath).readAllBytes()
            val bb = MemoryUtil.memAlloc(data.size).put(data).flip()
            val pixels = stbi_load_from_memory(bb, w, h, comp, 4)

            width = w.get(0)
            height = h.get(0)

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexImage2D(target, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels)

            stbi_image_free(pixels)
            MemoryUtil.memFree(bb)
        }

        // Apply defaults
        updateTextureFilter()
        wrapS = wrapS
        wrapT = wrapT
        wrapR = wrapR
        borderColor = borderColor
    }

    private constructor(size: Vector2i) {
        width = size.x
        height = size.y

        glBindTexture(target, id)

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexStorage2D(target, 1, GL_RGBA8, width, height)

        // Apply defaults
        updateTextureFilter()
        wrapS = wrapS
        wrapT = wrapT
        wrapR = wrapR
        borderColor = borderColor
    }

    companion object {
        @JvmStatic fun createEmpty(size: Vector2i): Texture2D {
            return Texture2D(size)
        }
    }
}