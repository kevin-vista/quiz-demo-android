package com.example.quizdemo

import com.example.quizdemo.entity.Answer

fun List<Answer>.isCompleted(): Boolean = all { it.checkedIndex >= 0 }
