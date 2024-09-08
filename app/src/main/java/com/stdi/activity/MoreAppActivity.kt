package com.stdi.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.stdi.R
import com.stdi.adapter.MoreAppAdapter
import com.stdi.model.MoreAppDetailsModel
import kotlinx.android.synthetic.main.activity_more_app.*
import java.lang.Exception

class MoreAppActivity : AppCompatActivity() {
    private var firebaseFirestore: FirebaseFirestore? = null
    lateinit var moreAppList:ArrayList<MoreAppDetailsModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_app)
        firebaseFirestore = FirebaseFirestore.getInstance()
        moreAppList= ArrayList()
        getMoreAppdata()
    }
    private fun getMoreAppdata() {
        val collRef: CollectionReference =firebaseFirestore!!.collection("more_apps")
        collRef.get().addOnCompleteListener(this@MoreAppActivity, object :
            OnCompleteListener<QuerySnapshot?> {
            override fun onComplete(p0: Task<QuerySnapshot?>) {
                if (p0.isSuccessful){
                    for (i in p0.result!!.documents)
                    {
                        if (i != null && i.exists()){
                            val appname:String= i.get("app_name").toString()
                            val appicon:String= i.get("app_icon").toString()
                            val appdesc:String= i.get("app_des").toString()
                            val apppackage:String= i.get("app_package").toString()
                            val appdownload:String= i.get("app_download").toString()
                            moreAppList.add(MoreAppDetailsModel(appname,appicon,appdesc,apppackage,appdownload))
                            Log.e("TAG", "APP Name: ${appname},$appicon,$appdesc,$apppackage")
                        }else{
                            Log.d("TAG", "No such document")
                        }
                    }
                    val adapter: MoreAppAdapter = MoreAppAdapter(this@MoreAppActivity,moreAppList)
                    rv_more_aap.adapter=adapter

                }else{
                    Log.e("TAG", "get failed with : ${p0.exception!!.message.toString()}" )
                }
            }
        }).addOnFailureListener(this@MoreAppActivity,object : OnFailureListener {
            override fun onFailure(p0: Exception) {
                Log.d("TAG", "get failed with ${p0.message.toString()}")
            }
        })

    }
}