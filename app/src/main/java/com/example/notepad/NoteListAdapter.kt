package com.example.notepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter(var notes : List<Note>, var itemClickListener: View.OnClickListener) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    class ViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem){
        val cardView = viewItem.findViewById<CardView>(R.id.card_view)
        val title = viewItem.findViewById<TextView>(R.id.title)
        val excerpt = viewItem.findViewById<TextView>(R.id.excerpt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /* Display */
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.note_item_layout, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* Data */
        var note = notes[position]
        holder.cardView.setOnClickListener(itemClickListener)
        holder.cardView.tag = position
        holder.title.text = note.title
        holder.excerpt.text = note.text
    }

    override fun getItemCount(): Int {
        /* Size */
        return notes.size
    }
}