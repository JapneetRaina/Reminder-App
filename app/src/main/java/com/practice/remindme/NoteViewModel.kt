package com.practice.remindme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {

    val allNotes : LiveData<List<NotesData>>
    val repository : NoteRepository
    init {
        val dao = NoteDataBase.getDatabase(application).getNoteDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }
    fun deleteNode(notesData: NotesData) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(notesData)

    }
    fun insertNode(notesData: NotesData) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(notesData)

    }
}