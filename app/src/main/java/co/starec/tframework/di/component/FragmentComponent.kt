package co.starec.tframework.di.component

import co.starec.tframework.base.fragment.BaseFragment
import co.starec.tframework.di.module.AppModule
import co.starec.tframework.di.module.DataModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class), (DataModule::class)])
interface FragmentComponent {
    fun inject(baseFragment: BaseFragment)
}
