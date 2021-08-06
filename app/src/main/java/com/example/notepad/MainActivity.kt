package com.example.notepad

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.utils.deleteNote
import com.example.notepad.utils.loadNotes
import com.example.notepad.utils.persistNote
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var adapter : NoteListAdapter
    lateinit var notes : MutableList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* ToolBar */
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        /* Buttons */
        var floatingActionButton = findViewById<FloatingActionButton>(R.id.add_btn)
        floatingActionButton.setOnClickListener(this)

        /* Notes List */
        notes = loadNotes(this)

        /* RecyclerView */
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = NoteListAdapter(notes, this)
        //!! Ne pas oublier le LayoutManager et le paramétrage de l'adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onClick(view: View?) {
        if (view?.tag != null){
            displayNote(view?.tag as Int)
        }else{
            when(view?.id){
                R.id.add_btn -> {
                    displayNote(-1)
                }
            }
        }
    }

    private fun displayNote(position : Int){
        var intent = Intent(this, NoteDetailActivity::class.java)
        if(position >= 0){
            intent.putExtra(Constants.NOTE_KEY, notes[position] as Parcelable)
            intent.putExtra(Constants.NOTE_INDEX_KEY, position)
        }else{
            var note = Note("", "", "")
            intent.putExtra(Constants.NOTE_KEY, note as Parcelable)
            intent.putExtra(Constants.NOTE_INDEX_KEY, position)
        }
        startActivityForResult(intent, Constants.REQUEST_EDIT_NOTE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("SaveNote", "Triggered")
        if (resultCode != Activity.RESULT_OK || data == null){
            return
        }

        when(requestCode){
            Constants.REQUEST_EDIT_NOTE -> {
                if (data.hasCategory(Constants.DELETE_NOTE)){
                    println("ProcessDelete")
                    processDeleteNoteData(data)
                }else{
                    println("ProcessEdit")
                    processEditNoteData(data)
                }
            }
        }
    }

    private fun processEditNoteData(data: Intent){
        val noteIndex = data.getIntExtra(Constants.NOTE_INDEX_KEY, -1)
        val note = data.getParcelableExtra<Note>(Constants.NOTE_KEY)

        // Crée la note en fichier
        persistNote(this, note!!)

        if (noteIndex >= 0){
            notes[noteIndex] = note!!
        }else{
            notes.add(0, note!!)
        }

        adapter.notifyDataSetChanged()
    }

    private fun processDeleteNoteData(data:Intent){
        val noteIndex = data.getIntExtra(Constants.NOTE_INDEX_KEY, -1)
        val note = data.getParcelableExtra<Note>(Constants.NOTE_KEY)
        notes.removeAt(noteIndex)

        // Supprime la note en fichier
        deleteNote(this, note!!)

        adapter.notifyDataSetChanged()
    }
}