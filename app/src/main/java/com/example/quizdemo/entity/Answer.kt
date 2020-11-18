package com.example.quizdemo.entity

class Answer(
	question: String,
	optionList: List<String>,
	correctIndex: Int,
	var checkedIndex: Int = -1
) : Quiz(
	question,
	optionList,
	correctIndex
)
