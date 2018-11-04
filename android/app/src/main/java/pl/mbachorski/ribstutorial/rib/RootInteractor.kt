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

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import pl.mbachorski.ribstutorial.rib.logged_out.LoggedOutInteractor
import pl.mbachorski.ribstutorial.service.IRootSecretService
import pl.mbachorski.ribstutorial.service.ISimpleMessageShower


import javax.inject.Inject

/** Coordinates Business Logic for [RootBuilder.RootScope].  */
@RibInteractor
class RootInteractor : Interactor<RootInteractor.RootPresenter, RootRouter>() {

    @Inject
    lateinit var presenter: RootPresenter

    @Inject
    lateinit var messageShower: ISimpleMessageShower

    @Inject
    lateinit var rootSecretService: IRootSecretService

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // Add attachment logic here (RxSubscriptions, etc.).
        router.attachLoggedOut()
        messageShower.showMessage("It's ALIVE!")
        rootSecretService.call007("I work only for ROOT!")
    }

    /** Presenter interface implemented by this RIB's view.  */
    interface RootPresenter

    inner class LoggedOutListener : LoggedOutInteractor.Listener {
        override fun login(playerOne: String, playerTwo: String) {
            router.detachLoggedOut()
            router.attachLoggedIn(playerOne, playerTwo)
        }
    }
}
