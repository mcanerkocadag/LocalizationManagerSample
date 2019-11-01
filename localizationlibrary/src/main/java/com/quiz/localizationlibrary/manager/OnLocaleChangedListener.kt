package com.quiz.localizationlibrary.manager


interface OnLocaleChangedListener {
    fun onBeforeLocaleChanged()

    fun onAfterLocaleChanged()
}
