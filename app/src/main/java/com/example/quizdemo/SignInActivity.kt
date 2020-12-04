package com.example.quizdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizdemo.SignUpActivity.Companion.KEY_REASON
import com.example.quizdemo.entity.SignInRequest
import com.example.quizdemo.entity.SignInResult
import com.example.quizdemo.entity.SignUpRequest
import com.example.quizdemo.entity.SignUpResult
import com.example.quizdemo.service.UserService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SignInActivity : AppCompatActivity() {

	companion object {
		// For debug use only
		val DEBUG_RESULT_SUCCESS = SignInResult(
			true,
			SignInResult.SUCCESS
		)
		val DEBUG_RESULT_WRONG_PASSWORD = SignInResult(
			false,
			SignInResult.FAILURE_WRONG_PASSWORD
		)

		const val KEY_USERNAME = "username"
		const val KEY_PASSWORD = "password"

		const val REQUEST_CODE_SIGN_IN = 46
	}

	private val job = Job()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_in)

		// Recover fields
		if (savedInstanceState?.containsKey(KEY_USERNAME) == true) {
			editTextUsername.setText(savedInstanceState[KEY_USERNAME] as String)
		}
		if (savedInstanceState?.containsKey(KEY_PASSWORD) == true) {
			editTextPassword.setText(savedInstanceState[KEY_PASSWORD] as String)
		}

		buttonSignIn.setOnClickListener {
			val username = editTextUsername.text.toString()
			val password = editTextPassword.text.toString()

			// Prompt if there are empty fields
			if (username.isBlank()) {
				editTextUsername.requestFocus()
				editTextUsername.error = getString(R.string.prompt_enter_username)
				return@setOnClickListener
			}
			if (password.isBlank()) {
				editTextPassword.requestFocus()
				editTextPassword.error = getString(R.string.prompt_enter_password)
				return@setOnClickListener
			}

			// Sign in
			CoroutineScope(job).launch {
				val signInResult = signIn(username, password)
				withContext(Dispatchers.Main) {
					if (signInResult.successful) {
						Toast.makeText(this@SignInActivity,
							signInResult.reason,
							Toast.LENGTH_SHORT
						).show()
						startActivity(Intent(this@SignInActivity, MainActivity::class.java))
					} else {
						Toast.makeText(
							this@SignInActivity,
							signInResult.reason,
							Toast.LENGTH_SHORT
						).show()
					}
				}
			}  // CoroutineScope
		}  // buttonSignIn.onClickListener
		textViewSignUp.setOnClickListener {
			val signUpIntent = Intent(this, SignUpActivity::class.java)
			if (editTextUsername.text.toString().isNotBlank()) {
				signUpIntent.putExtra(KEY_USERNAME, editTextUsername.text.toString())
			}
			startActivityForResult(signUpIntent, REQUEST_CODE_SIGN_IN)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			REQUEST_CODE_SIGN_IN -> {
				if (resultCode == RESULT_OK) {
					if (data?.hasExtra(KEY_REASON) == true) {
						Snackbar.make(
							constraintLayoutSignIn,
							data.getStringExtra(KEY_REASON) ?:
							getString(R.string.sign_up_finished_default),
							Snackbar.LENGTH_SHORT
						).show()
					}
					if (data?.hasExtra(KEY_USERNAME) == true) {
						val username = data.getStringExtra(KEY_USERNAME)
						editTextUsername.setText(username)
						editTextUsername.error = null
						editTextPassword.requestFocus()
					}
				}
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)

		// Save filled fields
		if (editTextUsername.text.toString().isNotBlank()) {
			outState.putString(KEY_USERNAME, editTextUsername.text.toString())
		}
		if (editTextPassword.text.toString().isNotBlank()) {
			outState.putString(KEY_PASSWORD, editTextPassword.text.toString())
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		job.cancel(null)
	}

	/**
	 * Send sign in request to backend server
	 * @return a `SignInResult` instance
	 */
	private suspend fun signIn(username: String, password: String): SignInResult {
		val retrofit = Retrofit.Builder()
			.baseUrl(SignUpActivity.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
		val userService = retrofit.create<UserService>()
		return try {
			userService.signIn(SignInRequest(username, password)).await()
		} catch (e: HttpException) {
			SignInResult(false, SignInResult.FAILURE_NETWORK)
		}
	}

}