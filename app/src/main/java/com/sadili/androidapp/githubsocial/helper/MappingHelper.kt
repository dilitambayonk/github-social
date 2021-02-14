package com.sadili.androidapp.githubsocial.helper

import android.database.Cursor
import com.sadili.androidapp.githubsocial.data.User
import com.sadili.androidapp.githubsocial.db.UserContract
import java.util.ArrayList

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