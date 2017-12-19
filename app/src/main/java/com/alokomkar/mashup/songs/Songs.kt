package com.alokomkar.mashup.songs

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Alok Omkar on 2017-12-16.
 */
class Songs(
        @SerializedName("song")
        @Expose
        var song: String = "",
        @SerializedName("url")
        @Expose
        var url: String = "",
        @SerializedName("artists")
        @Expose
        var artists: String = "",
        @SerializedName("cover_image")
        @Expose
        var coverImage: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    var expandedUrl : String = ""
    var isDownloading : Boolean = false

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(song)
        writeString(url)
        writeString(artists)
        writeString(coverImage)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Songs

        if (song != other.song) return false

        return true
    }

    override fun hashCode(): Int {
        return song.hashCode()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Songs> = object : Parcelable.Creator<Songs> {
            override fun createFromParcel(source: Parcel): Songs = Songs(source)
            override fun newArray(size: Int): Array<Songs?> = arrayOfNulls(size)
        }
    }


}