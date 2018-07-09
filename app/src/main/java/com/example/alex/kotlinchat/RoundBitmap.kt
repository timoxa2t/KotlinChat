package com.example.alex.kotlinchat

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import java.io.ByteArrayOutputStream

class RoundBitmap {
    fun create(context: Context, view: ImageView, bitmap: Bitmap?){
        val bs = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bs)
        Glide.with(context)
                .asBitmap()
                .load(bs.toByteArray())
                .apply(RequestOptions().centerCrop())
                .into(object : BitmapImageViewTarget(view) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource)
                        circularBitmapDrawable.isCircular = true
                        view.setImageDrawable(circularBitmapDrawable)
                    }
                })
    }
}