package net.nicksneurons.blastthebox.ecs.components

import net.nicksneurons.blastthebox.client.ShaderProgram
import net.nicksneurons.blastthebox.ecs.Component

data class RenderComponent(val program: ShaderProgram) : Component()
