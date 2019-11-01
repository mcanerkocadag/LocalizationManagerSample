package com.quiz.localizationlibrary.ui

import android.os.Bundle
import android.app.Activity
import android.os.Handler
import com.quiz.localizationlibrary.R


class BlankDummyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            R.anim.animation_localization_activity_transition_in,
            R.anim.animation_localization_activity_transition_out
        )
        setContentView(R.layout.activity_blank_dummy)

        delayedFinish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.animation_localization_activity_transition_in,
            R.anim.animation_localization_activity_transition_out
        )
    }

    private fun delayedFinish() {
        Handler().postDelayed({ finish() }, 200)
    }
}
