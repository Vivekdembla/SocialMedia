package com.example.socialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.socialmedia.daos.PostDao

class CreatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        val postInput=findViewById<EditText>(R.id.postInput)
        val postButton=findViewById<Button>(R.id.postButton)
        val postDao=PostDao()
        postButton.setOnClickListener{
            val input=postInput.text.toString().trim()
            if(input.isNotEmpty()) {
                postDao.addPost(input)
                finish()
            }
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

    }
}