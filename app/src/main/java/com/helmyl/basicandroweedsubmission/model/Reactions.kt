package com.helmyl.basicandroweedsubmission.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reactions(
    val likes: Int,
    val dislikes: Int
) : Parcelable
