package pl.mbachorski.ribstutorial.di

import pl.mbachorski.ribstutorial.service.IRootSecretService
import pl.mbachorski.ribstutorial.service.ISimpleMessageShower

interface RootDependencyProvider {
    fun provideSimpleMessageShower(): ISimpleMessageShower
    fun provideRootSecretService(): IRootSecretService
}