package net.nicksneurons.blastthebox.graphics.ui

import org.joml.Vector2f
import net.nicksneurons.blastthebox.ecs.components.*
import net.nicksneurons.blastthebox.graphics.geometry.Square
import net.nicksneurons.blastthebox.graphics.textures.Texture
import net.nicksneurons.blastthebox.graphics.textures.Texture2D
import net.nicksneurons.blastthebox.physics.shapes.RectangleCollider

class Button(name: String? = null) : View(name) {

    private val mesh = Mesh(Square())

    private var defaultTexture: Texture? = null
    var defaultTexturePath: String? = null
        set(value) {
            if (field != value) {
                field = value
                defaultTexture?.free()
                if (value != null) {
                    defaultTexture = Texture2D(value)
                    updateTexture()
                }
            }
        }

    private var hoverTexture: Texture? = null
    var hoverTexturePath: String? = null
        set(value) {
            if (field != value) {
                field = value
                hoverTexture?.free()
                if (value != null) {
                    hoverTexture = Texture2D(value)
                    updateTexture()
                }
            }
        }

    var size: Vector2f = Vector2f(32.0f)
        set(value) {
            (getComponent<ClickableBody2D>()!!.shape as RectangleCollider).apply {
                size = value
            }
            field = value
        }

    init {
        addComponent(mesh)
        addComponent(ClickableBody2D(RectangleCollider(size)).apply {
            mouseEvents().doOnNext {
                when (it) {
                    is MouseUp -> onUp(it)
                    is MouseDown -> onDown(it)
                    is MouseEnter -> {
                        updateTexture()
                        onEnter(it)
                    }
                    is MouseExit -> {
                        updateTexture()
                        onExit(it)
                    }
                }
            }.subscribe()
        })

        focusEvents().subscribe {
            when (it) {
                is Focused -> { updateTexture() }
                is Unfocused -> { updateTexture() }
            }
        }
    }

    var onDown: (MouseDown) -> Unit = {}
    var onUp: (e: MouseUp) -> Unit = {}
    var onEnter: (e: MouseEnter) -> Unit = {}
    var onExit: (e: MouseExit) -> Unit = {}

    private fun updateTexture() {
        mesh.texture = if (getComponent<ClickableBody2D>()!!.isHovered || hasFocus) hoverTexture else defaultTexture
    }

    override fun free() {
        super.free()

        defaultTexture?.free()
        hoverTexture?.free()
    }
}