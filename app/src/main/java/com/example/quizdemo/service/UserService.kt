package com.example.quizdemo.service

import com.example.quizdemo.entity.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

	@POST("user/sign_up")
	fun signUp(
		@Body signInRequest: SignUpRequest
	): Call<SignUpResult>

	@POST("user/sign_in")
	fun signIn(
		@Body signUpRequest: SignInRequest
	): Call<SignInResult>

}