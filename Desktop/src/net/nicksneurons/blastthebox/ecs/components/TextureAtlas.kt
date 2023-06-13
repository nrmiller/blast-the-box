package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.ecs.Component
import net.nicksneurons.blastthebox.utils.ImageTools
import org.joml.Vector2i
import org.joml.Vector4f
import org.lwjgl.opengl.GL42.*
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil

class TextureAtlas(
        val resourcePath: String,
        val subImageSizePx: Vector2i) : Component() {

    val id: Int
    val width: Int
    val height: Int
    val columns: Int
    val rows: Int

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
            glBindTexture(GL_TEXTURE_2D_ARRAY, id)
            glGenerateMipmap(GL_TEXTURE_2D_ARRAY)
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
            glBindTexture(GL_TEXTURE_2D_ARRAY, id)
            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, wrapS.value)
        }

    var wrapT: TextureWrap = TextureWrap.REPEAT
        set(value) {
            field = value
            glBindTexture(GL_TEXTURE_2D_ARRAY, id)
            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, wrapT.value)
        }

    var wrapR: TextureWrap = TextureWrap.REPEAT
        set(value) {
            field = value
            glBindTexture(GL_TEXTURE_2D_ARRAY, id)
            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_R, wrapR.value)
        }

    var borderColor: Vector4f = Vector4f()
        set(value) {
            field = value
            stackPush().use() {
                glBindTexture(GL_TEXTURE_2D_ARRAY, id)

                stackPush().use() {
                    val buffer = it.mallocFloat(4)
                    glTexParameterfv(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_BORDER_COLOR, value.get(buffer))
                }
            }
        }

    init {
        stackPush().use() { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val comp = stack.mallocInt(1)
            id = glGenTextures()
            glBindTexture(GL_TEXTURE_2D_ARRAY, id)

            val data = javaClass.getResourceAsStream(resourcePath).readAllBytes()
            val bb = MemoryUtil.memAlloc(data.size).put(data).flip()

            val pixels = stbi_load_from_memory(bb, w, h, comp, 4)

            width = w.get(0)
            height = h.get(0)
            rows = (height / subImageSizePx.x)
            columns = (width / subImageSizePx.y)

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

            glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1,  GL_RGBA8, subImageSizePx.x, subImageSizePx.y, rows * columns)

            for (row in 0 until rows) {
                for (column in 0 until columns) {

                    val xOffset = column * subImageSizePx.x
                    val yOffset = row * subImageSizePx.y
                    val index = row * columns + column

                    val cropped = ImageTools.crop(xOffset, yOffset, subImageSizePx.x, subImageSizePx.y, width, height, 4, pixels!!)

                    glTexSubImage3D(GL_TEXTURE_2D_ARRAY,
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

    private  fun updateTextureFilter() {
        glBindTexture(GL_TEXTURE_2D_ARRAY, id)


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

        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, minFilterMode)

        val magFilterMode: Int = if (magFilter == TextureFilter.NEAREST) GL_NEAREST else GL_LINEAR
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, magFilterMode)
    }


    override fun free() {
        glDeleteTextures(id)
    }

    var index: Int = 0

    fun bind() {
        glBindTexture(GL_TEXTURE_2D_ARRAY, id)

        val program = glGetInteger(GL_CURRENT_PROGRAM)
        glUniform1i(glGetUniformLocation(program, "frame_index"), index)
        glUniform1i(glGetUniformLocation(program, "frame_width"), subImageSizePx.x)
        glUniform1i(glGetUniformLocation(program, "frame_height"), subImageSizePx.y)
    }
}