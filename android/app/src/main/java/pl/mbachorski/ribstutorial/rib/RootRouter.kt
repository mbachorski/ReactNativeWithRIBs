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

package pl.mbachorski.ribstutorial.rib

import com.uber.rib.core.ViewRouter
import pl.mbachorski.ribstutorial.rib.logged_in.LoggedInBuilder
import pl.mbachorski.ribstutorial.rib.logged_out.LoggedOutBuilder
import pl.mbachorski.ribstutorial.rib.logged_out.LoggedOutRouter

/** Adds and removes children of [RootBuilder.RootScope].  */
class RootRouter internal constructor(view: RootView, interactor: RootInteractor,
                                      component: RootBuilder.Component, private val loggedOutBuilder: LoggedOutBuilder,
                                      private val loggedInBuilder: LoggedInBuilder) :
        ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {
    private var loggedOutRouter: LoggedOutRouter? = null

    fun attachLoggedOut() {
        loggedOutRouter = loggedOutBuilder.build(view)
        attachChild(loggedOutRouter)
        view.addView(loggedOutRouter!!.view)
    }

    fun detachLoggedOut() {
        if (loggedOutRouter != null) {
            detachChild(loggedOutRouter)
            view.removeView(loggedOutRouter!!.view)
            loggedOutRouter = null
        }
    }

    fun attachLoggedIn(playerOne: String, playerTwo: String) {
        attachChild(loggedInBuilder.build(playerOne, playerTwo))
    }
}
