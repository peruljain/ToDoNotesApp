package com.example.todonotesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todonotesapp.R
import com.example.todonotesapp.clicklisteners.ItemClickListener
import com.example.todonotesapp.db.Notes
import kotlinx.android.synthetic.main.notes_adapter_layout.view.*
import java.util.*

 class NotesAdapter(val list: List<Notes>, val itemClickListeners: ItemClickListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_adapter_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notes = list[position]
        holder.textViewTitle.text = notes.title
        holder.textViewDescription.text = notes.description
        holder.checkBoxMarked.isChecked = notes.isTaskCompleted
        holder.itemView.setOnClickListener { itemClickListeners.onClick(notes) }
        holder.itemView.checkboxMarked.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                notes.isTaskCompleted = isChecked
                itemClickListeners.onUpdate(notes)
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val checkBoxMarked : CheckBox = itemView.findViewById(R.id.checkboxMarked)
    }


}