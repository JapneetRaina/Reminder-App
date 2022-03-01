package com.practice.remindme

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NotesData)

    @Delete
    suspend fun delete(note: NotesData)

    @Query("Select * from notes_table order by id ASC")
    fun getAllNotes(): LiveData<List<NotesData>>

}