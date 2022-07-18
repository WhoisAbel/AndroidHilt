package ir.whoisAbel.androidhilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/*
   1. If you don't have an application class, you need to create one.

   2. HILT must contain an Application class that is annotated
   with @HiltAndroidApp annotation. It will trigger Hilt’s code generation.

   3. Do not forget to add this application class in AndroidManifest.xml,
    you need to update android:name in the AndroidManifest.xml
*/
@HiltAndroidApp
class AndroidHiltApplication : Application() {
}