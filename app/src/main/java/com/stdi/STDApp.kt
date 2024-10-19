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

    lateinit var firestoreDB: FirebaseFirestore
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        app = this
        FirebaseApp.initializeApp(this)
        firestoreDB = FirebaseFirestore.getInstance()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.setAnalyticsCollectionEnabled(true)
        AppUtils.adjustFontScale(this)
    }
}