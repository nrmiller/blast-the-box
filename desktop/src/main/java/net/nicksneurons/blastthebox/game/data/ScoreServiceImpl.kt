package net.nicksneurons.blastthebox.game.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class ScoreServiceImpl : ScoreService {

    private var subject: Subject<ScoreEvent> = PublishSubject.create()

    private var score: Int = 0

    override fun scoreUpdates(): Observable<ScoreEvent> {
        return subject
    }


    override fun addScore(amount: Int) {
        score += amount
        subject.onNext(ScoreUpdatedEvent(amount))
    }

    override fun addBonusScore(amount: Int) {
        score += amount
        subject.onNext(BonusScoreEvent(amount))
    }

    override fun getCurrentScore(): Int {
        return score
    }

    override fun resetScore() {
        score = 0
        subject.onNext(ScoreResetEvent())
    }

    fun free() {
        subject.onComplete()
    }
}
