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

package pl.mbachorski.ribstutorial.rib.logged_in.off_game


import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.view.RxView
import com.uber.rib.core.Initializer
import io.reactivex.Observable
import kotlinx.android.synthetic.main.off_game_rib.view.*

/**
 * Top level view for [OffGameBuilder.OffGameScope].
 */
class OffGameView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), OffGameInteractor.OffGamePresenter {
    override fun setScores(playerOneScore: Int?, playerTwoScore: Int?) {
        player_one_win_count.text = playerOneScore.toString()
        player_two_win_count.text = playerTwoScore.toString()
    }

    override fun setPlayerNames(playerOne: String, playerTwo: String) {
        player_one_name.text = playerOne
        player_two_name.text = playerTwo
    }

    @Initializer
    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun startGameRequest(): Observable<Any> {
        return RxView.clicks(start_game_button)
    }
}
