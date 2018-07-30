package co.starec.tframework.di.module

import android.support.v7.app.AppCompatActivity
import co.starec.tframework.app.TAnotation.ActivityContext
import co.starec.runningapp.runther.framework.base.FragmentNavigator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ActivityModule(private var mActivity: AppCompatActivity, private var mContainerId: Int) {

    @Provides
    @ActivityContext
    fun provideActivityContext(): AppCompatActivity {
        return mActivity
    }

    @Provides
    @Singleton
    fun provideFragmentNavigator(): FragmentNavigator {
        return FragmentNavigator(mActivity.supportFragmentManager, mContainerId)
    }
}
