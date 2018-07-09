package com.example.alex.kotlinchat.Messages

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.alex.kotlinchat.R
import java.util.*
import kotlin.collections.ArrayList

class MessagesAdapter(callback: MessageCallback, userId: String, list: ArrayList<Message>): RecyclerView.Adapter<MessagesAdapter.MessageViewHollder>() {

    var messageList = list
    val mUserId = userId
    val mCalllback = callback
    var contactId: String = ""
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHollder{
        var messageLayout = 0
        when(viewType){
            0 -> messageLayout = R.layout.contact_message_item
            1 -> messageLayout = R.layout.user_message_item
        }
        val view = LayoutInflater.from(parent.context).inflate(messageLayout, parent, false)
        return MessageViewHollder(view)
    }

    override fun getItemViewType(position: Int): Int {
        if(messageList[position].fromContact.equals(mUserId)) return 1
        else return 0
    }

    override fun getItemCount(): Int {
        return messageList.size
        Log.d("taga", "getItemCount: ${messageList.size}")
    }

    fun addMessage(text: String, sender: String){
        val message = Message(messageList.size, text, Date().time, sender)
        messageList.add(message)
        notifyItemInserted(messageList.size - 1)
        Log.d("taga", "Add message to list : $message, and notified")
        mCalllback.messageCallback(message)
        Log.d("taga", "message added to firebase: $message")
    }

    override fun onBindViewHolder(holder: MessageViewHollder, position: Int) {
        holder.setData(messageList[position])
        Log.d("taga", "message is binded to view holder: ${messageList[position]}")
    }

    fun loadList(messagesList: ArrayList<Message>) {
        messageList.clear()
        messageList = messagesList
        notifyDataSetChanged()
    }


    inner class MessageViewHollder(view: View): RecyclerView.ViewHolder(view) {

        val messageText = view.findViewById<TextView>(R.id.message_view)
        fun setData(message: Message){
            messageText.text = message.message
        }
    }

}