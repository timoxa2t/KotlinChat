package com.example.alex.kotlinchat.Contacts

import com.example.alex.kotlinchat.Contacts.Contact

interface ContactsCallback {
    fun contactsCallback(callback: ArrayList<Contact>)
    fun contactClickCallback(contact: Contact)
}