package com.example.quizdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizdemo.SignInActivity.Companion.KEY_USERNAME
import com.example.quizdemo.entity.SignUpResult
import com.example.quizdemo.util.isLegalUsername
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

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

		const val KEY_REASON = "result"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_up)

		// Fill username if possible
		val intent = intent
		if (intent.hasExtra(KEY_USERNAME)) {
			editTextUsername.setText(intent.getStringExtra(KEY_USERNAME))
		}

		// Toolbar Configurations
		setSupportActionBar(findViewById(R.id.toolbarSignUp))
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)

		editTextUsername.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) {
				checkUsernameLegality()
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
				editTextUsername.requestFocus()
				editTextUsername.error = getString(R.string.prompt_enter_username)
				return@setOnClickListener
			}
			if (password.isBlank()) {
				editTextPassword.requestFocus()
				editTextPassword.error = getString(R.string.prompt_enter_password)
				return@setOnClickListener
			}
			if (password2.isBlank()) {
				editTextPassword2.requestFocus()
				editTextPassword2.error = getString(R.string.prompt_enter_password2)
				return@setOnClickListener
			}
			if (!checkPasswordConsistency()) {
				return@setOnClickListener
			}

			val signUpResult = signUp(username, password)
			if (signUpResult.successful) {
				val backIntent = Intent()
				backIntent.putExtra(KEY_USERNAME, username)
				backIntent.putExtra(KEY_REASON, signUpResult.reason)
				setResult(RESULT_OK, backIntent)
				finish()
			}
		}
	}

	private fun signUp(username: String, password: String): SignUpResult {
		// TODO Send request using Retrofit

		val signUpResult = DEBUG_RESULT_SUCCESS
		return signUpResult
	}

	private fun checkUsernameLegality(): Boolean {
		val username = editTextUsername.text.toString()
		if (!username.isLegalUsername()) {
			editTextUsername.requestFocus()
			editTextUsername.error = getString(R.string.error_illegal_username)
			return false
		}
		return true
	}

	private fun checkPasswordConsistency(): Boolean {
		val password = editTextPassword.text.toString()
		val password2 = editTextPassword2.text.toString()
		if (password != password2) {
			editTextPassword2.requestFocus()
			editTextPassword2.error = getString(R.string.error_password_inconsistent)
			return false
		}
		return true
	}
}