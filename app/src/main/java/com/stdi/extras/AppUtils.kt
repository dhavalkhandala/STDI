package com.stdi.extras

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.stdi.R
import com.stdi.model.LanguageModel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    @JvmStatic
    val TAG = "STD"

    @JvmStatic
    val dateFormat_ddMM_HHmmss = SimpleDateFormat("dd-MM HH:mm:ss", Locale.ENGLISH)

    var defaultLangPos: Int = 4

    @JvmStatic
    val fileNameList: MutableList<String> = mutableListOf(
        "Introduction",
        "Bacterial_Vaginosis",
        "Chlamydia",
        "Gonorrhea",
        "Trichomoniasis",
        "Chancroid",
        "Lymphogranuloma_Venereum_(LGV)",
        "Scabies",
        "Syphilis",
        "HIV_AIDs",
        "Genital Herpes",
        "Human_Papillomavirus_and_Genital_Warts",
        "Hepatitis_B",
        "Hepatitis_C",
        "Pubic_Lice_(Crabs)",
        "Candidiasis_(Yeast_Infection)",
        "Molluscum_Contagiosum",
        "Herpes_Simplex_Virus",
        "Pelvic_inflammatory_disease"
    )

    @JvmStatic
    val languageList: MutableList<LanguageModel> = mutableListOf(
        LanguageModel("Arabic", "AR"),
        LanguageModel("Bengali", "BN"),
        LanguageModel("Chinese", "ZN"),
        LanguageModel("Dutch", "NL"),
        LanguageModel("English", "EN"),
        LanguageModel("French", "FR"),
        LanguageModel("German", "DE"),
        LanguageModel("Gujarati", "GU"),
        LanguageModel("Hindi", "HI"),
        LanguageModel("Italian", "IT"),
        LanguageModel("Japanese", "JA"),
        LanguageModel("Kannada", "KN"),
        LanguageModel("Korean", "KO"),
        LanguageModel("Malayalam", "ML"),
        LanguageModel("Marathi", "MR"),
        LanguageModel("Persian", "FA"),
        LanguageModel("Portuguese", "PT"),
        LanguageModel("Punjabi", "PA"),
        LanguageModel("Russian", "RU"),
        LanguageModel("Sindhi", "SD"),
        LanguageModel("Spanish", "ES"),
        LanguageModel("Tamil", "TA"),
        LanguageModel("Telugu", "TE"),
        LanguageModel("Urdu", "UR")
    )




    @JvmStatic
    fun getScreenWidth(activity: Activity): Int {
        return activity.resources?.displayMetrics?.widthPixels ?: 0
    }

    @JvmStatic
    fun getScreenHeight(activity: Activity): Int {
        return activity.resources?.displayMetrics?.heightPixels ?: 0
    }

    @JvmStatic
    @SuppressLint("PackageManagerGetSignatures")
    fun getKeyHash(context: Context) {
        val info: PackageInfo
        try {
            info = context.getPackageManager().getPackageInfo(
                context.getPackageName(),
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("hashkey", something)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }

    }

    @JvmStatic
    fun adjustFontScale(context: Context) {
        if (context.resources.configuration.fontScale != 1f) {
            context.resources.configuration.fontScale = 1.toFloat()
            val metrics = context.resources.displayMetrics
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = context.resources.configuration.fontScale * metrics.density
            context.resources.updateConfiguration(context.resources.configuration, metrics)
        }
    }

    @JvmStatic
    fun shareApp(activity: Activity) {
        val text = "Download this app to get information about Sexually Transmitted Diseases\n\n" +
                "http://play.google.com/store/apps/details?id=" + activity.packageName

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        activity.startActivity(sendIntent)
    }

    @JvmStatic
    fun rateApp(activity: Activity) {
        val uri = Uri.parse("market://details?id=" + activity.packageName)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "http://play.google.com/store/apps/details?id="
                                + activity.packageName
                    )
                )
            )
        }

    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    @JvmStatic
    fun toastShort(context: Context, message: String) {
        if (TextUtils.isEmpty(message))
            return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun toastLong(context: Context, message: String) {
        if (TextUtils.isEmpty(message))
            return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun log(message: String) {
        if (TextUtils.isEmpty(message))
            return
        Log.e(TAG, message)
    }


    //----------------------------------------------------------------------------------------------
    //--------------------------------------Call Activity-------------------------------------------
    //----------------------------------------------------------------------------------------------
    @JvmStatic
    fun callActivity(activity: Activity, classActivity: Class<*>) {
        callActivityIsFinish(activity, classActivity, false)
    }

    @JvmStatic
    fun callActivityWithFinish(activity: Activity, classActivity: Class<*>) {
        callActivityIsFinish(activity, classActivity, true)
    }

    @JvmStatic
    fun callActivityIsFinish(activity: Activity?, classActivity: Class<*>?, isFinish: Boolean) {
        if (activity == null || classActivity == null)
            return
        val intent = Intent(activity, classActivity)
        activity.startActivity(intent)
        if (isFinish)
            activity.finish()
    }


    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    @JvmStatic
    fun checkStringContainsSpecialChar(line: String): Boolean {
        if (TextUtils.isEmpty(line))
            return false
        if (line.equals(AppConstants.TITLE_HINT) || line.equals(AppConstants.SUB_TITLE_HINT) ||
            line.equals(AppConstants.SUB_SUB_TITLE_HINT) || line.equals(AppConstants.LIST_HINT) ||
            line.equals(AppConstants.SUB_LIST_HINT)
        ) {
            return true
        }
        return false
    }

    @JvmStatic
    fun getCurrentUTCTime(): String {
        dateFormat_ddMM_HHmmss.setTimeZone(TimeZone.getTimeZone("gmt"))
        return dateFormat_ddMM_HHmmss.format(Date())
    }


    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    fun initNewUpdateDialog(activity: Activity) {
        val dialog = AppCompatDialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setContentView(R.layout.dialog_new_update)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.findViewById<AppCompatButton>(R.id.btnUpdate)!!.setOnClickListener {
            dismissDialog(dialog)
            rateApp(activity)
        }
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        showDialog(dialog)
    }

    fun showDialog(dialog: AppCompatDialog?) {
        Handler(Looper.getMainLooper()).post {
            if (dialog != null && !dialog.isShowing)
                dialog.show()
        }
    }

    fun dismissDialog(dialog: AppCompatDialog?) {
        Handler(Looper.getMainLooper()).post {
            if (dialog != null && dialog.isShowing)
                dialog.dismiss()
        }
    }


    //----------------------------------------------------------------------------------------------
    //----------------------------------------Animations--------------------------------------------
    //----------------------------------------------------------------------------------------------
    @JvmStatic
    fun popupAnimation(view: View) {
        if (view.visibility == View.VISIBLE)
            return
        view.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.popup)
        view.startAnimation(animation)
    }

    @JvmStatic
    fun popdownAnimation(view: View) {
        if (view.visibility != View.VISIBLE)
            return
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.popdown)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        view.startAnimation(animation)
    }

    @JvmStatic
    fun popupAnimationLarge(view: View) {
        if (view.visibility == View.VISIBLE)
            return
        view.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.popup_large)
        view.startAnimation(animation)
    }

    @JvmStatic
    fun popdownAnimationLarge(view: View) {
        if (view.visibility != View.VISIBLE)
            return
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.popdown_large)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        view.startAnimation(animation)
    }

    @JvmStatic
    fun popDownUpAnimation(view: View) {
        if (view.visibility != View.VISIBLE)
            return
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.pop_down_up)
        view.startAnimation(animation)
    }


    fun changeToolbarStatusBarColor(activity: Activity, toolbar: Toolbar, position: Int) {
        when (position % 5) {
            1 -> {
                toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.brown_dark))
                setNavigationBarColor(activity, R.color.brown_light)
//                setNavigationBarColor(activity, R.color.brown_dark)
                setStatusBarColor(activity, R.color.brown_light)
            }
            2 -> {
                toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.pink_dark_dark))
                setNavigationBarColor(activity, R.color.pink_dark_light)
//                setNavigationBarColor(activity, R.color.pink_dark_dark)
                setStatusBarColor(activity, R.color.pink_dark_light)
            }
            3 -> {
                toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.green_dark))
                setNavigationBarColor(activity, R.color.green_light)
//                setNavigationBarColor(activity, R.color.green_dark)
                setStatusBarColor(activity, R.color.green_light)
            }
            4 -> {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.pink_light_dark
                    )
                )
                setNavigationBarColor(activity, R.color.pink_light_light)
//                setNavigationBarColor(activity, R.color.pink_light_dark)
                setStatusBarColor(activity, R.color.pink_light_light)
            }
            else -> {
                toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.navy_blue_dark))
                setNavigationBarColor(activity, R.color.navy_blue_light)
//                setNavigationBarColor(activity, R.color.navy_blue_dark)
                setStatusBarColor(activity, R.color.navy_blue_light)
            }
        }
    }

    private fun setStatusBarColor(activity: Activity, colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = ContextCompat.getColor(activity, colorId)
        }
    }

    private fun setNavigationBarColor(activity: Activity, colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.navigationBarColor = ContextCompat.getColor(activity, colorId)
        }
    }

}