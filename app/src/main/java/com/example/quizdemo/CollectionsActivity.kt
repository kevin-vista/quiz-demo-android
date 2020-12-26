package com.example.quizdemo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizdemo.adapter.CollectionAdapter
import com.example.quizdemo.service.CollectionService
import kotlinx.android.synthetic.main.activity_collections.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException

class CollectionsActivity : SecureActivity() {

	companion object {
		const val TAG = "CollectionsActivity"
		const val EXIT_TIME_THRESHOLD = 2000L
	}

	private lateinit var collectionNameList: List<String>

	private val job = Job()

	private var exitTime = 0L

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_collections)
		setSupportActionBar(findViewById(R.id.toolbarCollections))

		collectionNameList = emptyList()

		recyclerViewCollections.layoutManager = LinearLayoutManager(this@CollectionsActivity)

		refreshLayoutCollections.setOnRefreshListener {	reloadCollections() }
		reloadCollections()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_collections, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_profile -> {
				val profileIntent = Intent(this, ProfileActivity::class.java)
				startActivity(profileIntent)
				true
			}
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

	override fun onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > EXIT_TIME_THRESHOLD) {
			Toast.makeText(
				this,
				getString(R.string.double_press_to_exit),
				Toast.LENGTH_SHORT
			).show()
			exitTime = System.currentTimeMillis()
		} else {
			ActivityCollector.finishAll()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		job.cancel(null)
	}

	private fun reloadCollections() {
		CoroutineScope(job).launch {
			val collectionNames = getCollectionNames()
			withContext(Dispatchers.Main) {
				collectionNameList = collectionNames
				recyclerViewCollections.adapter = CollectionAdapter(collectionNameList)

				refreshLayoutCollections.isRefreshing = false
			}
		}
	}

	private suspend fun getCollectionNames(): List<String> {
		val retrofit = Retrofit.Builder()
			.baseUrl(getString(R.string.base_url))
			.addConverterFactory(GsonConverterFactory.create())
			.build()
		val collectionService = retrofit.create<CollectionService>()
		return try {
			collectionService.getAll().await()
		} catch (e: IOException) {
			e.printStackTrace()
			emptyList()
		}
	}

}