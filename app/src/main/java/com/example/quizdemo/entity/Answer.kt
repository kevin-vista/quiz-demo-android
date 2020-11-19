package com.example.quizdemo.entity

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class Answer(
	question: String,
	optionList: ArrayList<String>,
	correctIndex: Int,
	var checkedIndex: Int = -1
) : Quiz(
	question,
	optionList,
	correctIndex
), Serializable {

	override fun toString(): String {
		return "Answer{question = $question, " +
				"optionList = $optionList, " +
				"correctIndex = $correctIndex, " +
				"checkedIndex = $checkedIndex}"
	}

}
