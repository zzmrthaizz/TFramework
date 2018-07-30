package co.starec.tframework.base.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import co.starec.runningapp.runther.framework.base.FragmentNavigator
import co.starec.tframework.di.component.ActivitySubComponent
import co.starec.tframework.di.module.ActivityModule
import co.starec.tframework.di.module.DataModule
import co.starec.tframework.app.TApplication
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mActivityComponent: ActivitySubComponent

    @Inject
    lateinit var fragmentNavigator: FragmentNavigator

    private var containerId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayout())
        containerId = setContainer()
        mActivityComponent = (application as TApplication).appComponent!!.
                plus(DataModule(application as TApplication), ActivityModule(this, containerId))
        mActivityComponent.inject(this)
        onCreated()
    }

    protected abstract fun onCreated()

    protected abstract fun setContainer(): Int

    protected abstract fun setLayout(): Int

    protected fun hideKeyboard(context: Context) {
        try {
            val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                    INPUT_METHOD_SERVICE) as InputMethodManager
            if (activity.currentFocus != null)
                inputMethodManager.hideSoftInputFromWindow(
                        activity.currentFocus!!.windowToken, 0)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
