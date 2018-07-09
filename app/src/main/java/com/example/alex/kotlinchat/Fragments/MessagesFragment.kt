package com.example.alex.kotlinchat.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.alex.kotlinchat.Contacts.Contact
import com.example.alex.kotlinchat.Messages.Message
import com.example.alex.kotlinchat.Messages.MessageCallback
import com.example.alex.kotlinchat.Messages.MessagesAdapter
import com.example.alex.kotlinchat.Messages.MessagesFromFirebase
import com.example.alex.kotlinchat.R
import com.example.alex.kotlinchat.RoundBitmap
import kotlin.collections.ArrayList


class MessagesFragment(): Fragment(), MessageCallback {
    override fun firebaseMessageCallback(message: Message) {
        messageAdapter.addMessage(message.message, message.fromContact)
    }

    override fun firebaseCallback(messagesList: ArrayList<Message>) {
        messageAdapter.loadList(messagesList)
    }

    val messagesList = ArrayList<Message>()
    lateinit var recView: RecyclerView
    lateinit var messageAdapter: MessagesAdapter
    lateinit var editMessage: EditText
    lateinit var toContact: Contact
    lateinit var messagesFromFirebase: MessagesFromFirebase
    lateinit var sendMessage: Button
    lateinit var userId: String
    lateinit var contactIconView: ImageView
    lateinit var contactNameView: TextView

    override fun messageCallback(message: Message) {
        messagesFromFirebase.save(message, toContact.contactId)
        recView.scrollToPosition(messageAdapter.itemCount - 1)
        //TODO notify service
    }

    fun setUserKey(key:String){
        userId = key
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.messages_fragment_layout, container, false)
        recView = view.findViewById(R.id.rec_view)

        contactIconView = view.findViewById(R.id.toolbar_image_view)
        contactNameView = view.findViewById(R.id.toolbar_title)

        editMessage = view.findViewById(R.id.edit_message)
        sendMessage = view.findViewById(R.id.send_message)
        sendMessage.setOnClickListener(listener)
        initVals()
        return view
    }

    fun initVals(){
        messageAdapter = MessagesAdapter(this, userId, messagesList)
        val manager = LinearLayoutManager(activity?.baseContext)
        recView.adapter = messageAdapter
        recView.layoutManager = manager
        messagesFromFirebase = MessagesFromFirebase(userId, this)
    }

    fun contactMessages(contact: Contact){
        toContact = contact
        messagesFromFirebase.load(toContact.contactId)
        messageAdapter.contactId = contact.contactId
        contactNameView.text = contact.name
        val context = context
        if(context != null) {
            RoundBitmap().create(context, contactIconView, contact.icon)
        }
    }
    val listener = object: View.OnClickListener{
        override fun onClick(v: View?) {
            val message = editMessage.text.toString()
            editMessage.setText("")
            messageAdapter.addMessage(message, userId)
        }
    }
}