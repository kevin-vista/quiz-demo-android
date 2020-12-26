package com.example.quizdemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.example.quizdemo.SignUpActivity.Companion.KEY_REASON
import com.example.quizdemo.util.signIn
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.*

class SignInActivity : BaseActivity() {

	companion object {
		const val KEY_USERNAME = "username"
		const val KEY_PASSWORD = "password"

		const val REQUEST_CODE_SIGN_IN = 46
	}

	private val job = Job()

	private lateinit var sharedPref: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_in)

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
				val signInResult = signIn(this@SignInActivity, username, password)
				withContext(Dispatchers.Main) {
					if (signInResult.successful) {
						saveUserData()
						Toast.makeText(this@SignInActivity,
							signInResult.reason,
							Toast.LENGTH_LONG
						).show()
						startActivity(Intent(this@SignInActivity, CollectionsActivity::class.java))
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

		sharedPref = getSharedPreferences(
			getString(R.string.key_user_preference),
			Context.MODE_PRIVATE
		)
		if (getString(R.string.key_username) in sharedPref) {
			val username = sharedPref.getString(getString(R.string.key_username), "")
			editTextUsername.setText(username)
		}
		if (getString(R.string.key_password) in sharedPref) {
			val password = sharedPref.getString(getString(R.string.key_password), "")
			editTextPassword.setText(password)
		}
		if (editTextUsername.text.toString().isNotBlank() &&
			editTextPassword.text.toString().isNotBlank()) {
			buttonSignIn.callOnClick()
		}

		// Recover fields
		if (savedInstanceState?.containsKey(KEY_USERNAME) == true) {
			editTextUsername.setText(savedInstanceState[KEY_USERNAME] as String)
		}
		if (savedInstanceState?.containsKey(KEY_PASSWORD) == true) {
			editTextPassword.setText(savedInstanceState[KEY_PASSWORD] as String)
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
			}  // REQUEST_CODE_SIGN_IN
		}  // when
	}  // onActivityResult

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

	private fun saveUserData() {
		val username = editTextUsername.text.toString()
		val password = editTextPassword.text.toString()
		with (sharedPref.edit()) {
			putString(getString(R.string.key_username), username)
			putString(getString(R.string.key_password), password)
			apply()
		}
	}

}