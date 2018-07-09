package com.example.alex.kotlinchat.Contacts

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.alex.kotlinchat.R
import kotlinx.android.synthetic.main.contact_item.view.*
import java.util.*
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.graphics.Bitmap
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.alex.kotlinchat.RoundBitmap
import java.io.ByteArrayOutputStream


class ContactAdapter(context: Context, callback: ContactsCallback, list: ArrayList<Contact>) : RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    val TAG = "myTagAdapter"

    val contactsList = list
    val callback = callback
    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(contactsList[position])
        Log.d(TAG, "onBindViewHolder list size = ${contactsList.size}, position = $position")
     }


    inner class MyViewHolder(view:View): RecyclerView.ViewHolder(view) {

        val contactView = view
        val name = contactView.contact_name
        val icon = contactView.contact_image
        val message = contactView.contact_message
        val time = contactView.contact_time
        lateinit var contact: Contact


        fun setData(data: Contact){
            contact = data
            Log.d(TAG, "Set contact data to views. Name = ${data.name}")
            if(data.icon != null){
                RoundBitmap().create(context, icon, data.icon)
            }
            name.text = data.name
            message.text = data.message
            time.text = Date(data.time).toString()
        }

        val listener = contactView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                callback.contactClickCallback(contact)
            }
        })
    }
}