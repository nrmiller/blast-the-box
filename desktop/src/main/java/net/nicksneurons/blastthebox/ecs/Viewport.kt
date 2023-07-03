package net.nicksneurons.blastthebox.ecs

/**
 * Describes a region of the screen
 * @param x - the x-offset expressed as a percentage of the screen width
 * @param y - the y-offset expressed as a percentage of the screen height
 * @param width - the viewport width expressed as a percentage of the screen width
 * @param height - the viewport height expressed as a percentage of the screen height
 */
class Viewport(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float) {

    companion object {
        @JvmStatic val DEFAULT = Viewport(0.0f, 0.0f, 1.0f, 1.0f)

        @JvmStatic val TOP = Viewport(0.0f, 0.5f, 1.0f, 0.5f)
        @JvmStatic val BOTTOM = Viewport(0.0f, 0.0f, 1.0f, 0.5f)
        @JvmStatic val LEFT = Viewport(0.0f, 0.0f, 0.5f, 1.0f)
        @JvmStatic val RIGHT = Viewport(0.5f, 0.0f, 0.5f, 1.0f)

        @JvmStatic val TOP_LEFT = Viewport(0.0f, 0.5f, 0.5f, 0.5f)
        @JvmStatic val TOP_RIGHT = Viewport(0.5f, 0.5f, 0.5f, 0.5f)
        @JvmStatic val BOTTOM_LEFT = Viewport(0.0f, 0.0f, 0.5f, 0.5f)
        @JvmStatic val BOTTOM_RIGHT = Viewport(0.5f, 0.0f, 0.5f, 0.5f)
    }
}