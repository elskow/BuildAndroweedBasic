package com.helmyl.basicandroweedsubmission.repository

import com.helmyl.basicandroweedsubmission.model.Post
import com.helmyl.basicandroweedsubmission.model.PostResponse
import com.helmyl.basicandroweedsubmission.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository {
    suspend fun getPosts(): List<Post> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.api.getPosts()
            response.posts
        } catch (e: Exception) {
            throw e
        }
    }
}
