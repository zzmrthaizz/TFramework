package co.starec.tframework.base.mvp

interface BaseView {
    fun showLoading()

    fun hideLoading()

    fun showRetry()

    fun hideRetry()

    fun showError(error: String)
}
