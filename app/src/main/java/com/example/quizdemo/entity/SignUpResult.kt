package com.example.quizdemo.entity

import com.google.gson.annotations.SerializedName

class SignUpResult(
	@field:SerializedName("successful") val successful: Boolean,
	@field:SerializedName("reason") val reason: String
) {

	companion object {
		const val SUCCESS = "账户创建成功"
		const val FAILURE_ILLEGAL_USERNAME = "用户名不合法"
		const val FAILURE_DUPLICATE_USERNAME = "此用户名已被占用"
		const val FAILURE_NETWORK = "网络连接错误"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SignUpResult

		if (reason != other.reason) return false

		return true
	}

	override fun hashCode(): Int {
		return reason.hashCode()
	}

	override fun toString(): String {
		return "SignUpResult(${if (successful) "successful" else "failure"}: $reason)"
	}

}