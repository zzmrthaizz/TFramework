package co.starec.tframework.base.service

import android.app.Service
import co.starec.tframework.data.local.TPreference
import co.starec.tframework.di.component.ServiceComponent
import co.starec.tframework.di.module.DataModule
import co.starec.tframework.app.TApplication
import co.starec.tframework.di.component.DaggerServiceComponent
import javax.inject.Inject

/**
 * Created by ThaiDang.
 * Since: 2/23/17 on 4:28 PM.
 * Project: Tfamework
 */

abstract class BaseService : Service() {

    private var mServiceComponent: ServiceComponent? = null
    internal val serviceComponent: ServiceComponent
        get() {
            if (mServiceComponent == null) {
                mServiceComponent = DaggerServiceComponent.builder()
                        .dataModule(DataModule(TApplication[this]))
                        .build()
            }
            return mServiceComponent as ServiceComponent
        }


    @Inject
    lateinit var preference: TPreference

    override fun onCreate() {
        onServiceStart()
    }

    protected abstract fun onServiceStart()
}
