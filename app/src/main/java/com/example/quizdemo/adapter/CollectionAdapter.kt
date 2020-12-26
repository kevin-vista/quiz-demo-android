package com.example.quizdemo.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizdemo.QuizActivity
import com.example.quizdemo.R

class CollectionAdapter(private val collectionList: List<String>) :
	RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

	companion object {
		const val TAG = "CollectionAdapter"
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val textViewCollectionName: TextView = view.findViewById(R.id.textViewCollectionName)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		Log.d(TAG, "onCreateViewHolder: size: ${collectionList.size}")
		return ViewHolder(
			LayoutInflater.from(parent.context)
				.inflate(R.layout.item_collection, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val collectionName = collectionList[position]
		holder.textViewCollectionName.text = collectionName
		holder.itemView.setOnClickListener {
			val context = holder.itemView.context
			val collectionIntent = Intent(context, QuizActivity::class.java)
			collectionIntent.putExtra(context.getString(R.string.key_collection_name), collectionName)
			context.startActivity(collectionIntent)

		}
	}

	override fun getItemCount(): Int = collectionList.size
}