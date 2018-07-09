package com.example.alex.kotlinchat.Messages

interface MessageCallback {
    fun messageCallback(message: Message)
    fun firebaseCallback(messagesList: ArrayList<Message>)
    fun firebaseMessageCallback(message: Message)
}