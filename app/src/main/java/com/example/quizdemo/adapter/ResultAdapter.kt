package com.example.quizdemo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.Dimension.SP
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.quizdemo.R
import com.example.quizdemo.entity.Answer

class ResultAdapter(private val answerList: List<Answer>) :
	RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

	companion object {
		const val TAG = "ResultAdapter"
		const val BIAS_GROUP = 10_000
	}

//	private val checkedIndexes = HashMap<Int, Int>(answerList.size)

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val textViewQuestion: TextView = view.findViewById(R.id.textViewQuestion)
		val radioGroupOptions: RadioGroup = view.findViewById(R.id.radioGroupOptions)
		val textViewScore: TextView = view.findViewById(R.id.textViewScore)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			LayoutInflater.from(parent.context)
				.inflate(R.layout.item_result, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val realPosition = holder.layoutPosition
//		Log.i(TAG, answerList[realPosition]::class.java.toString())
		val answer = answerList[realPosition]
		Log.i(TAG, answer.toString())

		holder.apply {
			val questionText = "${realPosition+1}. ${answer.question}"
			textViewQuestion.text = questionText

			radioGroupOptions.apply {
				id = realPosition + BIAS_GROUP
				removeAllViews()
			}

			// Add choices
			for (i in answer.optionList.indices) {
				val radioButtonOption = RadioButton(holder.itemView.context).apply {
//					id = i + 1_000
					layoutParams = RadioGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT
					).apply {
						topMargin = 2
						bottomMargin = 2
					}

					// Results should be readonly
					isClickable = false

					setTextSize(SP, 18F)
					text = answer.optionList[i]

					// Set button color
					if (i == answer.correctIndex) {
						buttonTintList = AppCompatResources.getColorStateList(
							holder.itemView.context,
							R.color.tint_correct
						)
						isChecked = true
					} else if (i == answer.checkedIndex) {
						buttonTintList = AppCompatResources.getColorStateList(
							holder.itemView.context,
							R.color.tint_checked_wrong
						)
						isChecked = true
					}
				}
				radioGroupOptions.addView(radioButtonOption)
			}

			val score = if (answer.checkedIndex == answer.correctIndex) {
				1.0
			} else {
				0.0
			}
			val textCorrect = answer.optionList[answer.correctIndex]

			val textChecked =
				if (answer.checkedIndex >= 0)
					answer.optionList[answer.checkedIndex]
				else
					itemView.context.getString(R.string.not_answered)
			val textScore = "本题答案:  $textCorrect\n我的选项:  $textChecked\n本题得分:  $score/1.0"
			textViewScore.text = textScore
		}
	}

	override fun getItemCount(): Int = answerList.size

}