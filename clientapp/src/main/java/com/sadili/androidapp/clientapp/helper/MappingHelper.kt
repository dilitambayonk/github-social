package com.sadili.androidapp.clientapp.helper

import android.database.Cursor
import com.sadili.androidapp.clientapp.data.User
import com.sadili.androidapp.clientapp.db.UserContract
import java.util.*

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        cursor?.apply {
            while (moveToNext()) {
                val userItems = User()
                userItems.name = getString(getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
                userItems.photo = getString(getColumnIndexOrThrow(UserContract.UserColumns.PHOTO))
                userList.add(userItems)
            }
        }
        return userList
    }
}