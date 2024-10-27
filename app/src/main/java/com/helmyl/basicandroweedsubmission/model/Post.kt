package com.helmyl.basicandroweedsubmission.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    val tags: List<String>,
    val reactions: Reactions,
    val views: Int
) : Parcelable {
    fun getImageUrl() = "https://picsum.photos/seed/$id/400/200"
}

data class PostResponse(
    val posts: List<Post>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
