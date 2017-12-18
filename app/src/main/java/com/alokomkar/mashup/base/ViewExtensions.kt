package com.alokomkar.mashup.base

import android.view.View
/**
 * Created by Alok Omkar on 2017-09-09.
 */

fun View.isVisible() = visibility == View.VISIBLE

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}