package co.starec.runningapp.runther.framework.widget.dotviewpager

import android.view.View

class RadioGroupController : View.OnClickListener {

    lateinit private var radioButtons: Array<IRadioButton>
    private var selectedIndex: Int = 0
    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    fun setRadioButtons(buttons: Array<IRadioButton>) {
        radioButtons = buttons
        buttons[selectedIndex].setCheck(true)
    }

    override fun onClick(v: View) {
        setSelection(v.tag as Int)
    }

    val checkedRadioButtonId: Int
        get() = (radioButtons!![selectedIndex] as View).getId()

    fun setOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(checkedId: Int, position: Int)
    }

    fun setSelection(position: Int) {
        if (radioButtons.size <= position || position < 0) {
            return
        }

        if (position == selectedIndex) {
            return
        }

        radioButtons[position].setCheck(true)
        if (selectedIndex >= 0) {
            radioButtons[selectedIndex].setCheck(false)
        }
        selectedIndex = position
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener!!.onCheckedChanged((radioButtons[selectedIndex] as View).id, selectedIndex)
        }
    }
}