package com.example.alex.kotlinchat.Authorization

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.alex.kotlinchat.Contacts.Contact
import com.example.alex.kotlinchat.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    val IMAGE_REQUEST_CODE = 1
    val TAG = "myTagRegister"

    lateinit var editName:EditText
    lateinit var editEmail:EditText
    lateinit var editPassword:EditText
    lateinit var editConfirmPassword: EditText
    lateinit var imageView: ImageView
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        editEmail = edit_email
        editName = edit_name
        editPassword = edit_password
        editConfirmPassword = confirm_password
        imageView = image_view
    }

    fun selectImage(view: View){
        val galeryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galeryIntent.type = "image/*"
        startActivityForResult(galeryIntent, IMAGE_REQUEST_CODE)
    }

    fun registerUser(view: View){
        if(checkName() && checkEmail() && checkPassword()){
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val name = editName.text.toString()
            val autRef = FirebaseAuth.getInstance()
            autRef.createUserWithEmailAndPassword(email, password).addOnSuccessListener(object : OnSuccessListener<AuthResult>{
                override fun onSuccess(p0: AuthResult?) {
                    Toast.makeText(this@RegisterActivity, "User registration succesful", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "User registration succesful")
                    val key = email.replace(".", "").replace("@", "")
                    addUserToChat(key, name)
                    if(checkImage()) loadImage(key)
                    returnResult(email, password)
                }
            }).addOnFailureListener(object : OnFailureListener{
                override fun onFailure(p0: Exception) {

                    Log.d(TAG, p0.toString())
                    Toast.makeText(this@RegisterActivity, "User Registration failure", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun returnResult(email: String, password: String) {
        val intent = Intent()
        val pair = email to password
        intent.putExtra("result", pair)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun addUserToChat(email: String, name: String) {
        val ref = FirebaseDatabase.getInstance().getReference("contacts")
        ref.child(email).setValue(Contact(email, name, "", Date().time))
    }

    //TODO change allowed password types
    private fun checkPassword(): Boolean {
        if(editPassword.text.length < 6){
            Toast.makeText(this, "Password shoud be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        val password = editPassword.text.toString()
        val confirmPassword = editConfirmPassword.text.toString()
        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Passwords dont match", Toast.LENGTH_SHORT).show()
            return false
        }
        else return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode != IMAGE_REQUEST_CODE) return

        val uri = data?.data
        if(uri == null) return
        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        imageView.setImageBitmap(bitmap)
    }

    private fun loadImage(imageName: String) {
        val outputStream = ByteArrayOutputStream()
        val smallOutputStream = ByteArrayOutputStream()
        if(bitmap == null) return
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val size = bitmap?.byteCount ?: 1
        getResizedBitmap(bitmap).compress(Bitmap.CompressFormat.JPEG, 50, smallOutputStream)
        val byteArray = outputStream.toByteArray()
        val smallByteArray = smallOutputStream.toByteArray()
        val imageRef =  imageName + ".jpg"
        FirebaseStorage.getInstance().getReference().child("contactImages/$imageRef").putBytes(byteArray)
        FirebaseStorage.getInstance().getReference().child("contactImages/small$imageRef").putBytes(smallByteArray)
    }

    fun getResizedBitmap(bm: Bitmap?): Bitmap {
        if(bm == null) return bitmap!!
        val width = bm.width
        val height = bm.height
        var newHeight = 0
        var newWidth = 0
        if(height > width) {
            newWidth = 500
            newHeight = newWidth * height / width
        }else{
            newHeight = 500
            newWidth = newHeight * width / height
        }
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
    }


    private fun checkImage(): Boolean {
        return (bitmap != null)
    }

    private fun checkEmail(): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(editEmail.text)
        val check = matcher.matches()
        if(!check){
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show()
        }
        return matcher.matches()
    }

    private fun checkName(): Boolean {
        if(editName.text.isEmpty()){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


}
