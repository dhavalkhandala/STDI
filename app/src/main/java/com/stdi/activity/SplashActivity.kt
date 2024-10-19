package com.stdi.activity



import android.os.Bundle
import android.os.Handler

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*

import com.google.firebase.firestore.FirebaseFirestore
import com.stdi.R
import com.stdi.extras.AppUtils



class SplashActivity : AppCompatActivity() {

    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseFirestore = FirebaseFirestore.getInstance();
         init()

    }

    private fun init() {
        Handler().postDelayed(
            { AppUtils.callActivityWithFinish(this, HomeActivity::class.java) },
            2000
        )
    }
}
