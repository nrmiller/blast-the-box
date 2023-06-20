package net.nicksneurons.blastthebox.game

import net.nicksneurons.blastthebox.graphics.text.BitmapFont
import net.nicksneurons.blastthebox.graphics.text.FontLoader
import net.nicksneurons.blastthebox.graphics.textures.TextureAtlas
import org.joml.Vector2i

object Fonts {

    @JvmStatic var alphanumeric: Int = 0
        private set

    @JvmStatic var yellowNumbers: Int = 0
        private set

    @JvmStatic fun loadFonts() {

        alphanumeric = FontLoader.loadFont(BitmapFont(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ! ".toList(),
                TextureAtlas("/textures/alphabet.png", Vector2i(64, 64))).mergeWith(
                "abcdefghijklmnopqrstuvwxyz! ".toList(),
                TextureAtlas("/textures/alphabet.png", Vector2i(64, 64))).mergeWith(
                "01234+56789%".toList(),
                TextureAtlas("/textures/numbers.png", Vector2i(64, 64))))

        yellowNumbers = FontLoader.loadFont(BitmapFont(
                "01234+56789%".toList(),
                TextureAtlas("/textures/numbers.png", Vector2i(64, 64))))
    }

    @JvmStatic fun freeFonts() {
        FontLoader.free()
    }
}