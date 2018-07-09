package com.example.alex.kotlinchat.Contacts

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.example.alex.kotlinchat.Contacts.Contact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream

class DataFromStorage(context: Context) {

    val TAG = "myTagStorageData"


    val context = context

    fun save(contactsList: ArrayList<Contact>){
        for(item in contactsList){
            item.icon = null
        }
        Log.d(TAG, "Saving contacts in storage. List size = ${contactsList.size}")
        val sharedPreferences = context.getSharedPreferences("contacts", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val contactString = gson.toJson(contactsList)
        editor.putString("contactString", contactString)
        editor.apply()
    }

    private fun saveIcon(iconId: String, icon: Bitmap) {

        val path = Environment.getExternalStorageDirectory().toString()
        val file = File(path, "$iconId.jpg")
        val fileOut = FileOutputStream(file)

        icon.compress(Bitmap.CompressFormat.JPEG, 100, fileOut)
        fileOut.flush()
        fileOut.close()
    }

    fun load(): ArrayList<Contact>{
        val sharedPreferences = context.getSharedPreferences("contacts", Context.MODE_PRIVATE)
        if(sharedPreferences == null) return ArrayList<Contact>()
        val gson = Gson()
        val contactString = sharedPreferences.getString("contactString", "")
        if(contactString == "") return ArrayList<Contact>()
        val type = object :TypeToken<ArrayList<Contact>>(){}.type
        val contactsList = gson.fromJson(contactString, type) as ArrayList<Contact>
        Log.d(TAG, "Loading contacts from storage. List size = ${contactsList.size}")

        return contactsList
    }
}