package com.example.alex.kotlinchat.Fragments


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.alex.kotlinchat.Contacts.Contact
import com.google.firebase.auth.FirebaseAuth


class ChatFragmentAdapter(viewPager: ViewPager, fm: FragmentManager):FragmentPagerAdapter(fm), FragmentsAdapterCallback {

    val viewPager = viewPager

    override fun fragmentAdapterCallback(contact: Contact) {
        (fragmentList[1] as MessagesFragment).contactMessages(contact)
        viewPager.currentItem = 1
    }


    val fragmentList = ArrayList<Fragment>()

   init {
       fragmentList.add(ContactsFragment(this))
       fragmentList.add(MessagesFragment())
       val auth = FirebaseAuth.getInstance()
       val email = auth.currentUser?.email
       if(email != null){
           val key = email.replace(".", "").replace("@", "")
           (fragmentList[0] as ContactsFragment).setUserKey(key)
           (fragmentList[1] as MessagesFragment).setUserKey(key)

       }
   }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}