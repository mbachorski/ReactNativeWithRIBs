package pl.mbachorski.ribstutorial

import android.app.Application
import pl.mbachorski.ribstutorial.di.AppComponent
import pl.mbachorski.ribstutorial.di.AppModule
import pl.mbachorski.ribstutorial.di.DaggerAppComponent

class App : Application() {
    private var component: AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        App.app = this
        initDagger()
    }

    private fun initDagger() {
        app.component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getAppComponent(): AppComponent {
        return app.component!!
    }

    companion object {
        lateinit var app: App
    }
}