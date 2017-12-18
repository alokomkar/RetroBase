package com.alokomkar.mashup.base

interface BaseView {
    fun showProgress(message: String)
    fun hideProgress()
    fun showError(error: String)
}
