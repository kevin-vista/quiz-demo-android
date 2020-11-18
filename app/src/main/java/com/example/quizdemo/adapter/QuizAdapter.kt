package com.example.quizdemo.adapter

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
import com.example.quizdemo.entity.Quiz

class QuizAdapter(private val quizList: List<Quiz>) :
	RecyclerView.Adapter<QuizAdapter.ViewHolder>() {

	companion object {
		const val VIEW_TYPE = 7849
	}

	private val checkedIndexes = HashMap<Int, Int>(quizList.size)

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
		val quiz = quizList[realPosition]

		holder.apply {
			val questionText = "${realPosition+1}. ${quiz.question}"
			textViewQuestion.text = questionText

			radioGroupOptions.id = realPosition + 10_000
			radioGroupOptions.removeAllViews()
			radioGroupOptions.setOnCheckedChangeListener { _, checkedId ->
				if (checkedId >= 0)
					checkedIndexes[realPosition] = checkedId
			}

			// Add choices
			for (i in quiz.optionList.indices) {
				val radioButtonOption = RadioButton(holder.itemView.context).apply {
					id = i + 1_000
					layoutParams = RadioGroup.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT
					).apply {
						topMargin = 2
						bottomMargin = 2
					}
					setTextSize(SP, 16F)
					text = quiz.optionList[i]
				}
				radioGroupOptions.addView(radioButtonOption)
			}
			if (realPosition in checkedIndexes.keys) {
				radioGroupOptions.clearCheck()
				val checkedIndex: Int = checkedIndexes[realPosition]!!
				radioGroupOptions.check(checkedIndex)
			}
		}
	}

	override fun getItemCount(): Int = quizList.size

	override fun getItemViewType(position: Int): Int {
		return VIEW_TYPE
	}
}