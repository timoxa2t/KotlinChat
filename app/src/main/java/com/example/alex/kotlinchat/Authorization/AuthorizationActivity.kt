package com.example.alex.kotlinchat.Authorization

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.alex.kotlinchat.MainActivity
import com.example.alex.kotlinchat.R
import com.example.alex.kotlinchat.R.string.email
import com.example.alex.kotlinchat.R.string.password
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authorization.*
import java.lang.Exception


class AuthorizationActivity : AppCompatActivity() {

    val REGISTERED_USER_REQUEST = 1
    val TAG = "myTagAuth"

    lateinit var editEmail: EditText
    lateinit var editPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        editEmail = enter_email
        editPassword = enter_password
        getSavedUserData()
    }

    private fun getSavedUserData() {
        val sharedPref = getSharedPreferences("User Data", Context.MODE_PRIVATE)
        editEmail.setText(sharedPref.getString("email", ""))
        editPassword.setText(sharedPref.getString("password", ""))
    }

    fun signIn(view: View){
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
            return
        }
        checkUserInFirebase(email, password)
    }

    fun signUp(view: View){
        startActivityForResult(Intent(this, RegisterActivity::class.java), REGISTERED_USER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = data?.getSerializableExtra("result")
        if(result != null && result is Pair<Any?, Any?>){
            val resultPair = result as Pair<String, String>
            val email = resultPair.first
            val password = resultPair.second
            editEmail.setText(email)
            editPassword.setText(password)
            checkUserInFirebase(email, password)
        }
    }

    private fun checkUserInFirebase(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(object : OnSuccessListener<AuthResult>{
            override fun onSuccess(p0: AuthResult?) {
                val mail = p0?.user?.email
                if(mail != null) {
                    saveUserLogInData(email, password)
                    loadProfile(mail)
                }
            }
        }).addOnFailureListener(object : OnFailureListener{
            override fun onFailure(p0: Exception) {
                Log.d(TAG, p0.toString())
            }

        })
    }

    private fun saveUserLogInData(email: String, password: String) {
        val sharedPref = getSharedPreferences("User Data", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.commit()
    }

    private fun loadProfile(email:String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("key", email)
        startActivity(intent)
    }


}
