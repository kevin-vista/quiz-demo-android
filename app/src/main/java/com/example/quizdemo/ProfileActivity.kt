package com.example.quizdemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {

	private lateinit var sharedPref: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile)

		sharedPref = getSharedPreferences(getString(R.string.key_user_preference), Context.MODE_PRIVATE)

		textViewCurrentUser.text = getString(
			R.string.current_user,
			sharedPref.getString(getString(R.string.key_username), "")
		)

		buttonSignOut.setOnClickListener {
			with (sharedPref.edit()) {
				remove(getString(R.string.key_username))
				remove(getString(R.string.key_password))
				commit()
			}
			Toast.makeText(this, getString(R.string.signed_out), Toast.LENGTH_SHORT).show()
			startActivity(Intent(this, SignInActivity::class.java))
		}
	}
}