package net.nicksneurons.blastthebox.ecs.components

import org.lwjgl.opengl.GL33.*
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil

class Texture2D(val resourcePath: String) : Texture() {

    override val target: Int = GL_TEXTURE_2D

    init {
        stackPush().use() { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val comp = stack.mallocInt(1)
            glBindTexture(target, id)

            val data = javaClass.getResourceAsStream(resourcePath).readAllBytes()
            val bb = MemoryUtil.memAlloc(data.size).put(data).flip()
            val pixels = stbi_load_from_memory(bb, w, h, comp, 4)

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexImage2D(target, 0, GL_RGBA8, w.get(0), h.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels)

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
}