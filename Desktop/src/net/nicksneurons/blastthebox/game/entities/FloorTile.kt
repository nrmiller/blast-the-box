package net.nicksneurons.blastthebox.game.entities

import net.nicksneurons.blastthebox.ecs.Entity
import net.nicksneurons.blastthebox.ecs.Scene
import net.nicksneurons.blastthebox.ecs.components.Mesh
import net.nicksneurons.blastthebox.graphics.geometry.Square
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import org.joml.Vector2d
import org.joml.Vector3d
import org.joml.Vector3f

class FloorTile: Entity() {

    override fun onAddedToScene(scene: Scene) {
        super.onAddedToScene(scene)

        addComponent(Mesh(Square(Vector3d(0.0, 0.0, 0.0), Vector2d(20.0, 20.0), 20), Texture2D("/textures/floor.png")))
        transform.rotation = Vector3f(- Math.PI.toFloat() / 2.0f, 0.0f, 0.0f)

        scene.addEntity(Entity().also {
            it.addComponent(Mesh(Square(Vector3d(0.0, 0.0, 0.0), Vector2d(1.0, 20.0), 1, 20), Texture2D("/textures/floor_transr.png")))
            it.transform.position = Vector3f(-1.0f, 0.0f, 0.0f)
            it.transform.parent = this.transform
        })

        scene.addEntity(Entity().also {
            it.addComponent(Mesh(Square(Vector3d(0.0, 0.0, 0.0), Vector2d(1.0, 20.0), 1, 20), Texture2D("/textures/floor_transl.png")))
            it.transform.position = Vector3f(20.0f, 0.0f, 0.0f)
            it.transform.parent = this.transform
        })
    }
}