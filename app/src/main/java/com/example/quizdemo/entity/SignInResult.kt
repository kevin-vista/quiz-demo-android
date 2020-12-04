package com.example.quizdemo.entity

import com.google.gson.annotations.SerializedName

class SignInResult(
	@field:SerializedName("successful") val successful: Boolean,
	@field:SerializedName("reason") val reason: String
) {

	companion object {
		const val SUCCESS = "登录成功"
		const val FAILURE_NO_SUCH_USER = "用户名不存在"
		const val FAILURE_WRONG_PASSWORD = "用户名和密码不匹配"
		const val FAILURE_NETWORK = "网络连接错误"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SignInResult

		if (reason != other.reason) return false

		return true
	}

	override fun hashCode(): Int {
		return reason.hashCode()
	}

	override fun toString(): String {
		return "SignInResult(${if (successful) "successful" else "failure"}: $reason)"
	}

}