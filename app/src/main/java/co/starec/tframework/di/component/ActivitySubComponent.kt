package co.starec.tframework.di.component

import co.starec.tframework.base.activity.BaseActivity
import co.starec.tframework.di.module.ActivityModule
import co.starec.tframework.di.module.DataModule
import dagger.Subcomponent
import javax.inject.Singleton


@Singleton
@Subcomponent(modules = [(DataModule::class), (ActivityModule::class)])
interface ActivitySubComponent {
    fun inject(baseActivity: BaseActivity)
}

