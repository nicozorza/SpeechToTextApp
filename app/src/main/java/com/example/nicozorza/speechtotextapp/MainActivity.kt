package com.example.nicozorza.speechtotextapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, AudioRecord::class.java)
            startActivity(intent)
        }, 1000)
    }

}
