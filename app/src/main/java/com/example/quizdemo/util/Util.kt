package com.example.quizdemo.util

import com.example.quizdemo.entity.Answer

private const val LEGAL_USERNAME = "^[A-Za-z0-9_]{4,32}$"

/**
 * @return Whether a string is legal to be a username
 */
fun String.isLegalUsername(): Boolean = matches(Regex(LEGAL_USERNAME))

fun List<Answer>.isCompleted(): Boolean = all { it.checkedIndex >= 0 }
