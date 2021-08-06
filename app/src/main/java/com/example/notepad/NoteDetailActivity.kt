package com.example.notepad

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class NoteDetailActivity : AppCompatActivity() {

    lateinit var title : TextView
    lateinit var content : TextView
    lateinit var note : Note
    var noteIndex : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        title = findViewById<TextView>(R.id.title_et)
        content = findViewById<TextView>(R.id.content_et)


        noteIndex = intent.getIntExtra(Constants.NOTE_INDEX_KEY, -1)
        note = intent.getParcelableExtra<Note>(Constants.NOTE_KEY)!!
        title.text = note?.title
        content.text = note?.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_note -> {
                deleteNote(note.title)
                return true
            }
            R.id.save_note -> {
                saveModification()
                return true
            } else -> return false
        }
    }

    private fun saveModification(){
        note.title = title.text.toString()
        note.text = content.text.toString()
        var intent = Intent()
        intent.putExtra(Constants.NOTE_KEY, note as Parcelable)
        intent.putExtra(Constants.NOTE_INDEX_KEY, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun deleteNote(notetitle : String){
        var intent = Intent(this, MainActivity::class.java)
            .addCategory(Constants.DELETE_NOTE)
        //
        val fragment = DeleteNoteDialogFragment(notetitle)
        fragment.listener = object: DeleteNoteDialogFragment.MyDialogListener{

            override fun onPositiveClick() {
                intent.putExtra(Constants.NOTE_KEY, note as Parcelable)
                intent.putExtra(Constants.NOTE_INDEX_KEY, noteIndex)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            override fun onNegativeCLick() {
                Log.i("Delete Note", "Aborted")
            }
        }
        fragment.show(supportFragmentManager, "deleteNoteFragment")
    }
}