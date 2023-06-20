package net.nicksneurons.blastthebox.graphics.text

import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.ecs.components.RenderableComponent
import net.nicksneurons.blastthebox.graphics.geometry.Square
import org.joml.Vector2d
import org.joml.Vector3d

class TextSprite(fontId: Int) : RenderableComponent() {

    private val font = FontLoader.getFont(fontId)

    var text: String = ""
        set(value) {
            updatePrimitives(field, value)
            field = value
        }

    // todo how do we get dip with OpenGL?
    var sizePx: Int = 12
        set(value) {
            field = value
            updatePrimitives(text, text)
        }

    var columns: Int = Int.MAX_VALUE
        set(value) {
            field = value
            updatePrimitives(text, text)
        }

    private val meshes = mutableListOf<Mesh>()

    private fun updatePrimitives(oldText: String, newText: String) {
        for (mesh in meshes) {
            // do not free the texture as we do not own it
            mesh.primitive.free()
        }
        meshes.clear()

        var column = 0
        var xOffset = 0
        var yOffset = 0
        for (char in newText) {
            if (char == '\n' || column == columns) {
                yOffset -= sizePx
                xOffset = 0
                column = 0
            }

            val glyph = font.glyphs[char]
            if (glyph != null) {

                val scale = sizePx / glyph.texture.height.toDouble()

                meshes.add(Mesh(Square(
                        Vector3d(xOffset.toDouble(), yOffset.toDouble(), 0.0),
                        Vector2d(glyph.texture.width.toDouble() * scale, sizePx.toDouble())),
                        glyph.texture))

                // advance by the width of the texture
                xOffset += (glyph.texture.width * scale).toInt()
                column++
            }
        }
    }

    override fun draw() {
        meshes.forEach {
            it.draw()
        }

    }

    override fun free() {
        for (mesh in meshes) {
            // do not free the texture as we do not own it
            mesh.primitive.free()
        }
        meshes.clear()
    }
}
