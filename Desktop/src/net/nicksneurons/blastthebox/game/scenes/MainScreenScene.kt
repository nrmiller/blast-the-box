package net.nicksneurons.blastthebox.game.scenes

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.*
import net.nicksneurons.blastthebox.geometry.Cube
import net.nicksneurons.blastthebox.geometry.Square
import org.joml.Vector2i
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*

class MainScreenScene: Scene() {

    lateinit var alphabet: Entity
    lateinit var texture: TextureAtlas

    override fun onSceneBegin() {

        texture = TextureAtlas("/textures/alphabet.png", Vector2i(64, 64)).apply {
            index = 7
            minFilter = TextureFilter.NEAREST
            isMipmap = false
        }

//        texture = TextureAtlas("/textures/uv_checker.png", Vector2i(128, 128)).apply {
//            index = 37
//            minFilter = TextureFilter.LINEAR
//        }

        alphabet = Entity().apply {
            addComponent(texture)
            addComponent(Transform(position = Vector3f(0.0f, 0.0f, -1.0f)))
            addComponent(Mesh(Square()))
            addComponent(Material("/shaders/atlas.vert","/shaders/atlas.frag"))
//            addComponent(Mesh(Cube()))
        }

        entities.add(alphabet)
    }

    var index = 0

    override fun onKeyDown(key: Int, scancode: Int, modifiers: Int) {
        if (key == GLFW_KEY_RIGHT) {
            texture.index = ((texture.index + 1).mod(27))
        }
        if (key == GLFW_KEY_LEFT) {
            texture.index = ((texture.index - 1).mod(27))
        }
    }

    override fun onKeyRepeat(key: Int, scancode: Int, modifiers: Int) {
        if (key == GLFW_KEY_RIGHT) {
            texture.index = ((texture.index + 1).mod(27))
        }
        if (key == GLFW_KEY_LEFT) {
            texture.index = ((texture.index - 1).mod(27))
        }
    }

    override fun onSceneEnd() {

    }
}