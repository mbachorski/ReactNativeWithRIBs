/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.mbachorski.ribstutorial.rib.logged_in.tic_tac_toe


import android.content.Context
import android.support.percent.PercentRelativeLayout
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.uber.rib.core.Initializer
import io.reactivex.Observable
import pl.mbachorski.ribstutorial.R
import java.util.*

/**
 * Top level view for [TicTacToeBuilder.TicTacToeScope].
 */
class TicTacToeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : PercentRelativeLayout(context, attrs, defStyle), TicTacToeInteractor.TicTacToePresenter {

    private var imageButtons: Array<Array<TextView>?>? = null
    private var titleView: TextView? = null

    @Initializer
    override fun onFinishInflate() {
        super.onFinishInflate()
        imageButtons = arrayOfNulls(3)
        imageButtons!![0] = arrayOf(findViewById<TextView>(R.id.button11) as TextView, findViewById<Button>(R.id.button12) as TextView, findViewById<TextView>(R.id.button13) as TextView)
        imageButtons!![1] = arrayOf(findViewById<TextView>(R.id.button21) as TextView, findViewById<Button>(R.id.button22) as TextView, findViewById<TextView>(R.id.button23) as TextView)
        imageButtons!![2] = arrayOf(findViewById<TextView>(R.id.button31) as TextView, findViewById<Button>(R.id.button32) as TextView, findViewById<TextView>(R.id.button33) as TextView)
        titleView = findViewById<TextView>(R.id.title)
    }

    override fun squareClicks(): Observable<BoardCoordinate> {
        val observables = ArrayList<Observable<BoardCoordinate>>()
        for (i in 0..2) {
            for (j in 0..2) {
                observables.add(
                        RxView.clicks(imageButtons!![i]!![j])
                                .map { BoardCoordinate(i, j) })
            }
        }
        return Observable.merge(observables)
    }

    override fun addCross(xy: BoardCoordinate) {
        val textView = imageButtons!![xy.x]!![xy.y]
        textView.text = "x"
        textView.isClickable = false
    }

    override fun addNought(xy: BoardCoordinate) {
        val textView = imageButtons!![xy.x]!![xy.y]
        textView.text = "O"
        textView.isClickable = false
    }

    override fun setCurrentPlayerName(currentPlayer: String) {
        titleView!!.text = "Current Player: $currentPlayer"
    }

    override fun setPlayerWon(playerName: String) {
        titleView!!.text = "Player won: $playerName !"
    }

    override fun setPlayerTie() {
        titleView!!.text = "Tie game!"
    }
}
