package co.starec.tframework.base.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.starec.tframework.app.TApplication
import co.starec.runningapp.runther.framework.base.FragmentNavigator
import co.starec.tframework.data.local.TPreference
import co.starec.tframework.di.component.FragmentComponent
import co.starec.tframework.di.module.DataModule
import co.starec.tframework.base.activity.BaseActivity
import co.starec.tframework.di.component.DaggerFragmentComponent
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    private var fragmentNavigator: FragmentNavigator? = null
    lateinit var dataComponent: FragmentComponent

    @Inject
    lateinit var tPreference: TPreference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNavigator = (activity as BaseActivity).fragmentNavigator
        dataComponent = DaggerFragmentComponent.builder()
                .dataModule(DataModule(TApplication[context]))
                .build()
        onScreenVisible()
    }

    protected abstract fun onScreenVisible()

    protected abstract fun setLayout(): Int

    val context: Context
        @JvmName("getContext2")
        get() = getContext()!!
}
