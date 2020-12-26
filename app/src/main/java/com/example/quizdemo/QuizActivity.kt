package com.example.quizdemo

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizdemo.adapter.QuizAdapter
import com.example.quizdemo.entity.AnsweredQuiz
import com.example.quizdemo.entity.Quiz
import com.example.quizdemo.service.CollectionService
import com.example.quizdemo.util.isCompleted
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.content_quiz.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException

class QuizActivity : BaseActivity() {

	companion object {
		const val TAG = "QuizActivity"
		const val KEY_ANSWER_LIST = "answerList"
	}

	private val job = Job()

	private val answerList = ArrayList<AnsweredQuiz>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_quiz)
		setSupportActionBar(findViewById(R.id.toolbarQuiz))

		if (savedInstanceState?.containsKey(KEY_ANSWER_LIST) == true) {
			answerList += savedInstanceState.getSerializable(KEY_ANSWER_LIST) as List<AnsweredQuiz>
			Log.d(TAG, "answerList recovered")
		}
		if (answerList.isEmpty()) {
			val collectionName = intent.getStringExtra(getString(R.string.key_collection_name))?: ""
			CoroutineScope(job).launch {
				val answeredQuizzes = getQuizzes(collectionName)
				withContext(Dispatchers.Main) {
					for (aq in answeredQuizzes) {
						answerList += aq.toAnsweredQuiz()
					}
					Log.d(TAG, "answerList: $answerList")
					recyclerViewQuiz.layoutManager = LinearLayoutManager(this@QuizActivity)
					recyclerViewQuiz.adapter = QuizAdapter(answerList)
				}
			}
			Log.d(TAG, "answerList initialized")
		}

		fab.apply {
			if (Build.VERSION.SDK_INT >= O) {
				tooltipText = getString(R.string.tool_tip_submit)
			}
			setOnClickListener {
				if (!answerList.isCompleted()) {
					AlertDialog.Builder(context).apply {
						setTitle(R.string.title_prompt)
						setMessage(R.string.message_not_completed)
						setPositiveButton(R.string.force_submit) { _, _ ->
							submit()
						}
						setNegativeButton(R.string.cancel) { _, _ -> }
					}.create().show()
				} else {
					AlertDialog.Builder(context).apply {
						setTitle(R.string.title_prompt)
						setMessage(R.string.message_completed)
						setPositiveButton(R.string.submit) { _, _ ->
							submit()
						}
						setNegativeButton(R.string.cancel) { _, _ -> }
					}.create().show()
				}
			}  // fab.OnClickListener
		}  // fab.apply
	}  // onCreate

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		if (answerList.isNotEmpty()) {
			outState.putSerializable(KEY_ANSWER_LIST, answerList)
			Log.d(TAG, "answerList saved")
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_quiz, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return when (item.itemId) {
			R.id.action_open_source_license -> {
				AlertDialog.Builder(this)
					.setMessage(R.string.open_source_license)
					.create()
					.show()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun submit() {
		val answerListBundle = Bundle()
		answerListBundle.putSerializable(KEY_ANSWER_LIST, answerList)

		val intent = Intent(this, ResultActivity::class.java)
		intent.putExtra(KEY_ANSWER_LIST, answerListBundle)
		startActivity(intent)
	}

	private suspend fun getQuizzes(collectionName: String): List<Quiz> {
		val retrofit = Retrofit.Builder()
			.baseUrl(getString(R.string.base_url))
			.addConverterFactory(GsonConverterFactory.create())
			.build()
		val collectionService = retrofit.create<CollectionService>()
		return try {
			collectionService.getQuizzes(collectionName).await()
		} catch (e: IOException) {
			ArrayList(0)
		}

		/*answerList.apply {
			repeat(3) {
				add(
					AnsweredQuiz(
						"“入我相思门，知我相思苦”是谁写的？",
						arrayListOf("白居易", "杜甫", "李白", "纳兰性德"),
						2,
						-1
					)
				)
				add(
					AnsweredQuiz(
						"“相思”与下面哪个词意思相近？",
						arrayListOf("思念", "思考", "相信"),
						0,
						-1
					)
				)
				add(
					AnsweredQuiz(
						"以下哪种物品可以表达相思之情？",
						arrayListOf("桃花", "牡丹", "绿豆", "红豆"),
						3,
						-1
					)
				)
			}
		}*/
	}

}