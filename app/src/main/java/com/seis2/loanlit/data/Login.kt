package com.seis2.loanlit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LoginDetails")
data class LoginTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int = 0,

    @ColumnInfo(name = "Email")
    val email: String,

    @ColumnInfo(name = "Password")
    val password: String
)

