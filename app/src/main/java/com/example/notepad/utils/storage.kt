package com.example.notepad.utils

import android.content.Context
import android.text.TextUtils
import com.example.notepad.Note
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

fun persistNote(context: Context, note : Note){

    // Si le filename de la note est vide,
    if (TextUtils.isEmpty(note.filename)){
        // alors on en génère un aléatoirement grâce à UUID.randomUUID()
        note.filename = UUID.randomUUID().toString() + ".note"
    }

    // fileOutput == permet d'ouvrir un fichier avec un nom donné, et le mode d'ouverture
    // ici PRIVATE, donc le fichier ne sera qu'accessible dans l'application
    val fileOutput = context.openFileOutput(note.filename, Context.MODE_PRIVATE)

    // Intermédiaire pour écrire dans un fichier précédemment ouvert (fileOutput)
    val outputStream = ObjectOutputStream(fileOutput)

    // Converti tous les fields d'une Note et les écrit dans le fileOutput
    outputStream.writeObject(note)
    outputStream.close()

}

fun loadNotes(context: Context) : MutableList<Note> {
    // Sera load à la place d'une liste hardcoded dans le MainActivity.kt
    // d'où le : MutableList<Note> en return

    val notes = mutableListOf<Note>()

    val notesDir = context.filesDir
    for (filename in notesDir.list()){
        if(filename.endsWith(".note")){
            val note = loadNote(context, filename)
            notes.add(note)
        }
    }

    return notes
}

fun loadNote(context: Context, filename : String) : Note {
    val fileInput = context.openFileInput(filename)
    val inputStream = ObjectInputStream(fileInput)
    val note = inputStream.readObject() as Note
    inputStream.close()

    return note
}

fun deleteNote(context: Context, note : Note){
    context.deleteFile(note.filename)
}