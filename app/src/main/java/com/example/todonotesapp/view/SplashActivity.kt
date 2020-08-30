package com.example.todonotesapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todonotesapp.utils.PreferenceConstants
import com.example.todonotesapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class SplashActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setSharedPreference()
        getFcmToken()
        checkLoginStatus()
    }

    private fun getFcmToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    // Log and toast
                })

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