package net.nicksneurons.blastthebox.graphics.ui

interface Focusable {
    fun focus(child: Focusable? = null)
    fun clearFocus()

    val hasFocus: Boolean
}

class FocusScope(vararg val elements: Focusable): Focusable {

    private var currentNode: FocusNode? = null

    init {
        var workingNode: FocusNode? = null

        for (element in elements) {
            val node = FocusNode(element)

            if (workingNode != null) {
                workingNode.next = node
                node.previous = workingNode
            }

            workingNode = node

            // set the first node
            if (currentNode == null ) {
                currentNode = workingNode
            }

            workingNode.next = currentNode
            currentNode?.previous = workingNode
        }
    }

    fun next() {
        currentNode?.focusable?.clearFocus()
        currentNode = currentNode?.next
        currentNode?.focusable?.focus()
    }

    fun previous() {
        currentNode?.focusable?.clearFocus()
        currentNode = currentNode?.previous
        currentNode?.focusable?.focus()
    }

    fun get(): Focusable? = currentNode?.focusable

    override var hasFocus: Boolean = false
        private set

    override fun focus(child: Focusable?) {
        if (child == null) {
            currentNode?.focusable?.focus()
        }

        // Clear the scope's focus
        // then focus the child if it belongs to the scope
        val curr = currentNode
        var target: FocusNode? = null
        var scopeOwnsChild = false
        do {
            if (currentNode?.focusable == child) {
                target = currentNode
                scopeOwnsChild = true
            }
            currentNode?.focusable?.clearFocus()
            currentNode = currentNode?.next
        } while (currentNode != curr)

        if (scopeOwnsChild) {
            currentNode = target
            child?.focus()

        }

        hasFocus = true
    }

    override fun clearFocus() {
        // Clear the scope's focus
        val curr = currentNode
        do {
            currentNode?.focusable?.clearFocus()
            currentNode = currentNode?.next
        } while (currentNode != curr)

        hasFocus = false
    }

    class FocusNode(val focusable: Focusable) {

        var previous: FocusNode? = null
        var next: FocusNode? = null

        fun add(focuseable: Focusable) {
            next = FocusNode(focuseable)
            next?.previous = this
        }

        fun remove() {
            previous?.next = next
            next?.previous = previous

            next = null
            previous = null
        }
    }
}