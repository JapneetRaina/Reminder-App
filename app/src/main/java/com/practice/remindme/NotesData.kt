package com.practice.remindme

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class NotesData (@ColumnInfo(name = "text")val text : String,
                 @ColumnInfo(name = "time")val time : String) {
    @PrimaryKey(autoGenerate = true) var id = 0
}