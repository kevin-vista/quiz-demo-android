package com.example.quizdemo.util

import android.content.Context
import com.example.quizdemo.R
import com.example.quizdemo.entity.AnsweredQuiz
import com.example.quizdemo.entity.SignInRequest
import com.example.quizdemo.entity.SignInResult
import com.example.quizdemo.service.UserService
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException

private const val LEGAL_USERNAME = "^[A-Za-z0-9_]{4,32}$"

/**
 * @return Whether a string is legal to be a username
 */
fun String.isLegalUsername(): Boolean = matches(Regex(LEGAL_USERNAME))

fun List<AnsweredQuiz>.isCompleted(): Boolean = all { it.checkedIndex >= 0 }

/**
 * Send sign in request to backend server
 * @return a `SignInResult` instance
 */
suspend fun signIn(context: Context, username: String, password: String): SignInResult {
	val retrofit = Retrofit.Builder()
		.baseUrl(context.getString(R.string.base_url))
		.addConverterFactory(GsonConverterFactory.create())
		.build()
	val userService = retrofit.create<UserService>()
	return try {
		userService.signIn(SignInRequest(username, password)).await()
	} catch (e: IOException) {
		var reason = SignInResult.FAILURE_NETWORK
		val message = e.localizedMessage
		if (message != null) {
			reason += ": $message"
		}
		SignInResult(false, reason)
	}
}
