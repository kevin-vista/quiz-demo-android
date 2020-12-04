package com.example.quizdemo.entity

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
	@field:SerializedName("username") val username: String,
	@field:SerializedName("password") val password: String
)