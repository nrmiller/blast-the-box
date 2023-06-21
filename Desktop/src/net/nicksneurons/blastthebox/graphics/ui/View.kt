package net.nicksneurons.blastthebox.graphics.ui

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import net.nicksneurons.blastthebox.ecs.Entity

open class View(name: String? = null) : Entity(name), Focusable {

    private var subject: Subject<FocusEvent> = PublishSubject.create()

    fun focusEvents(): Observable<FocusEvent> {
        return subject
    }

    final override var hasFocus: Boolean = false
        private set(value) {
            if (field != value) {
                field = value

                if (value) {
                    subject.onNext(Focused())
                } else {
                    subject.onNext(Unfocused())
                }
            }
        }

    override fun focus(child: Focusable?) {
        hasFocus = true
    }

    override fun clearFocus() {
        hasFocus = false
    }
}

interface FocusEvent
class Focused(): FocusEvent
class Unfocused(): FocusEvent
