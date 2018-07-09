package com.example.alex.kotlinchat

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.example.alex.kotlinchat.Authorization.RegisterActivity
import com.example.alex.kotlinchat.Fragments.ChatFragmentAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity() {

    lateinit var fragmentAdapter: ChatFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager = view_pager
        fragmentAdapter = ChatFragmentAdapter(viewPager, supportFragmentManager)
        viewPager.adapter = fragmentAdapter
        startService(Intent(this, MyIntentService::class.java))
    }
}


