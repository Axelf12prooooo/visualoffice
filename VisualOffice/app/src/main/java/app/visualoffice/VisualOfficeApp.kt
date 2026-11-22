package app.visualoffice

import android.app.Application

class VisualOfficeApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
