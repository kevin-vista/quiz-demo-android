package com.example.quizdemo.service

import com.example.quizdemo.entity.SignInRequest
import com.example.quizdemo.entity.SignInResult
import com.example.quizdemo.entity.SignUpRequest
import com.example.quizdemo.entity.SignUpResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

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