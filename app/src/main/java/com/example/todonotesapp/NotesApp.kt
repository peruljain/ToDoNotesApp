package com.example.todonotesapp

import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.example.todonotesapp.db.NotesDatabase

class NotesApp : Application() {
    override fun onCreate() {
        AndroidNetworking.initialize(applicationContext);
        super.onCreate()
    }
    fun getNotesDb(): NotesDatabase{
        return NotesDatabase.getInstance(this)
    }
}