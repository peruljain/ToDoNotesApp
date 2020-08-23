package com.example.todonotesapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todonotesapp.utils.PreferenceConstants
import com.example.todonotesapp.R

class SplashActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setSharedPreference()
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val isLoggenIn = sharedPreferences.getBoolean(PreferenceConstants.IS_LOGGED_IN, false)
        if (isLoggenIn) {
            val intent = Intent(this, MyNotesActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setSharedPreference() {
        sharedPreferences = getSharedPreferences(PreferenceConstants.NAME, Context.MODE_PRIVATE)
    }
}