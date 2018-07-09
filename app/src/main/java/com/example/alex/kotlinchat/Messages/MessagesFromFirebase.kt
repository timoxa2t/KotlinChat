package com.example.alex.kotlinchat.Messages

import android.util.Log
import com.google.firebase.database.*

class MessagesFromFirebase(userId: String, callback: MessageCallback) {

    val mCallback = callback
    val myRef = FirebaseDatabase.getInstance().getReference("messages")
    val mUserId = userId

    fun load(contactId: String){
        val messagesRef = myRef.child(getRef(contactId))
        messagesRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(!(p0 != null && p0.hasChildren())) return
                val messagesList = ArrayList<Message>()
                val iterator = p0.children.iterator()
                do{
                    val message = iterator.next().getValue(Message::class.java)
                    if(message != null){
                        Log.d("taga", "Message ${message.id} from firebase succesfuly loaded")
                        messagesList.add(message)
                    }
                }while(iterator.hasNext())
                mCallback.firebaseCallback(messagesList)
            }
        })
        messagesRef.orderByKey().addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
             }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java)
                if(p0.getValue(Message::class.java) is Message && message != null){
                    if(message.fromContact.equals(mUserId))return
                    else{
                        mCallback.firebaseMessageCallback(message)
                    }
                }
             }

        })

    }
    fun save(message: Message, contactId: String){
        val messagesRef = myRef.child(getRef(contactId)).child(message.id.toString())
        messagesRef.setValue(message)
    }

    fun getRef(contactId: String): String{
        // Less goes first
        if(contactId.compareTo(mUserId) > 0){
            return mUserId + contactId
        }else{
            return contactId + mUserId
        }
    }
}