package com.alokomkar.mashup.songs

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alokomkar.mashup.R
import com.alokomkar.mashup.base.BaseFragment
import com.alokomkar.mashup.base.hide
import com.alokomkar.mashup.base.show
import kotlinx.android.synthetic.main.fragment_songs_list.*

/**
 * Created by Alok Omkar on 2017-12-16.
 */
class SongsListFragment : BaseFragment(), SongsView, TextWatcher {

    private lateinit var mSongsPresenter : SongsPresenter

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * [.onCreate] and [.onActivityCreated].
     *
     *
     * If you return a View from here, you will later be called in
     * [.onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_songs_list, container, false)
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSongsPresenter = SongsPresenter(this)
        mSongsPresenter.getSongs()
        searchEditText.addTextChangedListener(this)
        searchEditText.setOnTouchListener { view, event ->
            if( event!!.action == MotionEvent.ACTION_DOWN )
                searchEditText.requestFocus()
            true
        }
        searchEditText.clearFocus()
    }

    override fun showProgress(message: String) {
        progressLayout.show()
    }

    override fun hideProgress() {
        progressLayout.hide()
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private var mSongsList: ArrayList<Songs> ?= null

    private var mSongsAdapter: SongsRecyclerAdapter?= null

    override fun onSuccess(songsList: ArrayList<Songs>) {
        mSongsList = songsList
        songsRecyclerView.layoutManager = LinearLayoutManager(context)
        mSongsAdapter = SongsRecyclerAdapter(songsList, this as SongsView)
        songsRecyclerView.adapter = mSongsAdapter
    }

    override fun onSongSelect(songIndex: Int) {
        mNavigationListener.loadPlayerFragment(mSongsList!![songIndex], mSongsList!!)
    }

    /**
     * Called when the view previously created by [.onCreateView] has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after [.onStop] and before [.onDestroy].  It is called
     * *regardless* of whether [.onCreateView] returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        mSongsPresenter.onDestroy()
    }

    override fun afterTextChanged(p0: Editable?) {
        if( p0 != null && mSongsAdapter != null ) {
            mSongsAdapter!!.filterList(p0.toString().trim())
        }
        else {
            mSongsAdapter!!.filterList("")
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
}