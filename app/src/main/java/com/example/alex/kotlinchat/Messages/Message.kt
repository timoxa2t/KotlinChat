package com.example.alex.kotlinchat.Messages

data class Message(val id: Int, val message: String, val time: Long, val fromContact: String) {

    constructor():this(0,"", 0, "")
}