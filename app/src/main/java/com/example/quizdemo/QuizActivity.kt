package com.example.quizdemo

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizdemo.adapter.QuizAdapter
import com.example.quizdemo.entity.Answer
import com.example.quizdemo.entity.Quiz
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.content_quiz.*

class QuizActivity : AppCompatActivity() {

	companion object {
		const val KEY_ANSWER_LIST = "answerList"
		const val TOOL_TIP_TEXT_SUBMIT = "提交答案"
	}

	private val answerList = ArrayList<Answer>()
	private val checkedIndexMap = HashMap<Int, Int>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_quiz)
		setSupportActionBar(findViewById(R.id.toolbar))

		initQuiz()

		recyclerViewQuiz.let {
			it.layoutManager = LinearLayoutManager(this)
			it.adapter = QuizAdapter(answerList)
		}

		fab.apply {
			if (Build.VERSION.SDK_INT >= O) tooltipText = TOOL_TIP_TEXT_SUBMIT
			setOnClickListener {
				submit()
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return when (item.itemId) {
			R.id.action_settings -> true
			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun submit() {
		val intent = Intent(this, ResultActivity::class.java)
		intent.putParcelableArrayListExtra(KEY_ANSWER_LIST, answerList)

	}

	private fun initQuiz() {
		answerList.apply {
			repeat(3) {
				add(
					Answer(
						"“入我相思门，知我相思苦”是谁写的？",
						arrayListOf("白居易", "杜甫", "李白", "纳兰性德"),
						2
					)
				)
				add(
					Answer(
						"“相思”与下面哪个词意思相近？",
						arrayListOf("思念", "思考", "相信"),
						0
					)
				)
				add(
					Answer(
						"以下哪种物品可以表达相思之情？",
						arrayListOf("桃花", "牡丹", "绿豆", "红豆"),
						3
					)
				)
			}
		}
	}

}