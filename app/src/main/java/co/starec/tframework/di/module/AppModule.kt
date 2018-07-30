package co.starec.tframework.di.module

import android.app.Application
import android.content.Context

import co.starec.tframework.data.local.TPreference
import co.starec.runningapp.runther.framework.utils.security.ObscuredSharedPreferences
import co.starec.tframework.R

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import co.starec.tframework.app.TAnotation.ApplicationContext


@Module
class AppModule(var mApplication: Application) {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context {
        return mApplication
    }

    @Provides
    @Singleton
    fun provideObscuredSharedPreferences(): ObscuredSharedPreferences {
        return ObscuredSharedPreferences(this.mApplication,
                mApplication.getSharedPreferences(this.mApplication.getString(R.string.app_name), Context.MODE_PRIVATE))
    }

    @Provides
    @Singleton
    fun provideTPreference(obscuredSharedPreferences: ObscuredSharedPreferences): TPreference {
        return TPreference(obscuredSharedPreferences)
    }

}
