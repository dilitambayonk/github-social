package com.sadili.androidapp.clientapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    var name: String? = null,
    var photo: String? = null
) : Parcelable