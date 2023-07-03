package net.nicksneurons.blastthebox.graphics.textures

import net.nicksneurons.blastthebox.utils.ImageTools
import org.joml.Vector2i
import org.lwjgl.opengl.GL42.*
import org.lwjgl.opengl.GL43.glCopyImageSubData
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil

class TextureAtlas(
        val resourcePath: String,
        val subImageSizePx: Vector2i) : Texture() {

    val width: Int
    val height: Int
    val columns: Int
    val rows: Int
    val count: Int
        get() = columns * rows

    override val target = GL_TEXTURE_2D_ARRAY

    init {
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
            rows = (height / subImageSizePx.x)
            columns = (width / subImageSizePx.y)

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

            glTexStorage3D(target, 1,  GL_RGBA8, subImageSizePx.x, subImageSizePx.y, rows * columns)

            for (row in 0 until rows) {
                for (column in 0 until columns) {

                    val xOffset = column * subImageSizePx.x
                    val yOffset = row * subImageSizePx.y
                    val index = row * columns + column

                    val cropped = ImageTools.crop(xOffset, yOffset, subImageSizePx.x, subImageSizePx.y, width, height, 4, pixels!!)

                    glTexSubImage3D(target,
                            0,
                            0, 0, index,
                            subImageSizePx.x, subImageSizePx.y, 1,
                            GL_RGBA,
                            GL_UNSIGNED_BYTE,
                            cropped)

                    MemoryUtil.memFree(cropped)
                }
            }

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

    fun copyTextureAt(index: Int) : Texture2D {
        require(index in 0 until count) { "Index out of range!" }

        val texture = Texture2D.createEmpty(subImageSizePx)

        glCopyImageSubData(
                id, target, 0,
                0, 0, index,
                texture.id, texture.target, 0,
                0, 0, 0,
                subImageSizePx.x, subImageSizePx.y, 1)

        return texture
    }

    var index: Int = 0
        set(value) {
            require(index in 0 until count) { "Index out of range!" }
            field = value
        }

    override fun bind() {
        super.bind()

        val program = glGetInteger(GL_CURRENT_PROGRAM)
        glUniform1i(glGetUniformLocation(program, "frame_index"), index)
        glUniform1i(glGetUniformLocation(program, "frame_width"), subImageSizePx.x)
        glUniform1i(glGetUniformLocation(program, "frame_height"), subImageSizePx.y)
    }
}