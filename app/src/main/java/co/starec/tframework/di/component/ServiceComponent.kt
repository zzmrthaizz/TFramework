package co.starec.tframework.di.component

import co.starec.tframework.base.service.BaseService
import co.starec.tframework.di.module.AppModule
import co.starec.tframework.di.module.DataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = [(AppModule::class), (DataModule::class)])
interface ServiceComponent {
    fun inject(baseService: BaseService)
}
