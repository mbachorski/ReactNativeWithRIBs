package pl.mbachorski.ribstutorial.rib.logged_in

import com.uber.rib.core.Router
import pl.mbachorski.ribstutorial.rib.RootView
import pl.mbachorski.ribstutorial.rib.logged_in.off_game.OffGameBuilder
import pl.mbachorski.ribstutorial.rib.logged_in.off_game.OffGameRouter
import pl.mbachorski.ribstutorial.rib.logged_in.tic_tac_toe.TicTacToeBuilder
import pl.mbachorski.ribstutorial.rib.logged_in.tic_tac_toe.TicTacToeRouter

/**
 * Adds and removes children of {@link LoggedInBuilder.LoggedInScope}.
 *
 * LoggedInRouter doesn’t have a view
 */
class LoggedInRouter(
        interactor: LoggedInInteractor,
        component: LoggedInBuilder.Component,
        private val rootView: RootView, // TODO: this should be dependency inversed -> LoggedInRouter shouldn't know about RootView. Read comment below.
        private val offGameBuilder: OffGameBuilder,
        private val ticTacToeBuilder: TicTacToeBuilder) : Router<LoggedInInteractor, LoggedInBuilder.Component>(interactor, component) {

    // TODO: Normally you should use dependency inversion when passing a parent view into a child
    // like this. Ie, LoggedIn should declare an interface called LoggedInParentView. This makes
    // the RIB more reusable and allows for builder optimizations by splitting the app into
    // multiple modules. But we’re pressed for time.

    private var offGameRouter: OffGameRouter? = null
    private var ticTacToeRouter: TicTacToeRouter? = null

    fun attachOffGame() {
        offGameRouter = offGameBuilder.build(rootView)
        attachChild(offGameRouter)
        rootView.addView(offGameRouter?.view)
    }

    fun detachOffGame() {
        offGameRouter?.let {
            detachChild(it)
            rootView.removeView(it.view)
            offGameRouter = null
        }
    }

    fun attachTicTacToe() {
        ticTacToeRouter = ticTacToeBuilder.build(rootView)
        attachChild(ticTacToeRouter)
        rootView.addView(ticTacToeRouter?.view)
    }

    fun detachTicTacToe() {
        ticTacToeRouter?.let {
            detachChild(it)
            rootView.removeView(it.view)
            ticTacToeRouter = null
        }
    }

    override fun willDetach() {
        super.willDetach()
        detachOffGame()
        detachTicTacToe()
    }
}
