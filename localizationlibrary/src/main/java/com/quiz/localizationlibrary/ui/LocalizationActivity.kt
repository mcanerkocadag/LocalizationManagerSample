package com.quiz.localizationlibrary.ui

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quiz.localizationlibrary.manager.LocalizationActivityDelegate
import com.quiz.localizationlibrary.manager.OnLocaleChangedListener
import java.util.*


abstract class LocalizationActivity : AppCompatActivity(),
    OnLocaleChangedListener {

    private val localizationDelegate =
        LocalizationActivityDelegate(this)

    val currentLanguage: Locale
        get() = localizationDelegate.getLanguage(this)

    public override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate(savedInstanceState!!)
        super.onCreate(savedInstanceState)
    }

    public override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
    }

    protected override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase))
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    fun setLanguage(language: String) {
        localizationDelegate.setLanguage(this, language)
    }

    fun setLanguage(locale: Locale) {
        localizationDelegate.setLanguage(this, locale)
    }

    fun setDefaultLanguage(language: String) {
        localizationDelegate.setDefaultLanguage(language)
    }

    fun setDefaultLanguage(locale: Locale) {
        localizationDelegate.setDefaultLanguage(locale)
    }

    // Just override method locale change event
    override fun onBeforeLocaleChanged() {}

    override fun onAfterLocaleChanged() {}
}