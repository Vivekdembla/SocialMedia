package com.example.socialmedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.daos.PostDao
import com.example.socialmedia.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postDao:PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signOut=findViewById<Button>(R.id.signOut)
        val fab:View = findViewById(R.id.fab)
        recyclerView=findViewById(R.id.recyclerView)
        fab.setOnClickListener{
            val intent=Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        signOut.setOnClickListener{

            val auth=Firebase.auth
            val uid=auth.currentUser!!.uid
            val db=FirebaseFirestore.getInstance()
            val userCollection=db.collection("users")
            userCollection.document(uid).delete()
            auth.signOut()
            val intent=Intent(this, SignInActivity::class.java)
            startActivity(intent)

        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        postDao=PostDao()
        val postsCollections=postDao.postCollection
        val query=postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions= FirestoreRecyclerOptions.Builder<Post>().setQuery(
            query,
            Post::class.java
        ).build()

        adapter= PostAdapter(recyclerViewOptions, this)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this)
    }


    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }
}