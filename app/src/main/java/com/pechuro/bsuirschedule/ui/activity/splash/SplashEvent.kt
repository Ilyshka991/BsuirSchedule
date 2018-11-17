package com.pechuro.bsuirschedule.ui.activity.splash

sealed class SplashEvent

object OpenNavigationActivity : SplashEvent()
object OpenInfoLoadActivity : SplashEvent()