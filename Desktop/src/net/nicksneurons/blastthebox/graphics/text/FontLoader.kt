package net.nicksneurons.blastthebox.graphics.text

object FontLoader {

    private val fonts = mutableMapOf<Int, Font>()

    private fun generateUniqueID(map: Map<Int, Any>): Int {
        var id = 1
        while (map.containsKey(id)) {
            id++
        }
        return id
    }


    fun loadFont(font: Font): Int {
        val id = generateUniqueID(fonts)
        fonts[id] = font
        return id
    }

    fun getFont(id: Int): Font {
        check(fonts.containsKey(id)) { "Invalid ID, font may already be freed!"}
        return fonts[id]!!
    }

    fun freeFont(font: Font) {
        font.free()
    }

    fun free() {
        val keys = fonts.keys.toList()
        for (index in fonts.size - 1 downTo 0) {
            val key = keys[index]
            if (fonts.containsKey(key)) {
                freeFont(fonts[key]!!)
            }
        }
    }
}