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


import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import pl.mbachorski.ribstutorial.R
import pl.mbachorski.ribstutorial.rib.logged_in.ScoreStream
import pl.mbachorski.ribstutorial.rib.logged_in.off_game.OffGameBuilder.OffGameScope
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the [OffGameScope].
 */
class OffGameBuilder(dependency: ParentComponent) : ViewBuilder<OffGameView, OffGameRouter, OffGameBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [OffGameRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [OffGameRouter].
     */
    fun build(parentViewGroup: ViewGroup): OffGameRouter {
        val view = createView(parentViewGroup)
        val interactor = OffGameInteractor()
        val component = DaggerOffGameBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.offgameRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): OffGameView {
        return inflater.inflate(R.layout.off_game_rib, parentViewGroup, false) as OffGameView
    }

    interface ParentComponent {
        @Named("player_one")
        fun playerOne(): String

        @Named("player_two")
        fun playerTwo(): String

        fun listener(): OffGameInteractor.Listener

        fun scoreStream(): ScoreStream
    }

    @dagger.Module
    abstract class Module {

        @OffGameScope
        @Binds
        internal abstract fun presenter(view: OffGameView): OffGameInteractor.OffGamePresenter

        @dagger.Module
        companion object {

            @OffGameScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: OffGameView,
                    interactor: OffGameInteractor): OffGameRouter {
                return OffGameRouter(view, interactor, component)
            }
        }
    }

    @OffGameScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<OffGameInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {

            @BindsInstance
            fun interactor(interactor: OffGameInteractor): Builder

            @BindsInstance
            fun view(view: OffGameView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {

        fun offgameRouter(): OffGameRouter
    }

    @Scope
    @kotlin.annotation.Retention
    internal annotation class OffGameScope

    @Qualifier
    @kotlin.annotation.Retention
    internal annotation class OffGameInternal
}
