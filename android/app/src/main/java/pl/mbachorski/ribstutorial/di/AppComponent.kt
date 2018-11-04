package pl.mbachorski.ribstutorial.di

import dagger.Component
import pl.mbachorski.ribstutorial.rib.RootBuilder
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent : RootBuilder.ParentComponent, RootDependencyProvider
