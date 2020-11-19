package com.example.quizdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizdemo.QuizActivity.Companion.KEY_ANSWER_LIST
import com.example.quizdemo.adapter.ResultAdapter
import com.example.quizdemo.entity.Answer
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

	companion object {
		const val TAG = "ResultActivity"
	}

	lateinit var answerList: List<Answer>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_result)

		val bundle = intent.getBundleExtra(KEY_ANSWER_LIST)
		answerList = bundle?.get(KEY_ANSWER_LIST) as List<Answer>

		recyclerViewResult.layoutManager = LinearLayoutManager(this)
		recyclerViewResult.adapter = ResultAdapter(answerList)

	}
}