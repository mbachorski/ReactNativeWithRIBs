package pl.mbachorski.ribstutorial.di

import android.content.Context
import dagger.Module
import dagger.Provides
import pl.mbachorski.ribstutorial.App
import pl.mbachorski.ribstutorial.service.AndroidSimpleMessageShower
import pl.mbachorski.ribstutorial.service.IRootSecretService
import pl.mbachorski.ribstutorial.service.ISimpleMessageShower
import pl.mbachorski.ribstutorial.service.RootSecretService
import javax.inject.Singleton

@Module
class AppModule constructor(private val app: App) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideSimpleMessageShower(context: Context): ISimpleMessageShower {
        return AndroidSimpleMessageShower(context)
    }

    @Provides
    @Singleton
    fun provideRootSecretService(context: Context): IRootSecretService {
        return RootSecretService(context)
    }
}