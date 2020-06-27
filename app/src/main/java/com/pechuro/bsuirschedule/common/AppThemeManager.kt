package com.pechuro.bsuirschedule.common

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.StyleRes
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.AppTheme
import com.pechuro.bsuirschedule.domain.interactor.GetAppTheme
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AppThemeManager @Inject constructor(
        private val getAppTheme: GetAppTheme,
        private val context: Context
) {

    fun applyToCurrentTheme(theme: Resources.Theme) {
        val themeStyleRes = getThemeStyleRes()
        theme.applyStyle(themeStyleRes, true)
    }

    @StyleRes
    private fun getThemeStyleRes() = runBlocking {
        when (getAppTheme.execute(BaseInteractor.NoParams).getOrDefault(AppTheme.DEFAULT)) {
            AppTheme.FOLLOW_SYSTEM -> {
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> R.style.LightStyleTheme
                    Configuration.UI_MODE_NIGHT_YES -> R.style.DarkStyleTheme
                    else -> R.style.DarkStyleTheme
                }
            }
            AppTheme.LIGHT -> R.style.LightStyleTheme
            AppTheme.DARK -> R.style.DarkStyleTheme
            AppTheme.BLACK -> R.style.BlackStyleTheme
            AppTheme.INDIGO -> R.style.IndigoStyleTheme
            AppTheme.TEAL -> R.style.TealStyleTheme
            AppTheme.BLUE_GREY -> R.style.BlueGreyStyleTheme
            AppTheme.BLUE_LIGHT -> R.style.BlueLightStyleTheme
            AppTheme.BROWN -> R.style.BrownStyleTheme
            AppTheme.ORANGE -> R.style.OrangeStyleTheme
            AppTheme.PURPLE -> R.style.PurpleStyleTheme
            AppTheme.CYAN -> R.style.CyanStyleTheme
            AppTheme.RED -> R.style.RedStyleTheme
            AppTheme.LIME -> R.style.LimeStyleTheme
        }
    }
}