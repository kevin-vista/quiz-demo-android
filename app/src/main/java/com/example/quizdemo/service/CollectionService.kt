package com.example.quizdemo.service

import com.example.quizdemo.entity.Quiz
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CollectionService {

	@POST("collection/all")
	fun getAll(): Call<List<String>>

	@POST("collection/quizzes")
	fun getQuizzes(
		@Body collectionId: String
	): Call<List<Quiz>>

}