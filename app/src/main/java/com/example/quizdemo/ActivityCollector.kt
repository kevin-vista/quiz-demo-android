package com.example.quizdemo

import android.app.Activity

object ActivityCollector {

	private val activities = ArrayList<Activity>()

	fun addActivity(activity: Activity) {
		activities += activity
	}

	fun removeActivity(activity: Activity) {
		activities += activity
	}

	fun finishAll() {
		for (a in activities) {
			if (!a.isFinishing) {
				a.finish()
			}
		}
	}

}