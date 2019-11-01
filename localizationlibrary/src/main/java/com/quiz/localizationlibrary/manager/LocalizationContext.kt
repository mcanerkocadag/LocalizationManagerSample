package com.quiz.localizationlibrary.manager

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources


class LocalizationContext(base: Context) : ContextWrapper(base) {

    override fun getResources(): Resources {
        val conf = super.getResources().configuration
        conf.locale = LanguageSetting.getLanguage(this)
        val metrics = super.getResources().displayMetrics
        return Resources(assets, metrics, conf)
    }
}