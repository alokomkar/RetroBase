package com.alokomkar.mashup.base

import android.content.Context
import android.support.v4.app.Fragment
import com.alokomkar.mashup.NavigationListener


/**
 * Created by Alok Omkar on 2017-12-16.
 */
abstract class BaseFragment : Fragment(){

    lateinit var mNavigationListener : NavigationListener

    /**
     * Called when a fragment is first attached to its context.
     * [.onCreate] will be called after this.
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if( context is NavigationListener )
            mNavigationListener = context
    }

}