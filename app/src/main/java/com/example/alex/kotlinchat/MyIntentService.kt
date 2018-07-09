package com.example.alex.kotlinchat

import android.app.IntentService
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.google.firebase.database.*
import junit.runner.Version.id
import java.io.File


class MyIntentService : IntentService("MyIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0 == null) return
                val message = p0.value
                if(message is String) {
                    val builder = NotificationCompat.Builder(this@MyIntentService).
                            setContentText(message).
                            setContentTitle("KotlinChat").
                            setSmallIcon(R.mipmap.ic_launcher)
                    val notification = builder.build()
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(1, notification)
                }
            }

        })

    }
}
