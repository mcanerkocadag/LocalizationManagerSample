package com.quiz.localizationlibrary.manager

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*


object LocalizationUtility {
    fun getLocaleFromConfiguration(configuration: Configuration): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.getLocales().get(0)
        } else {
            configuration.locale
        }
    }

    fun applyLocalizationContext(baseContext: Context): Context {
        val baseLocale =
            getLocaleFromConfiguration(
                baseContext.getResources().getConfiguration()
            )
        val currentLocale =
            LanguageSetting.getLanguage(baseContext)
        if (baseLocale.toString().toLowerCase() != currentLocale.toString().toLowerCase()) {
            val context = LocalizationContext(baseContext)
            val config = context.resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(currentLocale)
                val localeList = LocaleList(currentLocale)
                LocaleList.setDefault(localeList)
                config.setLocales(localeList)
                return context.createConfigurationContext(config)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(currentLocale)
                return context.createConfigurationContext(config)
            } else {
                return context
            }
        } else {
            return baseContext
        }
    }
}
