package com.example.quizdemo

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizdemo.QuizActivity.Companion.KEY_ANSWER_LIST
import com.example.quizdemo.adapter.ResultAdapter
import com.example.quizdemo.entity.AnsweredQuiz
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : BaseActivity() {

	companion object {
		const val TAG = "ResultActivity"
	}

	lateinit var answeredQuizList: List<AnsweredQuiz>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_result)

		val bundle = intent.getBundleExtra(KEY_ANSWER_LIST)
		answeredQuizList = bundle?.get(KEY_ANSWER_LIST) as List<AnsweredQuiz>

		recyclerViewResult.layoutManager = LinearLayoutManager(this)
		recyclerViewResult.adapter = ResultAdapter(answeredQuizList)
	}

	override fun onBackPressed() {
		val collectionsIntent = Intent(this, CollectionsActivity::class.java)
		startActivity(collectionsIntent)
		finish()
	}
}