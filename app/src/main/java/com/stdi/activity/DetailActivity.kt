package com.stdi.activity

import android.app.Activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import android.graphics.Typeface

import android.os.Bundle
import android.text.TextUtils
import android.util.Log

import android.util.TypedValue
import android.view.*

import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

import com.stdi.R
import com.stdi.databinding.ActivityDetailBinding

import com.stdi.extras.AppConstants
import com.stdi.extras.AppUtils
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class DetailActivity : AppCompatActivity() {
    private val activity: Activity = this
    private var fileName: String? = null
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (intent.hasExtra(AppConstants.FILE_NAME))
            fileName = intent.getStringExtra(AppConstants.FILE_NAME)
        if (intent.hasExtra(AppConstants.POSITION))
            position = intent.getIntExtra(AppConstants.POSITION, 0)

        if (TextUtils.isEmpty(fileName)) {
            AppUtils.toastLong(activity, "No Data found")
            finish()
        }

        initToolbar()
        init()
    }

    private fun initToolbar() {
        toolbar.title = fileName
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp)
        AppUtils.changeToolbarStatusBarColor(activity, toolbar, position)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun init() {

        when (position % 5) {
            1 -> ivBG.setBackgroundResource(R.drawable.gradient_brown)
            2 -> ivBG.setBackgroundResource(R.drawable.gradient_drak_pink)
            3 -> ivBG.setBackgroundResource(R.drawable.gradient_green)
            4 -> ivBG.setBackgroundResource(R.drawable.gradient_pink)
            else -> ivBG.setBackgroundResource(R.drawable.gradient_navy_blue)
        }
        readDataFromFile()
    }


    private fun readDataFromFile() {
        try {
            val fileNameFormated = AppUtils.fileNameList[position]
                .replace(" ", "_")
                .replace("/", "_")

            val br = BufferedReader(
                InputStreamReader(
                    assets.open(
                        fileNameFormated + "/" + fileNameFormated + "_" +
                                AppUtils.languageList[AppUtils.defaultLangPos].code + ".txt"
                    ), "UTF-8"
                )
            )
            var line: String? = br.readLine()
            var specialString: String? = null

            var subTitle: String? = null
            var subSubTitle: String? = null
            var subTitleData = StringBuilder()

            val lineBreak = "\n"

            while (line != null) {
                line = line.trim()
                if (AppUtils.checkStringContainsSpecialChar(line)) {
                    specialString = line
                    line = br.readLine()
                    continue
                }
                if (!TextUtils.isEmpty(specialString)) {
                    when (specialString) {
                        AppConstants.TITLE_HINT -> {
                        }
                        AppConstants.SUB_TITLE_HINT -> {
                            if (!TextUtils.isEmpty(subTitle) && !TextUtils.isEmpty(subTitleData.toString()))
                                createSubTitleView(subTitle, subTitleData.toString())

                            subTitle = line
                            subSubTitle = ""
                            subTitleData = StringBuilder()
                        }
                        AppConstants.SUB_SUB_TITLE_HINT -> {
                            if (!TextUtils.isEmpty(line)) {
                                subTitleData.append(lineBreak).append(line).append(lineBreak)
                                subSubTitle = line
                            }
                        }
                        AppConstants.LIST_HINT -> {
                            if (!TextUtils.isEmpty(line))
                                subTitleData.append("●  ").append(line).append(lineBreak)
                        }
                        AppConstants.SUB_LIST_HINT -> {
                            if (!TextUtils.isEmpty(subSubTitle))
                                subTitleData.append("\t").append("●  ").append(line).append(lineBreak)
                        }
                        else -> {
                            if (!TextUtils.isEmpty(line))
                                subTitleData.append(line).append(lineBreak)
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(line))
                        subTitleData.append(line).append(lineBreak)
                }
                specialString = ""
                line = br.readLine()
            }
            if (!TextUtils.isEmpty(subTitleData.toString()))
                createSubTitleView(subTitle, subTitleData.toString())
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createSubTitleView(subTitle: String?, subTitleData: String) {
        val linearLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val frameLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val margin4 = resources.getDimensionPixelSize(R.dimen.margin_4)
        val margin10 = resources.getDimensionPixelSize(R.dimen.margin_10)

        val cardView = CardView(activity)
        cardView.layoutParams = linearLayoutParams
        cardView.radius = margin10.toFloat()
        cardView.cardElevation = margin4.toFloat()
        cardView.useCompatPadding = true
        cardView.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.white))

        val linearLayout = LinearLayout(activity)
        linearLayout.layoutParams = frameLayoutParams
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(margin10, margin10, margin10, margin10)

        if (!TextUtils.isEmpty(subTitle)) {
            val subTitleTextView = AppCompatTextView(activity)
            subTitleTextView.layoutParams = linearLayoutParams
            subTitleTextView.setTextColor(ContextCompat.getColor(activity, R.color.black))
            subTitleTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimensionPixelSize(R.dimen.text_size_20).toFloat()
            )
            subTitleTextView.setTypeface(null, Typeface.BOLD)
            subTitleTextView.gravity = Gravity.CENTER
            subTitleTextView.text = subTitle
            linearLayout.addView(subTitleTextView)
        }


        val subTitleDataTextView = AppCompatTextView(activity)
        subTitleDataTextView.layoutParams = linearLayoutParams
        subTitleDataTextView.setPadding(0, margin4, 0, 0)
        subTitleDataTextView.setTextColor(ContextCompat.getColor(activity, R.color.black))
        subTitleDataTextView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimensionPixelSize(R.dimen.text_size_14).toFloat()
        )
        subTitleDataTextView.lineHeight = resources.getDimensionPixelSize(R.dimen.margin_20)
        subTitleDataTextView.text = subTitleData
        linearLayout.addView(subTitleDataTextView)

        cardView.addView(linearLayout)
        llDetail.addView(cardView)
    }
}