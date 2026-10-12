package me.xechoz.localekit.app.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

data class InstalledApp(
    val packageName: String,
    val label: String,
    val isSystem: Boolean,
)

object InstalledApps {

    fun load(context: Context, includeSystem: Boolean): List<InstalledApp> {
        val pm = context.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolved = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(launcherIntent, PackageManager.ResolveInfoFlags.of(0L))
        } else {
            @Suppress("DEPRECATION")
            pm.queryIntentActivities(launcherIntent, 0)
        }
        return resolved.asSequence()
            .map { it.activityInfo.applicationInfo }
            .distinctBy { it.packageName }
            .filter { includeSystem || !it.isSystemApp }
            .map { InstalledApp(it.packageName, pm.getApplicationLabel(it).toString(), it.isSystemApp) }
            .sortedBy { it.label.lowercase() }
            .toList()
    }
}

private val ApplicationInfo.isSystemApp: Boolean
    get() = (flags and ApplicationInfo.FLAG_SYSTEM) != 0
