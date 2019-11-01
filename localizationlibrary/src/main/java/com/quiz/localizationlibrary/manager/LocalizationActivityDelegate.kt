package com.quiz.localizationlibrary.manager

import android.content.Intent
import android.os.Build
import android.os.LocaleList
import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import com.quiz.localizationlibrary.ui.BlankDummyActivity
import java.util.*


open class LocalizationActivityDelegate(private val activity: Activity) {

    // Boolean flag to check that activity was recreated from locale changed.
    private var isLocalizationChanged = false

    // Prepare default language.
    private var currentLanguage =
        LanguageSetting.defaultLanguage
    private val localeChangedListeners = arrayListOf<OnLocaleChangedListener>()

    fun addOnLocaleChangedListener(onLocaleChangedListener: OnLocaleChangedListener) {
        localeChangedListeners.add(onLocaleChangedListener)
    }

    fun onCreate(savedInstanceState: Bundle) {
        setupLanguage()
        checkBeforeLocaleChanging()
    }

    // If activity is run to back stack. So we have to check if this activity is resume working.
    fun onResume(context: Context) {
        Handler().post {
            checkLocaleChange(context)
            checkAfterLocaleChanging()
        }
    }

    fun attachBaseContext(context: Context): Context {
        val locale = LanguageSetting.getLanguage(context)
        val config = context.getResources().getConfiguration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
            return context.createConfigurationContext(config)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        } else {
            return context
        }
    }

    fun getApplicationContext(applicationContext: Context): Context {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            LocalizationUtility.applyLocalizationContext(
                applicationContext
            )
        } else {
            applicationContext
        }
    }

    fun getResources(resources: Resources): Resources {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            val config = resources.getConfiguration()
            config.locale =
                LanguageSetting.getLanguage(activity)
            val metrics = resources.getDisplayMetrics()
            return Resources(activity.assets, metrics, config)
        } else {
            return resources
        }
    }

    // Provide method to set application language by country name.
    fun setLanguage(context: Context, language: String) {
        val locale = Locale(language)
        setLanguage(context, locale)
    }

    fun setLanguage(context: Context, language: String, country: String) {
        val locale = Locale(language, country)
        setLanguage(context, locale)
    }

    fun setLanguage(context: Context, locale: Locale) {
        if (!isCurrentLanguageSetting(context, locale)) {
            LanguageSetting.setLanguage(activity, locale)
            notifyLanguageChanged()
        }
    }

    fun setDefaultLanguage(language: String) {
        val locale = Locale(language)
        setDefaultLanguage(locale)
    }

    fun setDefaultLanguage(language: String, country: String) {
        val locale = Locale(language, country)
        setDefaultLanguage(locale)
    }

    fun setDefaultLanguage(locale: Locale) {
        LanguageSetting.defaultLanguage = locale
    }

    // Get current language
    fun getLanguage(context: Context): Locale {
        return LanguageSetting.getLanguage(context)
    }

    // Check that bundle come from locale change.
    // If yes, bundle will obe remove and set boolean flag to "true".
    private fun checkBeforeLocaleChanging() {
        val isLocalizationChanged =
            activity.intent.getBooleanExtra(KEY_ACTIVITY_LOCALE_CHANGED, false)
        if (isLocalizationChanged) {
            this.isLocalizationChanged = true
            activity.intent.removeExtra(KEY_ACTIVITY_LOCALE_CHANGED)
        }
    }

    // Setup language to locale and language preference.
    // This method will called before onCreate.
    private fun setupLanguage() {
        val locale = LanguageSetting.getLanguage(activity)
        setupLocale(locale)
        currentLanguage = locale
        LanguageSetting.setLanguage(activity, locale)
    }

    // Set locale configuration.
    private fun setupLocale(locale: Locale) {
        updateLocaleConfiguration(activity, locale)
    }


    private fun updateLocaleConfiguration(context: Context, locale: Locale) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val config = context.getResources().getConfiguration()
            config.locale = locale
            val dm = context.getResources().getDisplayMetrics()
            context.getResources().updateConfiguration(config, dm)
        }
    }

    // Avoid duplicated setup
    private fun isCurrentLanguageSetting(context: Context, locale: Locale): Boolean {
        return locale.toString().equals(
            LanguageSetting.getLanguage(
                context
            ).toString())
    }

    // Let's take it change! (Using recreate method that available on API 11 or more.
    private fun notifyLanguageChanged() {
        sendOnBeforeLocaleChangedEvent()
        activity.intent.putExtra(KEY_ACTIVITY_LOCALE_CHANGED, true)
        callDummyActivity()
        activity.recreate()
    }

    // Check if locale has change while this activity was run to back stack.
    private fun checkLocaleChange(context: Context) {
        if (!isCurrentLanguageSetting(context, currentLanguage)) {
            sendOnBeforeLocaleChangedEvent()
            isLocalizationChanged = true
            callDummyActivity()
            activity.recreate()
        }
    }

    // Call override method if local is really changed
    private fun checkAfterLocaleChanging() {
        if (isLocalizationChanged) {
            sendOnAfterLocaleChangedEvent()
            isLocalizationChanged = false
        }
    }

    private fun sendOnBeforeLocaleChangedEvent() {
        for (changedListener in localeChangedListeners) {
            changedListener.onBeforeLocaleChanged()
        }
    }

    private fun sendOnAfterLocaleChangedEvent() {
        for (listener in localeChangedListeners) {
            listener.onAfterLocaleChanged()
        }
    }

    private fun callDummyActivity() {
        activity.startActivity(Intent(activity, BlankDummyActivity::class.java))
    }

    companion object {
        private val KEY_ACTIVITY_LOCALE_CHANGED = "activity_locale_changed"
    }
}
