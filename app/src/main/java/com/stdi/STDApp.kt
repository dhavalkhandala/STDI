package com.stdi

import android.app.Activity
import android.app.Application
import android.content.*
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.*

import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.stdi.activity.HomeActivity

import com.stdi.extras.AppConstants
import com.stdi.extras.AppUtils

lateinit var app: STDApp

class STDApp : Application() {

    private var sharedPreferences: SharedPreferences? = null
    private var timeChangeReceiver: SystemTimeChangeReceiver? = null

    lateinit var firestoreDB: FirebaseFirestore
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private var currentActivity: Activity? = null

    private val  LOG_TAG:String = "AppOpenAdManager"
    private val  AD_UNIT_ID:String = "ca-app-pub-3940256099942544/3419835294"
    var isAppBackground = false


    override fun onCreate() {
        super.onCreate()
        app = this



        FirebaseApp.initializeApp(this)
        firestoreDB = FirebaseFirestore.getInstance()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.setAnalyticsCollectionEnabled(true)



        AppUtils.adjustFontScale(this)
        registerActivityLifecycleCallbacks(MyActivityLifecycleCallbacks())
    }



    fun getPreferences(): SharedPreferences {
        if (sharedPreferences == null)
            sharedPreferences = this.getSharedPreferences(
                "STDApp", Context.MODE_PRIVATE
            )
        return sharedPreferences!!
    }


    private inner class MyActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity.componentName.className == HomeActivity::class.java.name)
                registerTimeReceiver(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            isAppBackground = false
        }

        override fun onActivityPaused(activity: Activity) {
            isAppBackground = true
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity.componentName.className == HomeActivity::class.java.name)
                unRegisterTimeReceiver(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    }


    private fun registerTimeReceiver(activity: Activity) {
        if (timeChangeReceiver == null) {
            timeChangeReceiver = SystemTimeChangeReceiver()
            activity.registerReceiver(timeChangeReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
        }
    }

    private fun unRegisterTimeReceiver(activity: Activity) {
        try {
            if (timeChangeReceiver != null) {
                activity.unregisterReceiver(timeChangeReceiver)
                timeChangeReceiver = null
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    inner class SystemTimeChangeReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (TextUtils.isEmpty(action) || action != Intent.ACTION_TIME_TICK || app.isAppBackground)
                return
            sendBroadcast(Intent(AppConstants.BROADCAST_TIME_TICK))

        }
    }


}