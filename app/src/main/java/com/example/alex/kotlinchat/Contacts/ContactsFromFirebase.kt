package com.example.alex.kotlinchat.Contacts

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.example.alex.kotlinchat.Fragments.ContactsFragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

class ContactsFromFirebase(fragmentActivity: ContactsFragment, userId: String) : AsyncTask<ArrayList<Contact>, Int, ArrayList<Contact>>() {

    var list = ArrayList<Contact>()
    val userId = userId
    override fun doInBackground(vararg params: ArrayList<Contact>): ArrayList<Contact> {
        load()
        Log.d(TAG, "doInBackground")
        return list
    }

    override fun onPostExecute(result: ArrayList<Contact>) {
        super.onPostExecute(result)
        Log.d(TAG, "onPostExecute")
        callback.contactsCallback(result)
    }


    val callback = fragmentActivity
    val TAG = "myTagDataFromFirebase"

    fun load(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("contacts")

            myRef.addValueEventListener(object: ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
             }

                override fun onDataChange(p0: DataSnapshot) {
                if(p0 == null) return
                if(p0.hasChildren()){
                    getData(p0)
                }
            }
        })
    }

    fun getData(data: DataSnapshot){

            val iterator = data.children.iterator()
            Log.d(TAG, "iterator.hasNext() = ${iterator.hasNext()}")
            do {
                val item = iterator.next()
                Log.d(TAG, "item = ${item.key}")
                val firebaseContact = item.getValue(Contact::class.java)
                Log.d(TAG, firebaseContact.toString())
                val id = firebaseContact?.contactId


                if (firebaseContact != null && !id.equals(userId)) {
                    Log.d(TAG, "userid = $userId, id = $id")
                    list.add(firebaseContact)
                    imageLoader(firebaseContact)

                    Log.d(TAG, "Downloading contact from firebase, name = ${firebaseContact.name}")
                }
            } while (iterator.hasNext())
        Log.d(TAG, "last item added")
        callback.contactsCallback(list)
    }


    private fun imageLoader(contact: Contact): Bitmap? {
        val iconId = contact.contactId
        val  mStorageRef: StorageReference
        var bitmap: Bitmap? = null
        mStorageRef = FirebaseStorage.getInstance().getReference("contactImages/").child("small$iconId.jpg");
        Log.d(TAG, "Full storage reference: ${mStorageRef.toString()}")
        mStorageRef.getBytes(1024*1024).addOnSuccessListener(object: OnSuccessListener<ByteArray> {
            override fun onSuccess(p0: ByteArray?) {
                if(p0 == null) return
                bitmap = BitmapFactory.decodeByteArray(p0, 0, p0.size)
                contact.icon = bitmap
                callback.contactsCallback(list)
            }
        }).addOnFailureListener(object:OnFailureListener{
            override fun onFailure(p0: Exception) {
                Log.d(TAG, "Failed to load image. Exception: ${p0.toString()}")
            }
        })
        return bitmap
    }


    //TODO change function to write only personal data and contacts that are added as friends
    fun save(contactList: ArrayList<Contact>){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("contacts")
        for(contact in contactList){
            contact.icon = null
            myRef.child(contact.contactId).setValue(contact)

            //temp image saving
            val bitmap = contact.icon
            if(bitmap != null) {
                val mStorageRef: StorageReference
                mStorageRef = FirebaseStorage.getInstance().getReference().child("contactImages/${contact.contactId}.jpg");
                val arrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream)
                val array = arrayOutputStream.toByteArray()
                mStorageRef.putBytes(array)
            }
        }
    }
}