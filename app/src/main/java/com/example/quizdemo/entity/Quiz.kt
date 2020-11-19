package com.example.quizdemo.entity

import java.io.Serializable
import java.util.ArrayList

open class Quiz(
    val question: String,
    val optionList: ArrayList<String>,
    val correctIndex: Int
) : Serializable