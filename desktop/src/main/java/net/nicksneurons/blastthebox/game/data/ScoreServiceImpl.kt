package net.nicksneurons.blastthebox.game.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

class ScoreServiceImpl @Inject constructor() : ScoreService {

    private var subject: Subject<ScoreEvent> = PublishSubject.create()

    private var score: Float = 0.0f

    override fun scoreUpdates(): Observable<ScoreEvent> {
        return subject
    }


    override fun addScore(amount: Float) {
        score += amount
        subject.onNext(ScoreUpdatedEvent(amount))
    }

    override fun addBonusScore(amount: Float) {
        score += amount
        subject.onNext(BonusScoreEvent(amount))
    }

    override fun getCurrentScore(): Float {
        return score
    }

    override fun resetScore() {
        score = 0.0f
        subject.onNext(ScoreResetEvent())
    }

    fun free() {
        subject.onComplete()
    }
}
