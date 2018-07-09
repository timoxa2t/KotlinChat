package com.example.alex.kotlinchat.Fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alex.kotlinchat.*
import com.example.alex.kotlinchat.Contacts.*


@SuppressLint("ValidFragment")
class ContactsFragment(fragmentcCallback: FragmentsAdapterCallback): Fragment(), ContactsCallback {


    val callback = fragmentcCallback
    val TAG = "myTag"
    var contactList = ArrayList<Contact>()
    lateinit var myAdapter: ContactAdapter
    lateinit var recView: RecyclerView
    lateinit var dataFromStorage: DataFromStorage
    lateinit var dataFromFirebase: ContactsFromFirebase
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.contact_fragment_layout, container, false)
        recyclerViewInit(view)
        return view
    }

    fun setUserKey(key: String){
        userId = key
    }

    private fun loadData() {
        val getActivity = activity
        if(getActivity != null) {
            dataFromStorage = DataFromStorage(getActivity.baseContext)
            contactList = dataFromStorage.load()
        }
            Log.d(TAG, "loadingData from storage list size = ${contactList.size}")
            dataFromFirebase = ContactsFromFirebase(this, userId)
            dataFromFirebase.load()
            Log.d(TAG, "loadingData from firebase list size = ${contactList.size}")

    }


    fun recyclerViewInit(view: View){
        Log.d(TAG, "initializing RecyclerView")
        recView = view.findViewById(R.id.rec_view)
        myAdapter = ContactAdapter(context!!, this, contactList)
        val linLayoutManager = LinearLayoutManager(activity?.baseContext)
        recView.layoutManager = linLayoutManager
        recView.adapter = myAdapter
    }

    override fun onPause() {
        super.onPause()
        dataFromStorage.save(contactList)
        dataFromFirebase.save(contactList)
    }
    override fun contactsCallback(callback: ArrayList<Contact>) {
        if(callback.isEmpty()) return
        contactList.clear()
        for(contact in callback){
            contactList.add(contact)
        }
        myAdapter.notifyDataSetChanged()
    }
    override fun contactClickCallback(contact: Contact) {
        callback.fragmentAdapterCallback(contact)
    }

}