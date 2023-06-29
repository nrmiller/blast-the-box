package net.nicksneurons.blastthebox.game.data

import io.reactivex.rxjava3.core.Observable

interface ScoreService {

    fun addScore(amount: Int)

    fun addBonusScore(amount: Int)

    fun scoreUpdates() : Observable<ScoreEvent>

    fun getCurrentScore() : Int

    fun resetScore()
}

open class ScoreEvent() { }
open class ScoreUpdatedEvent(val amount : Int) : ScoreEvent() { }
class BonusScoreEvent(amount : Int) : ScoreUpdatedEvent(amount) { }
class ScoreResetEvent() : ScoreEvent()
