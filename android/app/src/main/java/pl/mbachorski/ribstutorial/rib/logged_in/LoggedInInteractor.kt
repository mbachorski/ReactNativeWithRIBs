package pl.mbachorski.ribstutorial.rib.logged_in

import android.util.Log
import com.uber.rib.core.Bundle
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import pl.mbachorski.ribstutorial.rib.logged_in.off_game.OffGameInteractor
import pl.mbachorski.ribstutorial.rib.logged_in.tic_tac_toe.TicTacToeInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [LoggedInScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class LoggedInInteractor : Interactor<EmptyPresenter, LoggedInRouter>() {

    /**
     * !!!
     * This is how to inject annotated field in kotlin.
     * In Java it's just  @Inject @LoggedInInternal MutableScoreStream scoreStream;
     * !!!
     */
    @Inject
    @field:LoggedInBuilder.LoggedInInternal
    lateinit var mutableScoreStream: MutableScoreStream

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).

        // when first logging in we should be in the OffGame state
        router.attachOffGame()
    }

    override fun willResignActive() {
        super.willResignActive()

        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
    }

    inner class OffGameListener : OffGameInteractor.Listener {
        override fun onStartGame() {
            router.detachOffGame()
            router.attachTicTacToe()
        }
    }

    inner class GameListener : TicTacToeInteractor.Listener {
        override fun gameWon(winner: String) {
            Log.v("GAME", "winner: $winner")
            if (TicTacToeInteractor.TIE_GAME != winner) {
                mutableScoreStream.addVictory(winner)
            }
            router.detachTicTacToe()
            router.attachOffGame()
        }
    }
}
