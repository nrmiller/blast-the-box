package net.nicksneurons.blastthebox.graphics.text

import kotlin.math.max


class FontMetrics(text: String, sizePx: Int, fontId: Int, columns: Int = Int.MAX_VALUE) {

    private val font = FontLoader.getFont(fontId)

    val width: Int
    val height: Int

    init {
        var maxWidth = 0
        var maxHeight = if (text.isBlank()) 0 else sizePx

        var column = 0
        var xOffset = 0
        for (char in text) {
            if (char == '\n' || column == columns) {
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