package pl.mbachorski.ribstutorial.rib.logged_out

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link LoggedOutBuilder.LoggedOutScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class LoggedOutRouter(
    view: LoggedOutView,
    interactor: LoggedOutInteractor,
    component: LoggedOutBuilder.Component) : ViewRouter<LoggedOutView, LoggedOutInteractor, LoggedOutBuilder.Component>(view, interactor, component)
