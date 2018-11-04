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


import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Coordinates Business Logic for [TicTacToeScope].
 */
@RibInteractor
class TicTacToeInteractor : Interactor<TicTacToeInteractor.TicTacToePresenter, TicTacToeRouter>() {

    @Inject
    lateinit var board: Board

    @Inject
    lateinit var presenter: TicTacToePresenter

    @Inject
    lateinit var listener: Listener

    @field:[Inject Named("player_one")]
    lateinit var playerOne: String

    @Inject
    @field:Named("player_two")
    lateinit var playerTwo: String

    private var currentPlayer: Board.MarkerType = Board.MarkerType.CROSS

    companion object {
        const val TIE_GAME = "TIE GAME"
    }

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        presenter
                .squareClicks()
                .subscribe { xy ->
                    if (board.cells[xy.x][xy.y] == null) {
                        if (currentPlayer == Board.MarkerType.CROSS) {
                            board.cells[xy.x][xy.y] = Board.MarkerType.CROSS
                            board.currentRow = xy.x
                            board.currentCol = xy.y
                            presenter.addCross(xy)
                            currentPlayer = Board.MarkerType.NOUGHT
                        } else {
                            board.cells[xy.x][xy.y] = Board.MarkerType.NOUGHT
                            board.currentRow = xy.x
                            board.currentCol = xy.y
                            presenter.addNought(xy)
                            currentPlayer = Board.MarkerType.CROSS
                        }
                    }
                    if (board.hasWon(Board.MarkerType.CROSS)) {
                        presenter.setPlayerWon(playerOne)
                        listener.gameWon(playerOne)
                    } else if (board.hasWon(Board.MarkerType.NOUGHT)) {
                        presenter.setPlayerWon(playerTwo)
                        listener.gameWon(playerTwo)
                    } else if (board.isDraw) {
                        presenter.setPlayerTie()
                        listener.gameWon(TIE_GAME)
                    } else {
                        updateCurrentPlayer()
                    }
                }
        updateCurrentPlayer()
    }

    private fun updateCurrentPlayer() {
        if (currentPlayer == Board.MarkerType.CROSS) {
            presenter.setCurrentPlayerName(playerOne)
        } else {
            presenter.setCurrentPlayerName(playerTwo)
        }
    }


    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface TicTacToePresenter {
        fun squareClicks(): Observable<BoardCoordinate>

        fun setCurrentPlayerName(currentPlayer: String)

        fun setPlayerWon(playerName: String)

        fun setPlayerTie()

        fun addCross(xy: BoardCoordinate)

        fun addNought(xy: BoardCoordinate)
    }

    interface Listener {
        // used to communicate with parent after user wins a game
        fun gameWon(winner: String)
    }
}
