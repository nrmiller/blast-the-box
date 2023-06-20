package net.nicksneurons.blastthebox.graphics.text

import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.graphics.geometry.Square
import org.joml.Vector2d
import org.joml.Vector3d
import kotlin.math.max


class FontMetrics(val text: String, val sizePx: Int, val fontId: Int, val columns: Int = Int.MAX_VALUE) {

    private val font = FontLoader.getFont(fontId)

    val width: Int
    val height: Int

    init {
        var maxWidth = 0
        var maxHeight = if (text.isBlank()) 0 else sizePx

        var column = 0
        var xOffset = 0
        var yOffset = 0
        for (char in text) {
            if (char == '\n' || column == columns) {
                yOffset -= sizePx
                maxHeight += sizePx
                xOffset = 0
                column = 0
            }

            val glyph = font.glyphs[char]
            if (glyph != null) {

                val scale = sizePx / glyph.texture.height.toDouble()

                // advance by the width of the texture
                xOffset += (glyph.texture.width * scale).toInt()
                maxWidth = max(maxWidth, xOffset)
                column++
            }
        }

        width = maxWidth
        height = maxHeight
    }
}