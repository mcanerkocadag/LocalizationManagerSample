package com.quiz.localizationlibrary.manager

import android.app.Application
import android.content.Context


class LocalizationApplicationDelegate(private val application: Application) {

    fun onConfigurationChanged(context: Context) {
        LocalizationUtility.applyLocalizationContext(context)
    }

    fun attachBaseContext(context: Context): Context {
        return LocalizationUtility.applyLocalizationContext(
            context
        )
    }

    fun getApplicationContext(applicationContext: Context): Context {
        return LocalizationUtility.applyLocalizationContext(
            applicationContext
        )
    }

    //    public static void onConfigurationChanged(Context context) {
    //        updateLocaleConfiguration(context);
    //    }

    //    public static Context updateLocaleConfiguration(Context baseContext) {
    //        return LocalizationUtility.applyLocalizationContext(baseContext);
    //    }

    //    public static Context attachBaseContext(Context context) {
    //        return updateLocaleConfiguration(context);
    //    }
}