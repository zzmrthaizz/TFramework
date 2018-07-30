package co.starec.tframework.base.mvp


interface BasePresenter<ViewType> {

    fun setView(pView: ViewType)
}