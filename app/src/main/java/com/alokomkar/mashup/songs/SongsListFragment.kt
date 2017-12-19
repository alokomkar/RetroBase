package com.alokomkar.mashup.songs

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.alokomkar.mashup.MashUpApplication
import com.alokomkar.mashup.R
import com.alokomkar.mashup.base.BaseFragment
import com.alokomkar.mashup.base.handleMultiplePermission
import com.alokomkar.mashup.base.hide
import com.alokomkar.mashup.base.show
import com.alokomkar.mashup.download.DownloadFileTask
import com.alokomkar.mashup.download.DownloadsPresenter
import kotlinx.android.synthetic.main.fragment_songs_list.*
import java.io.File

/**
 * Created by Alok Omkar on 2017-12-16.
 */
class SongsListFragment : BaseFragment(), SongsView, TextWatcher {


    private lateinit var mSongsPresenter : SongsPresenter
    private lateinit var mDownloadPresenter : DownloadsPresenter
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
        mDownloadPresenter = DownloadsPresenter( this )
        mSongsPresenter.getSongs()
        searchEditText.addTextChangedListener(this)
        searchEditText.setOnTouchListener { _, event ->
            if( event!!.action == MotionEvent.ACTION_UP ) {
                searchEditText.requestFocus()
            }
            true
        }
        profileFAB.setOnClickListener {
            showProfileDialog()
        }
    }

    private fun showProfileDialog() {
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.layout_profile, null)
        val cancelTextView: TextView = dialogView.findViewById(R.id.cancelTextView)
        val profileDialog: AlertDialog = AlertDialog.Builder(context!!)
                .setView(dialogView)
                .create()

        profileDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        profileDialog.show()

        cancelTextView.setOnClickListener { profileDialog.dismiss() }
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
        if( mSongsAdapter!!.itemCount == 0 ) emptyTextView.show()
        else emptyTextView.hide()
    }

    private var mSongIndex : Int = -1
    private var mAction : String = ""

    private val PERMISSIONS_REQUEST: Int = 21

    override fun onSongSelect(songIndex: Int, action : String ) {
        when( action ) {
            "play" -> {
                if( MashUpApplication.instance.isNetworkAvailable() )
                    mNavigationListener.loadPlayerFragment(mSongsList!![songIndex], mSongsList!!)
                else
                    Toast.makeText(context, R.string.interent_required, Toast.LENGTH_SHORT).show()
            }
            "download" -> {
                mSongIndex = songIndex
                mAction = action
                val permissionList = arrayListOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (!handleMultiplePermission(context!!, permissionList)) {
                    requestPermissions( permissionList.toTypedArray(), PERMISSIONS_REQUEST)
                }
                else {

                    if( MashUpApplication.instance.isNetworkAvailable() ) {
                        mSongsAdapter!!.showProgress(songIndex)
                        DownloadFileTask(context!!, this).execute(mSongsAdapter!!.getItemAtPosition(songIndex))
                        //mDownloadPresenter.downloadFile(mSongsAdapter!!.getItemAtPosition(songIndex).url, mSongsAdapter!!.getItemAtPosition(songIndex))

                    }
                    else
                        Toast.makeText(context, R.string.interent_required, Toast.LENGTH_SHORT).show()

                    mSongIndex = -1
                    mAction = ""
                }


            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if( requestCode == PERMISSIONS_REQUEST ) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSongSelect(mSongIndex, mAction)
            } else if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(context, R.string.some_permissions_denied, Toast.LENGTH_SHORT).show()
            }
        }

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
        mDownloadPresenter.onDestroy()
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

    override fun onDownloadSuccess(song: Songs, downloadedFile: File) {
        Toast.makeText(context, "Download complete : " + downloadedFile.absolutePath, Toast.LENGTH_SHORT).show()
        MashUpApplication.getPreferences().addDownloadedFile( song, downloadedFile )
    }

    override fun showDownloadProgress(song: Songs) {
        mSongsAdapter!!.showProgress(song)
    }

    override fun hideDownloadProgress(song: Songs) {
        mSongsAdapter!!.hideProgress(song)
    }
}