package com.example.quizdemo.entity

import java.io.Serializable
import java.util.*

open class Quiz(
    val question: String,
    val optionList: ArrayList<String>,
    val correctIndex: Int
) : Serializable {

    fun toAnsweredQuiz(): AnsweredQuiz = AnsweredQuiz(question, optionList, correctIndex)

}