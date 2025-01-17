package com.android.trade.presentation.ui.splash

import android.animation.Animator
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.android.trade.presentation.databinding.ActivitySplashBinding
import com.android.trade.presentation.ui.base.BaseActivity
import com.android.trade.presentation.ui.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding.lottieView.apply {
            playAnimation()
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}

                override fun onAnimationEnd(animator: Animator) {
                    navigateActivity(MainActivity::class.java)
                }

                override fun onAnimationCancel(animator: Animator) {}

                override fun onAnimationRepeat(animator: Animator) {}
            })
        }

    }

    override fun initializeBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)
}
