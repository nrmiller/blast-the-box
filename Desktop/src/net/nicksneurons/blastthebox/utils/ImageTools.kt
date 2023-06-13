package net.nicksneurons.blastthebox.utils

import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

class ImageTools {

    companion object {
        @JvmStatic fun crop(x: Int, y: Int, cropWidth: Int, cropHeight: Int, imageWidth: Int, imageHeight: Int, pixelSize: Int, pixels: ByteBuffer): ByteBuffer {
            if (x >= imageWidth) throw Exception("Invalid x coordinate")
            if (y >= imageWidth) throw Exception("Invalid y coordinate")

            // coerce width & height
            val subimageWidth: Int = if (x + cropWidth >= imageWidth) imageWidth - x else cropWidth
            val subimageHeight: Int = if (y + cropHeight >= imageHeight) imageHeight - y else cropHeight

            val result = MemoryUtil.memAlloc(subimageWidth * subimageHeight * pixelSize)

            for (run in 0 until subimageHeight) {
                val xOffset = x * pixelSize
                val runOffset = (y + run) * imageWidth * pixelSize + xOffset
                val runLength = subimageWidth * pixelSize

                result.put(pixels.slice(runOffset, runLength))
            }

            return result.flip()
        }
    }
}