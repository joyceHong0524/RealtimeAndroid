package com.junga.realtime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enter.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username",editText.text.toString())
            startActivity(intent)
        }
    }
}
