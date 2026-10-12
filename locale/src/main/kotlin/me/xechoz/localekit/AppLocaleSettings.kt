package me.xechoz.localekit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

/**
 * 其他应用的语言设置入口。
 *
 * v1 只做深链跳转（零权限）；v1.1 的 Shizuku 增强模块会新增独立的写入/读取 API，
 * 不改变此处签名。
 */
object AppLocaleSettings {

    /** 系统是否支持 per-app 语言（Android 13+）。 */
    val isSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    /**
     * 打开指定应用的系统语言设置页，返回是否成功打开。
     *
     * Android 13+ 深链 [Settings.ACTION_APP_LOCALE_SETTINGS]；无法解析时回退到该应用的
     * 详情页。13 以下返回 false（无 per-app 语言服务）。
     */
    fun open(context: Context, packageName: String): Boolean {
        if (isSupported && context.tryStartActivity(localePageIntent(packageName))) return true
        return context.tryStartActivity(appDetailsIntent(packageName))
    }

    private fun localePageIntent(packageName: String): Intent =
        Intent(Settings.ACTION_APP_LOCALE_SETTINGS).setData(packageUri(packageName))

    private fun appDetailsIntent(packageName: String): Intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(packageUri(packageName))

    private fun packageUri(packageName: String): Uri = Uri.fromParts("package", packageName, null)
}
