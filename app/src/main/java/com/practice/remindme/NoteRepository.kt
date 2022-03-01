package com.practice.remindme

import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao : Dao) {

    val allNotes: LiveData<List<NotesData>> = noteDao.getAllNotes()

    suspend fun insert(notesData: NotesData) {
        noteDao.insert(notesData)
    }
    suspend fun delete(notesData: NotesData) {
        noteDao.delete(notesData)
    }
}