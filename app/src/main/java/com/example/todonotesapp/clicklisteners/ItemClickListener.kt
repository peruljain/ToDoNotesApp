package com.example.todonotesapp.clicklisteners

import com.example.todonotesapp.db.Notes

interface ItemClickListener {
    fun onClick(notes : Notes)
    fun onUpdate(notes: Notes)
}