package com.example.locale

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import com.example.locale.LanguageHelper.getUserLanguage


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUserLanguage(this).let {
            LanguageHelper.updateLanguage(this, it)
        }

        korean.setOnClickListener {
            LanguageHelper.storeUserLanguage(this, "ko")
            recreate()
        }
        english.setOnClickListener {
            LanguageHelper.storeUserLanguage(this, "en")
            recreate()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(newBase: Context?): Context? {
        newBase?.let { context ->
            var locale = Locale(getUserLanguage(context))
            Locale.setDefault(locale)
            var configuration = context.resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(locale)
                Log.d("BaseActivityV3 TAG", "BaseActivity attachBaseContext 1")
                return context.createConfigurationContext(configuration)
            } else {
                configuration.locale = locale
                context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
                Log.d("BaseActivityV3 TAG", "BaseActivity attachBaseContext 2")
            }
        }
        return newBase
    }

}

object LanguageHelper {

    private val GENERAL_STORAGE = "GENERAL_STORAGE"
    private val KEY_USER_LANGUAGE = "KEY_USER_LANGUAGE"

    fun updateLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val cfg = Configuration(res.configuration)
        cfg.locale = locale
        res.updateConfiguration(cfg, res.displayMetrics)
    }

    fun storeUserLanguage(context: Context, language: String) {
        context.getSharedPreferences(GENERAL_STORAGE, MODE_PRIVATE).edit().putString(KEY_USER_LANGUAGE, language).apply()
    }

    fun getUserLanguage(context: Context): String {
        return context.getSharedPreferences(GENERAL_STORAGE, MODE_PRIVATE).getString(KEY_USER_LANGUAGE, "en")
    }
}
