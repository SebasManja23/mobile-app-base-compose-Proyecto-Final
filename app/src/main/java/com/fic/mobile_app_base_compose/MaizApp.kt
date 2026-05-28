package com.fic.mobile_app_base_compose

import android.app.Application
import com.fic.mobile_app_base_compose.data.local.MaizDatabase

class MaizApp : Application() {
    val database: MaizDatabase by lazy { MaizDatabase.getDatabase(this) }
}