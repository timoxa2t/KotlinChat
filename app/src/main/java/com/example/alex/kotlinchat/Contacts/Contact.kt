package com.example.alex.kotlinchat.Contacts

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class Contact(val contactId:String, var name:String, var message: String, var time: Long) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var icon :Bitmap? = null

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong()) {
    }


    constructor():this("","",
            "", 0)

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}