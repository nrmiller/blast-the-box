package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.ecs.Component

open class RenderableComponent: Component(), Renderable {

    var renderLayer: Int = 0
    var material: Material = Material.DEFAULT

    override fun draw() {

    }
}
