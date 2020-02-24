package com.devtides.animals

import android.app.Application
import com.devtides.animals.di.PrefsModule
import com.devtides.animals.util.SharePreferenceHelper

class PrefsModuleTest (val mockPrefs : SharePreferenceHelper) : PrefsModule(){

    override fun provideSharedPreferences(app: Application): SharePreferenceHelper {
        return mockPrefs
    }
}