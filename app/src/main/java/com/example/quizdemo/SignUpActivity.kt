package com.example.quizdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.quizdemo.SignInActivity.Companion.KEY_USERNAME
import com.example.quizdemo.entity.SignUpRequest
import com.example.quizdemo.entity.SignUpResult
import com.example.quizdemo.service.UserService
import com.example.quizdemo.util.isLegalUsername
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException

class SignUpActivity : BaseActivity() {

	companion object {
		// For debug use only
		val DEBUG_RESULT_SUCCESS = SignUpResult(
			true,
			SignUpResult.SUCCESS
		)
		val DEBUG_RESULT_DUPLICATE_USERNAME = SignUpResult(
			false,
			SignUpResult.FAILURE_DUPLICATE_USERNAME
		)

		const val TAG = "SignUpActivity"
		const val KEY_REASON = "result"
	}

	private val job = Job()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_up)

		// Toolbar Configurations
		setSupportActionBar(findViewById(R.id.toolbarSignUp))
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)

		// Fill username if possible
		val intent = intent
		if (intent.hasExtra(KEY_USERNAME)) {
			editTextUsername.setText(intent.getStringExtra(KEY_USERNAME))
		}

		editTextUsername.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) {
				val username = editTextUsername.text.toString()
				if (!username.isLegalUsername()) {
					editTextUsername.error = getString(R.string.error_illegal_username)
				}
			}
		}
		editTextPassword2.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) {
				checkPasswordConsistency()
			}
		}
		buttonSignUp.setOnClickListener {
			val username = editTextUsername.text.toString()
			val password = editTextPassword.text.toString()
			val password2 = editTextPassword2.text.toString()

			if (username.isBlank()) {
				editTextUsername.error = getString(R.string.prompt_enter_username)
				return@setOnClickListener
			}
			if (password.isBlank()) {
				editTextPassword.error = getString(R.string.prompt_enter_password)
				return@setOnClickListener
			}
			if (password2.isBlank()) {
				editTextPassword2.error = getString(R.string.prompt_enter_password2)
				return@setOnClickListener
			}
			if (!checkPasswordConsistency()) {
				return@setOnClickListener
			}

			// Sign up
			CoroutineScope(job).launch {
				val signUpResult = signUp(username, password)
				withContext(Dispatchers.Main) {
					Log.i(TAG, signUpResult.toString())
					if (signUpResult.successful) {
						val backIntent = Intent()
						backIntent.putExtra(KEY_USERNAME, username)
						backIntent.putExtra(KEY_REASON, signUpResult.reason)
						setResult(RESULT_OK, backIntent)
						Log.i(TAG, "finished")
						finish()
					} else {
						Toast.makeText(
							this@SignUpActivity,
							signUpResult.reason,
							Toast.LENGTH_LONG
						).show()
					}
				}
			}  // CoroutineScope
		}  // buttonSignUp.onClickListener
	}  // onCreate

	/**
	 * Send sign up request to backend server
	 * @return a `SignUpResult` instance
	 */
	private suspend fun signUp(username: String, password: String): SignUpResult {
		val retrofit = Retrofit.Builder()
			.baseUrl(getString(R.string.base_url))
			.addConverterFactory(GsonConverterFactory.create())
			.build()
		val userService = retrofit.create<UserService>()
		return try {
			userService.signUp(SignUpRequest(username, password)).await()
		} catch (e: IOException) {
			var reason = SignUpResult.FAILURE_NETWORK
			val message = e.localizedMessage
			if (message != null) {
				reason += ": $message"
			}
			SignUpResult(false, reason)
		}
	}

	private fun checkPasswordConsistency(): Boolean {
		val password = editTextPassword.text.toString()
		val password2 = editTextPassword2.text.toString()
		if (password != password2) {
			editTextPassword2.error = getString(R.string.error_password_inconsistent)
			return false
		}
		return true
	}
}