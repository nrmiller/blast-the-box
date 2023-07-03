package net.nicksneurons.blastthebox.graphics.text

import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import net.nicksneurons.blastthebox.graphics.textures.TextureAtlas
import java.util.*

abstract class Font {
    abstract val glyphs: Map<Char, Glyph>

    /**
     * Frees all the glyphs and any texture resources they are using.
     */
    fun free() {
        for (glyph in glyphs.values) {
            glyph.free()
        }
    }
}

class BitmapFont(
        characters: Iterable<Char>,
        source: TextureAtlas) : Font() {

    private val mutableGlyphs = characters.withIndex().associateBy({ it.value }, { Glyph(it.index, source.copyTextureAt(it.index)) }).toMutableMap()
    override val glyphs: MutableMap<Char, Glyph> = Collections.unmodifiableMap(mutableGlyphs)

    /**
     * Merges the font with another to include additional glyphs
     */
    fun mergeWith(glyphs: Iterable<Char>,
                  source: TextureAtlas): BitmapFont {
        glyphs.withIndex().forEach {
            if (!mutableGlyphs.containsKey(it.value)) {
                mutableGlyphs[it.value] = Glyph(it.index, source.copyTextureAt(it.index))
            }
        }

        return this
    }
}

data class Glyph(val index: Int,
                 val texture: Texture2D) {

    fun free() {
        texture.free()
    }
}