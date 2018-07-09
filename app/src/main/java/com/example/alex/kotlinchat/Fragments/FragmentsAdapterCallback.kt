package com.example.alex.kotlinchat.Fragments

import com.example.alex.kotlinchat.Contacts.Contact
import com.example.alex.kotlinchat.Fragments.MessagesFragment

interface FragmentsAdapterCallback {
    fun fragmentAdapterCallback(contact: Contact)
}