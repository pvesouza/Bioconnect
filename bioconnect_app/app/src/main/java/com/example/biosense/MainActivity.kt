package com.example.biosense

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity(), Runnable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)

//        val handler:Handler = Handler(this.mainLooper)
//        val myThread:Thread = Thread(this)
//        handler.postDelayed(this, 3000)
    }

    override fun onResume() {
        super.onResume()
        val handler:Handler = Handler(this.mainLooper)
        val myThread:Thread = Thread(this)
        handler.postDelayed(this, 3000)
    }

    override fun run() {
        val intent = Intent(this, List_Bluetooth_Devices::class.java)
        startActivity(intent)
    }
}