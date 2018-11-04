package pl.mbachorski.ribstutorial.rib.logged_in

import com.google.common.collect.ImmutableMap
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable


class MutableScoreStream(private val playerOne: String, private val playerTwo: String) : ScoreStream {

    // returns only 1 last from past and all new
    private val scoresRelay = BehaviorRelay.create<ImmutableMap<String, Int>>()

    init {
        scoresRelay.accept(ImmutableMap.of(playerOne, 0, playerTwo, 0))
    }

    fun addVictory(userName: String) {
        val currentScores = scoresRelay.value

        val newScoreMapBuilder = ImmutableMap.Builder<String, Int>()
        for (entry in currentScores.entries) {
            if (entry.key == userName) {
                newScoreMapBuilder.put(entry.key, entry.value + 1)
            } else {
                newScoreMapBuilder.put(entry.key, entry.value)
            }
        }

        scoresRelay.accept(newScoreMapBuilder.build())
    }

    override fun scores(): Observable<ImmutableMap<String, Int>> {
        // What is hide for? -> https://stackoverflow.com/a/46868365/770305
        return scoresRelay.hide()
    }
}