package com.example.quizdemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.example.quizdemo.entity.SignInResult
import com.example.quizdemo.util.signIn
import kotlinx.coroutines.*

open class SecureActivity : BaseActivity() {

	private val job = Job()

	private lateinit var sharedPref: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		sharedPref = getSharedPreferences(
			getString(R.string.key_user_preference),
			Context.MODE_PRIVATE
		)

		if (getString(R.string.key_username) in sharedPref &&
			getString(R.string.key_password) in sharedPref) {
			val username = sharedPref.getString(getString(R.string.key_username), "")
			val password = sharedPref.getString(getString(R.string.key_password), "")

			if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
				return
			}
			CoroutineScope(job).launch {
				val signInResult = signIn(this@SecureActivity, username, password)
				if (signInResult.reason == SignInResult.FAILURE_NO_SUCH_USER ||
					signInResult.reason == SignInResult.FAILURE_WRONG_PASSWORD) {
					withContext(Dispatchers.Main) {
						startActivity(Intent(this@SecureActivity, SignInActivity::class.java))
					}
				}
			}  // CoroutineScope.launch
		}  // if
	}  // onCreate

	override fun onDestroy() {
		super.onDestroy()
		job.cancel(null)
	}
}