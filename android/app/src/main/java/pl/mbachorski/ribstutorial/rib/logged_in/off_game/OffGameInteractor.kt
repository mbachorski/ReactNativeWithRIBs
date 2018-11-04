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


import com.google.common.collect.ImmutableMap
import com.uber.autodispose.ObservableScoper
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import pl.mbachorski.ribstutorial.rib.logged_in.ScoreStream
import javax.inject.Inject
import javax.inject.Named

/**
 * Coordinates Business Logic for [OffGameScope].
 */
@RibInteractor
class OffGameInteractor : Interactor<OffGameInteractor.OffGamePresenter, OffGameRouter>() {

    @Inject
    lateinit var listener: Listener

    @Inject
    lateinit var presenter: OffGamePresenter

    @field:[Inject Named("player_one")]
    lateinit var playerOne: String

    @Inject
    @field:Named("player_two")
    lateinit var playerTwo: String

    @Inject
    lateinit var scoreStream: ScoreStream

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        /**
         * what happens if we want to detach and garbage collect the OffGame RIB?
         * Currently, it will cause a memory leak because it's subscribed to the ScoreStream
         * which is scoped to the LoggedInScope. To fix this, we update our code to use AutoDispose
         * to automatically unsubscribe when OffGame is detached:
         */
        scoreStream
                .scores()
                // AutoDispose
                .to(ObservableScoper<ImmutableMap<String, Int>>(this))
                .subscribe {
                    val playerOneScore = it[playerOne]
                    val playerTwoScore = it[playerTwo]
                    presenter.setScores(playerOneScore, playerTwoScore)
                }
        presenter.setPlayerNames(playerOne, playerTwo)
        presenter
                .startGameRequest()
                .subscribe { listener.onStartGame() }
    }

    interface Listener {
        fun onStartGame()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface OffGamePresenter {
        fun setPlayerNames(playerOne: String, playerTwo: String)
        fun startGameRequest(): Observable<Any>
        fun setScores(playerOneScore: Int?, playerTwoScore: Int?)
    }
}
