package co.starec.tframework.widget.dotviewpager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import co.starec.runningapp.runther.framework.widget.dotviewpager.IRadioButton
import co.starec.tframework.R

class DotView(context: Context, attrs: AttributeSet) : View(context, attrs), IRadioButton {
    override fun setCheck(checked: Boolean) {
        if (checked) {
            setBackgroundResource(R.drawable.dot_selected)
        } else {
            setBackgroundResource(R.drawable.dot_normal)
        }
    }

    init {
        setCheck(false)
    }
}