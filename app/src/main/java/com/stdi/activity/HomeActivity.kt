package com.stdi.activity



import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*

import com.stdi.BuildConfig
import com.stdi.R
import com.stdi.SharedPref
import com.stdi.adapter.CardListAdapter
import com.stdi.adapter.VerticalListAdapter
import com.stdi.app
import com.stdi.cardstackview.CardStackLayoutManager
import com.stdi.cardstackview.CardStackListener
import com.stdi.cardstackview.Direction
import com.stdi.cardstackview.StackFrom
import com.stdi.extras.*
import kotlinx.android.synthetic.main.activity_home.*
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig
import java.io.BufferedReader
import java.io.InputStreamReader


class HomeActivity : AppCompatActivity(), LanguageListDialogFragment.Listener {

    private lateinit var planTitle: String
    private lateinit var planPrice: String
    private var now: Long = 0
    private val activity: Activity = this

    private val session: MySession? = null
    private var readyToPurchase = false
    private val productId = "plan.stdi.purchase"
    private val TAG = "HomeActivity"


    private var systemTimeTickReceiver: DetailActivity.SystemTimeTickReceiver? = null




    private var verticalListAdapter: VerticalListAdapter? = null
    private var cardListAdapter: CardListAdapter? = null


    private val titleList: MutableList<String> = mutableListOf()
    private val infoList: MutableList<String> = mutableListOf()

    private var isListTypeCard = true
    private var isCardEmpty = false
    private var width = 0
    private var height = 0
    val afterMinute: Long = System.currentTimeMillis() + 70000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val margin =
            (AppUtils.getScreenWidth(activity) / 2) - resources.getDimensionPixelSize(R.dimen.margin_88)
        fabLanguage.animate().setDuration(1).translationX(margin.toFloat()).start()
        fabListType.animate().setDuration(1).translationX(-margin.toFloat()).start()

        for ((index, value) in AppUtils.fileNameList.withIndex()) {
            readDataFromFile(value, index)
        }
        SharedPref.init(activity)
        initToolbar()
        showTutorialsShowcase()
    }



    public override fun onStart() {
        super.onStart()

    }

    public override fun onResume() {
        super.onResume()

        registerTimeReceiver()
    }

    public override fun onPause() {
        super.onPause()

        unRegisterTimeReceiver()
    }

    public override fun onStop() {
        super.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onBackPressed() {
            super.onBackPressed()

    }

    private fun initToolbar() {
        toolbar.setTitle("STD App"/*R.string.app_name*/)
        AppUtils.changeToolbarStatusBarColor(activity, toolbar, 0)
        changeFabButtonColor(0)
        setSupportActionBar(toolbar)
        lv_more_app.playAnimation()
        lv_more_app.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(Intent(this@HomeActivity,MoreAppActivity::class.java))
            }
        })
    }

    private fun init() {
        val cardStackLayoutManager = CardStackLayoutManager(activity, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {}
            override fun onCardSwiped(direction: Direction) {}
            override fun onCardRewound() {}
            override fun onCardCanceled() {}

            override fun onCardAppeared(view: View?, position: Int) {
                var pos = position
                if (pos < 0 || pos > titleList.size - 1)
                    pos = 0
                AppUtils.changeToolbarStatusBarColor(activity, toolbar, pos)
                changeFabButtonColor(pos)
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                if (position == titleList.size - 1) {
                    isCardEmpty = true
                    AppUtils.popupAnimation(llReload)
                    AppUtils.popdownAnimationLarge(cardStackView)
                }
            }
        })
        cardStackLayoutManager.setStackFrom(StackFrom.Bottom)
        cardStackLayoutManager.setDirections(Direction.FREEDOM)
        cardStackLayoutManager.setTranslationInterval(12f)
        cardStackLayoutManager.setCanScrollHorizontal(true)
        cardStackLayoutManager.setCanScrollVertical(true)

        cardStackView.layoutManager = cardStackLayoutManager
        if (cardListAdapter == null)
            cardListAdapter = CardListAdapter(activity, titleList, infoList)
        cardStackView.adapter = cardListAdapter

        recyclerView.layoutManager = LinearLayoutManager(activity)
        if (verticalListAdapter == null)
            verticalListAdapter = VerticalListAdapter(activity, titleList, infoList)
        recyclerView.adapter = verticalListAdapter

        fabListType.setOnClickListener {
            AppUtils.popDownUpAnimation(fabListType)
            Handler().postDelayed({
                if (isListTypeCard)
                    setListTypeList()
                else setListTypeCard()
            }, 100)
        }

        fabLanguage.setOnClickListener {
            AppUtils.popDownUpAnimation(fabLanguage)
            Handler().postDelayed({
                LanguageListDialogFragment.newInstance().show(supportFragmentManager, "dialog")
            }, 100)
        }

        btnReload.setOnClickListener {
            AppUtils.popDownUpAnimation(btnReload)
            Handler().postDelayed({
                isCardEmpty = false
                val list = mutableListOf<String>()
                list.addAll(titleList)
                titleList.clear()
                cardListAdapter!!.notifyDataSetChanged()

                titleList.addAll(list)
                cardListAdapter!!.notifyDataSetChanged()

                AppUtils.popdownAnimation(llReload)
                AppUtils.popupAnimationLarge(cardStackView)
            }, 100)
        }
    }


    private fun showTutorialsShowcase() {
        val config = ShowcaseConfig()
        config.delay = 10

        val sequence = MaterialShowcaseSequence(activity, "1234567890")
        sequence.setConfig(config)
        sequence.addSequenceItem(
            fabLanguage,
            "Click here to change language", "GOT IT"
        )
        sequence.addSequenceItem(
            fabListType,
            "Click here to read diseases details in list", "GOT IT"
        )
        sequence.addSequenceItem(
            viewCardShowcase,
            "Swipe left or right to view diseases information", "GOT IT"
        )
        sequence.start()
        sequence.setOnItemDismissedListener { _, position ->
            if (position == 2) {
                getVersionDataFromFirebase()
            }
        }
        if (sequence.hasFired()) {
            getVersionDataFromFirebase()
        }
    }


    override fun onLanguageClicked(position: Int) {
        AppUtils.defaultLangPos = position
        titleList.clear()
        infoList.clear()
        cardListAdapter!!.notifyDataSetChanged()
        verticalListAdapter!!.notifyDataSetChanged()

        for ((index, value) in AppUtils.fileNameList.withIndex()) {
            readDataFromFile(value, index)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_rate_app -> {
                AppUtils.rateApp(activity)
                return true
            }
            R.id.action_share_app -> {
                AppUtils.shareApp(activity)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setListTypeCard() {
        width = recyclerView.measuredWidth
        height = recyclerView.measuredHeight

        val margin = (AppUtils.getScreenWidth(activity) / 2) -
                resources.getDimensionPixelSize(R.dimen.margin_88)

        fabLanguage.animate().setDuration(800).translationX(margin.toFloat()).start()
        fabListType.animate().setDuration(800).translationX(-margin.toFloat()).start()
        fabListType.setImageResource(R.drawable.ic_list_white_24dp)

        AppUtils.popdownAnimationLarge(recyclerView)
        Handler().postDelayed({
            if (isCardEmpty)
                AppUtils.popupAnimation(llReload)
            else AppUtils.popupAnimationLarge(cardStackView)
        }, 600)

        isListTypeCard = true
    }

    private fun setListTypeList() {
        fabLanguage.animate().setDuration(800).translationX(0F).start()
        fabListType.animate().setDuration(800).translationX(0F).start()
        fabListType.setImageResource(R.drawable.ic_card_white_24dp)

        if (isCardEmpty)
            AppUtils.popdownAnimation(llReload)
        else AppUtils.popdownAnimationLarge(cardStackView)
        Handler().postDelayed({ AppUtils.popupAnimationLarge(recyclerView) }, 600)

        isListTypeCard = false
    }


    private fun readDataFromFile(fileName: String, index: Int) {
        try {
            val fileNameFormatted = fileName.replace(" ", "_").replace("/", "_")

            val br = BufferedReader(
                InputStreamReader(
                    assets.open(
                        fileNameFormatted + "/" + fileNameFormatted + "_" +
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

            loop@ while (line != null) {
                line = line.trim()
                if (AppUtils.checkStringContainsSpecialChar(line)) {
                    specialString = line
                    line = br.readLine()
                    continue
                }
                if (!TextUtils.isEmpty(specialString)) {
                    when (specialString) {
                        AppConstants.TITLE_HINT -> {
                            if (!TextUtils.isEmpty(line))
                                titleList.add(index, line)
                        }
                        AppConstants.SUB_TITLE_HINT -> {
                            if (!TextUtils.isEmpty(subTitle) && !TextUtils.isEmpty(subTitleData.toString())) {
                                infoList.add(index, subTitleData.toString())
                                subTitleData = StringBuilder()
                                break@loop
                            }
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
                                subTitleData.append("\t").append("●  ").append(line)
                                    .append(lineBreak)
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
            if (!TextUtils.isEmpty(subTitleData.toString())
            ) infoList.add(index, subTitleData.toString())

            br.close()
        } catch (e: Exception) {
            e.printStackTrace()
            init()
        } finally {
            if (index == AppUtils.fileNameList.size - 1) {
                init()
            }
        }
    }

    fun callDetailActivity(position: Int) {
        if (position < 0 || position > titleList.size - 1)
            return
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(AppConstants.FILE_NAME, titleList[position])
        intent.putExtra(AppConstants.POSITION, position)
        activity.startActivity(intent)
    }


    private fun getVersionDataFromFirebase() {
        app.firestoreDB.collection(getString(R.string.VERSION_DB_NAME))
            .get()
            .addOnSuccessListener { result ->
                var minVersionCode: Long = BuildConfig.VERSION_CODE.toLong()
                for ((index, document) in result.withIndex()) {

                    val map = document.data
                    if (document.id == AppConstants.Android) {
                        if (map.contains(AppConstants.minVersionSupport))
                            minVersionCode = map[AppConstants.minVersionSupport] as Long
                    }

                    if (index == result.size() - 1) {
                        if (BuildConfig.VERSION_CODE < minVersionCode)
                            AppUtils.initNewUpdateDialog(activity)
                    }
                }
            }
    }



    private fun changeFabButtonColor(position: Int) {
        when (position % 5) {
            1 -> {
                fabListType.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.brown_dark))
                fabLanguage.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.brown_dark))

            }
            2 -> {
                fabListType.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.pink_dark_dark))
                fabLanguage.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.pink_dark_dark))

            }
            3 -> {
                fabListType.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.green_dark))
                fabLanguage.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.green_dark))

            }
            4 -> {
                fabListType.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            activity,
                            R.color.pink_light_dark
                        )
                    )
                fabLanguage.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            activity,
                            R.color.pink_light_dark
                        )
                    )

            }
            else -> {
                fabListType.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.navy_blue_dark))
                fabLanguage.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.navy_blue_dark))

            }
        }
    }


    private fun registerTimeReceiver() {

    }

    private fun unRegisterTimeReceiver() {
        try {

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    inner class SystemTimeTickReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            /*if (bannerAdUtils == null)
                bannerAdUtils = BannerAdUtils(activity, llAdContainer)
            if (bannerAdUtils!!.bannerAdStatus == BannerAdStatus.Error)
                bannerAdUtils!!.initFbBannerAd()*/
        }
    }

}