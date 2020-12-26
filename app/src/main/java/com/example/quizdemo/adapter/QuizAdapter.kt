package com.example.quizdemo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.Dimension.SP
import androidx.recyclerview.widget.RecyclerView
import com.example.quizdemo.R
import com.example.quizdemo.entity.AnsweredQuiz

class QuizAdapter(private val answeredQuizList: List<AnsweredQuiz>) :
	RecyclerView.Adapter<QuizAdapter.ViewHolder>() {

	companion object {
		const val TAG = "QuizAdapter"
		const val VIEW_TYPE = 7849
		const val BIAS_GROUP = 10_000
		const val BIAS_BUTTON = 1_000
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val textViewQuestion: TextView = view.findViewById(R.id.textViewQuestion)
		val radioGroupOptions: RadioGroup = view.findViewById(R.id.radioGroupOptions)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			LayoutInflater.from(parent.context)
				.inflate(R.layout.item_quiz, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val realPosition = holder.layoutPosition
		val answer = answeredQuizList[realPosition]

		holder.apply {
			val questionText = "${realPosition+1}. ${answer.question}"
			textViewQuestion.text = questionText

			radioGroupOptions.id = realPosition + BIAS_GROUP
			radioGroupOptions.removeAllViews()
			radioGroupOptions.setOnCheckedChangeListener { _, checkedId ->
				if (checkedId >= 0) {
					answer.checkedIndex = checkedId - BIAS_BUTTON
				}
			}

			// Add choices
			for (i in answer.optionList.indices) {
				val radioButtonOption = RadioButton(holder.itemView.context).apply {
					id = i + BIAS_BUTTON
					layoutParams = RadioGroup.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT
					).apply {
						topMargin = 2
						bottomMargin = 2
					}

					setTextSize(SP, 18F)
					text = answer.optionList[i]
				}
				radioGroupOptions.addView(radioButtonOption)
			}
			if (answer.checkedIndex >= 0) {
				radioGroupOptions.clearCheck()
				val checkedIndex = answer.checkedIndex + BIAS_BUTTON
				radioGroupOptions.check(checkedIndex)
			}
		}
	}

	override fun getItemCount(): Int = answeredQuizList.size

	override fun getItemViewType(position: Int): Int {
		Log.d(TAG, "getItemViewType")
		return VIEW_TYPE
	}
}