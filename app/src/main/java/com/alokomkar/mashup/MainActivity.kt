package com.alokomkar.mashup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), NavigationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadSongsFragment()
    }

    override fun loadSongsFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.container, SongsListFragment()).commit()
    }

    override fun loadPlayerFragment(selectedSong: Songs, allSongs : ArrayList<Songs>) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_SONG, selectedSong)
        bundle.putParcelableArrayList(BUNDLE_SONGS_LIST, allSongs)
        playerFragment.arguments = bundle
        fragmentTransaction.replace(R.id.container, playerFragment).commit()
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    override fun onBackPressed() {
        if( supportFragmentManager.backStackEntryCount > 1 ) {
            supportFragmentManager.popBackStack()
        }
        else
            finish()
    }
}
