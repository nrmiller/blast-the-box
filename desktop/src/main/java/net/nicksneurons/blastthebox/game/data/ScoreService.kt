package net.nicksneurons.blastthebox.game.data

import io.reactivex.rxjava3.core.Observable

interface ScoreService {

    fun addScore(amount: Float)

    fun addBonusScore(amount: Float)

    fun scoreUpdates() : Observable<ScoreEvent>

    fun getCurrentScore() : Float

    fun resetScore()
}

open class ScoreEvent() { }
open class ScoreUpdatedEvent(val amount : Float) : ScoreEvent() { }
class BonusScoreEvent(amount : Float) : ScoreUpdatedEvent(amount) { }
class ScoreResetEvent() : ScoreEvent()
