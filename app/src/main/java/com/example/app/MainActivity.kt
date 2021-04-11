package com.example.app

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.qianlu.corelibrary.VerificationCodeInput

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input: VerificationCodeInput = findViewById(R.id.ver_number2)
        val tvNumber = findViewById<TextView>(R.id.tv_number)
        val tv2 = findViewById<TextView>(R.id.tv_2)

        tvNumber.setOnClickListener { input.insertNumber(tvNumber.text.toString()) }

        tv2.setOnClickListener { input.insertNumber(tv2.text.toString()) }

        input.setOnCompleteListener { content -> Log.d("----", content) }
    }
}