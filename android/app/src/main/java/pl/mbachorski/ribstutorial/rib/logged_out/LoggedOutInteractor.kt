package pl.mbachorski.ribstutorial.rib.logged_out

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import pl.mbachorski.ribstutorial.service.ISimpleMessageShower
import javax.inject.Inject

/**
 * Coordinates Business Logic for [LoggedOutScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class LoggedOutInteractor : Interactor<LoggedOutInteractor.LoggedOutPresenter, LoggedOutRouter>() {

    @Inject
    lateinit var presenter: LoggedOutPresenter

    // will be populated from the builder.
    @Inject
    lateinit var listener: Listener

    @Inject
    lateinit var messageShower: ISimpleMessageShower

    // This will not work as this dependency is not exposed in ParentComponent
    // like ISimpleMessageShower
//    @Inject
//    lateinit var rootSecretService: IRootSecretService

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        messageShower.showMessage("I'm inside LoggedOut and I can :D")

        presenter
                .loginName()
                .subscribe {
                    //                    Log.d("MOO", it)
                    if (!it.first.isEmpty() && !it.second.isEmpty()) {
                        listener.login(it.first, it.second)
                    }
                }
    }

    override fun willResignActive() {
        super.willResignActive()

        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
    }

    /**
     * Presenter interface implemented by this RIB's view.
     * Presenter interfaces like this don't need to be used. You could just directly call your view
     * from your interactor. In practice we've found this causes a cleaner seperation between views
     * and interactors, for psychological reasons.
     */
    interface LoggedOutPresenter {
        fun loginName(): Observable<Pair<String, String>>
    }

    /**
     * Listener for parent RIB
     */
    interface Listener {
        fun login(playerOne: String, playerTwo: String)
    }
}
