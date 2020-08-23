package com.example.todonotesapp.db

import androidx.room.*

@Dao
 interface NotesDao {
    @Query(value = "SELECT * FROM notesData")
    fun getAll():List<com.example.todonotesapp.db.Notes>
    @Insert
    fun insert(notes : com.example.todonotesapp.db.Notes)
    @Update
    fun updateNotes(notes : com.example.todonotesapp.db.Notes)
    @Delete
    fun delete (notes: com.example.todonotesapp.db.Notes)

    @Query(value = "DELETE FROM notesData WHERE isTaskCompleted = :status ")
    fun deleteNotes(status : Boolean)
 }