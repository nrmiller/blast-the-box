package net.nicksneurons.blastthebox.ecs

open class Component {
    var name: String?  = null

    var isMarkedForDeletion: Boolean = false
    fun queueFree() {
        isMarkedForDeletion = true
    }

    /**
     * Should be overridden to free any resources associated with the component
     */
    open fun free() {

    }

    var entity: Entity? = null

    open fun onAttached(entity: Entity) {
        this.entity = entity
    }

    open fun onDetached() {
        queueFree()
    }
}