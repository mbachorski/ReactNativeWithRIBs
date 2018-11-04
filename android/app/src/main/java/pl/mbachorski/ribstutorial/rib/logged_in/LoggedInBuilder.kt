package pl.mbachorski.ribstutorial.rib.logged_in

import com.uber.rib.core.Builder
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import pl.mbachorski.ribstutorial.rib.RootView
import pl.mbachorski.ribstutorial.rib.logged_in.off_game.OffGameBuilder
import pl.mbachorski.ribstutorial.rib.logged_in.off_game.OffGameInteractor
import pl.mbachorski.ribstutorial.rib.logged_in.tic_tac_toe.TicTacToeBuilder
import pl.mbachorski.ribstutorial.rib.logged_in.tic_tac_toe.TicTacToeInteractor
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope


class LoggedInBuilder(dependency: ParentComponent) : Builder<LoggedInRouter, LoggedInBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [LoggedInRouter].
     *
     * @return a new [LoggedInRouter].
     */
    fun build(playerOne: String, playerTwo: String): LoggedInRouter {
        val interactor = LoggedInInteractor()
        val component = DaggerLoggedInBuilder_Component.builder()
                .parentComponent(dependency)
                .interactor(interactor)
                .playerOne(playerOne)
                .playerTwo(playerTwo)
                .build()

        return component.loggedinRouter()
    }

    interface ParentComponent {
        // TODO: Define dependencies required from your parent interactor here.
        fun rootView(): RootView
    }

    @dagger.Module
    abstract class Module {

        /**
         * @Binds -> this is just shorthand for creating an @Provides method that takes
         * the MutableScoreStream and returns a ScoreStream
         * This is to provide ScoreStream to OffGameBuilder's ParentComponent and then to Interactor
         */
        @Binds
        @LoggedInScope
        internal abstract fun scoreStream(@LoggedInInternal mutableScoreStream: MutableScoreStream): ScoreStream

        @dagger.Module
        companion object {

            @LoggedInScope
            @Provides
            @JvmStatic
            internal fun presenter(): EmptyPresenter {
                return EmptyPresenter()
            }

            @LoggedInScope
            @Provides
            @JvmStatic
            internal fun router(component: Component, interactor: LoggedInInteractor, rootView: RootView): LoggedInRouter {
                return LoggedInRouter(interactor, component, rootView, OffGameBuilder(component), TicTacToeBuilder(component))
            }

            // TODO: Create provider methods for dependencies created by this Rib. These methods should be static.

            @LoggedInScope
            @Provides
            @JvmStatic
            fun offGameListener(interactor: LoggedInInteractor): OffGameInteractor.Listener {
                return interactor.OffGameListener()
            }

            /**
             * @LoggedInInternal -> Dagger qualifier that is generated for free with every RIB when using the
             * IntelliJ template. It's a package-private qualifier to prevent child RIBs from
             * listing the MutableScoreStream in their ParentComponent.Also worth noting, using
             * @LoggedInScope ensures that the score stream is a singleton for the logged in RIB
             * and all of its children.
             */
            @LoggedInScope
            @LoggedInInternal
            @Provides
            @JvmStatic
            internal fun mutableScoreStream(
                    @Named("player_one") playerOne: String,
                    @Named("player_two") playerTwo: String): MutableScoreStream {
                return MutableScoreStream(playerOne, playerTwo)
            }

            @LoggedInScope
            @Provides
            @JvmStatic
            internal fun ticTacToeListener(interactor: LoggedInInteractor): TicTacToeInteractor.Listener {
                return interactor.GameListener()
            }
        }
    }

    @LoggedInScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<LoggedInInteractor>, BuilderComponent, OffGameBuilder.ParentComponent, TicTacToeBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: LoggedInInteractor): Builder

            /**
             * NOTE: These dependencies are provided using the new BindsInstance Dagger API.
             * It's similar to using @Provides, but allows us to not have to pass the player
             * names into the module via its constructor.
             */
            @BindsInstance
            fun playerOne(@Named("player_one") playerOne: String): Builder

            @BindsInstance
            fun playerTwo(@Named("player_two") playerTwo: String): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun loggedinRouter(): LoggedInRouter
    }

    @Scope
    @kotlin.annotation.Retention
    internal annotation class LoggedInScope


    @Qualifier
    @kotlin.annotation.Retention
    internal annotation class LoggedInInternal
}
