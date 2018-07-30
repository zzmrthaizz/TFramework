package co.starec.tframework.app

import android.app.Application
import android.content.Context
import co.starec.tframework.data.local.TPreference
import co.starec.tframework.di.component.AppComponent
import co.starec.tframework.di.module.AppModule
import co.starec.tframework.R
import co.starec.tframework.di.component.DaggerAppComponent
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject

class TApplication : Application() {
    var appComponent: AppComponent? = null
        private set

    @Inject
    lateinit var tPreference: TPreference
        internal set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent!!.inject(this)

        //  Set default font
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(Constants.APP_DEFAULT_FONT)
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    companion object {
        operator fun get(context: Context): TApplication {
            return context.applicationContext as TApplication
        }
    }

}
