package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.ecs.Component

interface Renderable {
    fun draw()
}

open class RenderableComponent: Component(), Renderable {

    var material: Material = Material.DEFAULT

    override fun draw() {

    }
}