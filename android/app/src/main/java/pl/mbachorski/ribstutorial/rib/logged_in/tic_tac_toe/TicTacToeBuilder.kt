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


import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import pl.mbachorski.ribstutorial.R
import pl.mbachorski.ribstutorial.rib.logged_in.tic_tac_toe.TicTacToeBuilder.TicTacToeScope
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the [TicTacToeScope].
 */
class TicTacToeBuilder(dependency: ParentComponent) : ViewBuilder<TicTacToeView, TicTacToeRouter, TicTacToeBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [TicTacToeRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [TicTacToeRouter].
     */
    fun build(parentViewGroup: ViewGroup): TicTacToeRouter {
        val view = createView(parentViewGroup)
        val interactor = TicTacToeInteractor()
        val component = DaggerTicTacToeBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.tictactoeRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): TicTacToeView {
        return inflater.inflate(R.layout.tic_tac_toe_rib, parentViewGroup, false) as TicTacToeView
    }

    interface ParentComponent {
        fun ticTacToeListenerGameWon(): TicTacToeInteractor.Listener

        @Named("player_one")
        fun playerOne(): String

        @Named("player_two")
        fun playerTwo(): String
    }

    @dagger.Module
    abstract class Module {

        @TicTacToeScope
        @Binds
        internal abstract fun presenter(view: TicTacToeView): TicTacToeInteractor.TicTacToePresenter

        @dagger.Module
        companion object {

            @TicTacToeScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: TicTacToeView,
                    interactor: TicTacToeInteractor): TicTacToeRouter {
                return TicTacToeRouter(view, interactor, component)
            }
        }
    }

    @TicTacToeScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<TicTacToeInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {

            @BindsInstance
            fun interactor(interactor: TicTacToeInteractor): Builder

            @BindsInstance
            fun view(view: TicTacToeView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {

        fun tictactoeRouter(): TicTacToeRouter
    }

    @Scope
    @kotlin.annotation.Retention
    internal annotation class TicTacToeScope

    @Qualifier
    @kotlin.annotation.Retention
    internal annotation class TicTacToeInternal
}
