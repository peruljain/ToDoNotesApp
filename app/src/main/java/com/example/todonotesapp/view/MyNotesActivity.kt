package com.example.todonotesapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.todonotesapp.NotesApp
import com.example.todonotesapp.utils.AppConstants
import com.example.todonotesapp.utils.PreferenceConstants
import com.example.todonotesapp.R
import com.example.todonotesapp.adapter.NotesAdapter
import com.example.todonotesapp.clicklisteners.ItemClickListener
import com.example.todonotesapp.db.Notes
import com.example.todonotesapp.workmanager.MyWorker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyNotesActivity : AppCompatActivity() {
    private  var fullName: String? = null
    private lateinit var fabAddNotes: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerViewNotes: RecyclerView
    private val notesList = ArrayList<Notes>()
    private val TAG = "MyNotes"
    private val ADD_NOTES_CODE = 6
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        bindViews()
        setSharedPreference()
        setNotesTitle()
        getDataFromDatabase()
        setUpRecyclerView()
        clickListners()
        setUpWorkManager()
    }

    private fun setUpWorkManager() {
        val constraint = androidx.work.Constraints.Builder()
                .build()
        val request = PeriodicWorkRequest
                .Builder(MyWorker::class.java, 1, TimeUnit.MINUTES)
                .setConstraints(constraint)
                .build()
        WorkManager.getInstance().enqueue(request)
    }

    private fun clickListners() {
        fabAddNotes.setOnClickListener {
            //setupDialog()
            val intent = Intent(this, AddNotes::class.java)
            startActivityForResult(intent, ADD_NOTES_CODE);
        }
    }

    private fun getDataFromDatabase() {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        val listofNotes = notesDao.getAll()
        notesList.addAll(listofNotes)
    }

    private fun bindViews() {
        fabAddNotes = findViewById(R.id.addNotes)
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
    }

    private fun setNotesTitle() {
        val intent = intent
        if(intent.hasExtra(AppConstants.FULL_NAME)) {
            fullName = intent.getStringExtra(AppConstants.FULL_NAME)
        }
        if (TextUtils.isEmpty(fullName)) {
            fullName = sharedPreferences.getString(PreferenceConstants.FULL_NAME, "")!!
        }
        supportActionBar?.title = fullName
    }

    private fun setSharedPreference() {
        sharedPreferences = getSharedPreferences(PreferenceConstants.NAME, Context.MODE_PRIVATE)
    }

    private fun setupDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.add_notes_dialog, null)
        val title = view.findViewById<EditText>(R.id.Title)
        val description = view.findViewById<EditText>(R.id.Description)
        val submit = view.findViewById<Button>(R.id.submitButton)
        val dialog = AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view)
                .create()
        dialog.show()
        submit.setOnClickListener {
            val titles = title.text.toString()
            val Descriptions = description.text.toString()
            if (titles.isNotEmpty() && Descriptions.isNotEmpty()   ) {
                val notes = Notes(title = titles, description = Descriptions)
                notesList.add(notes)
                //addNotetoDb
                addNotesToDb(notes)
                setUpRecyclerView()
                dialog.dismiss()
            } else {
                Toast.makeText(this@MyNotesActivity, "Please enter title and description", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNotesToDb(notes : Notes) {
        //insert notesin Db
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        notesDao.insert(notes)
    }

    private fun setUpRecyclerView() {
        val itemClickListeners: ItemClickListener = object : ItemClickListener {
            override fun onClick(notes: Notes) {
                val intent = Intent(this@MyNotesActivity, DetailActivity::class.java)
                intent.putExtra(AppConstants.TITLE, notes.title)
                intent.putExtra(AppConstants.DESCRIPTION, notes.description)
                startActivity(intent)
            }

            override fun onUpdate(notes: Notes) {
                val notesApp = applicationContext as NotesApp
                val notesDao = notesApp.getNotesDb().notesDao()
                notesDao.updateNotes(notes)
            }
        }
        val notesAdapter = NotesAdapter(notesList, itemClickListeners)
        val linearLayoutManager = LinearLayoutManager(this@MyNotesActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewNotes.layoutManager = linearLayoutManager
        recyclerViewNotes.adapter = notesAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_NOTES_CODE) {
            val title = data?.getStringExtra(AppConstants.TITLE)
            val description = data?.getStringExtra(AppConstants.DESCRIPTION);
            val imagePath = data?.getStringExtra(AppConstants.IMAGE_PATH);
            var notes  = Notes(title = title!!, description = description!!, imagePath = imagePath!!, isTaskCompleted = false)
            addNotesToDb(notes)
            notesList.add(notes)
            recyclerViewNotes.adapter?.notifyItemChanged(notesList.size-1)
        }
    }
}