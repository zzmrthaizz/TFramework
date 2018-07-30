package co.starec.tframework.di.component

import co.starec.tframework.app.TApplication
import co.starec.tframework.data.local.TPreference
import co.starec.tframework.di.module.ActivityModule
import co.starec.tframework.di.module.AppModule
import co.starec.tframework.di.module.DataModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    val tPreference: TPreference

    fun inject(tApplication: TApplication)
    fun plus(dataModule: DataModule, activityModule: ActivityModule): ActivitySubComponent
}
