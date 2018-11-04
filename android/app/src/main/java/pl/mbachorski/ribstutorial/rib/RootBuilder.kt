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

import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import pl.mbachorski.ribstutorial.R
import pl.mbachorski.ribstutorial.di.RootDependencyProvider
import pl.mbachorski.ribstutorial.rib.RootBuilder.RootScope
import pl.mbachorski.ribstutorial.rib.logged_in.LoggedInBuilder
import pl.mbachorski.ribstutorial.rib.logged_out.LoggedOutBuilder
import pl.mbachorski.ribstutorial.rib.logged_out.LoggedOutInteractor
import javax.inject.Scope

/** Builder for the [RootScope].  */
class RootBuilder(dependency: ParentComponent) : ViewBuilder<RootView, RootRouter, RootBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [RootRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [RootRouter].
     */
    fun build(parentViewGroup: ViewGroup): RootRouter {
        val view = createView(parentViewGroup)
        val interactor = RootInteractor()
        val component = DaggerRootBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.rootRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): RootView {
        return inflater.inflate(R.layout.root_rib, parentViewGroup, false) as RootView
    }

    interface ParentComponent : RootDependencyProvider

    @dagger.Module
    abstract class Module {

        @RootScope
        @Binds
        internal abstract fun presenter(view: RootView): RootInteractor.RootPresenter

        @dagger.Module
        companion object {

            @RootScope
            @Provides
            @JvmStatic
            internal fun router(component: Component, view: RootView, interactor: RootInteractor): RootRouter {
                return RootRouter(view, interactor, component, LoggedOutBuilder(component), LoggedInBuilder(component))
            }

            @RootScope
            @Provides
            @JvmStatic
            fun provideLoggedOutListener(rootInteractor: RootInteractor): LoggedOutInteractor.Listener {
                return rootInteractor.LoggedOutListener()
            }
        }
    }

    @RootScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<RootInteractor>, BuilderComponent, LoggedOutBuilder.ParentComponent, LoggedInBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: RootInteractor): Builder

            @BindsInstance
            fun view(view: RootView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun rootRouter(): RootRouter
    }

    @Scope
    @kotlin.annotation.Retention
    internal annotation class RootScope
}
