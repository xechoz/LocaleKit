# LocaleKit

Android 语言切换工具库 + 示例工具 App。

解决两件事：**切换系统语言**、**切换 App 语言**。

## 功能矩阵

| 功能 | Android 13+ (API 33+) | Android 7.0 – 12 (API 24–32) |
|---|---|---|
| 本应用语言（跟随系统 / 单独指定） | ✅ 且同步进系统「应用语言」设置 | ✅ AppCompat 自动持久化 |
| 系统语言（查看 + 跳转设置页） | ✅ | ✅ |
| 其他 App 的语言入口 | ✅ 深链系统该 App 的语言页 | ❌ 系统无此能力 |

## 模块结构

```
LocaleKit/
├── locale/   # :locale 核心库（仅依赖 appcompat，无第三方）
└── app/      # :app 示例工具 App（Compose + Material3）
```

## 库接入（`:locale`）

### 1. 添加依赖

```kotlin
implementation(project(":locale"))
```

### 2. 声明支持的语言

`app/src/main/res/xml/locales_config.xml`：

```xml
<locale-config xmlns:android="http://schemas.android.com/apk/res/android">
    <locale android:name="zh-CN" />
    <locale android:name="en" />
</locale-config>
```

在 `AndroidManifest.xml` 的 `<application>` 上引用：

```xml
android:localeConfig="@xml/locales_config"
```

> 未声明 `localeConfig` 时，Android 13+ 的系统「应用语言」列表不会出现你的 App。

### 3. 宿主 Activity 约束

必须继承 `androidx.appcompat.app.AppCompatActivity`（`AppCompatDelegate` 官方要求）。

> 库已通过 manifest 合并自动注入 Android 12 及以下所需的
> `AppLocalesMetadataHolderService`（`autoStoreLocales=true`），无需任何初始化代码。

### API

```kotlin
// 本应用语言
sealed interface AppLanguagePreference {
    data object FollowSystem : AppLanguagePreference
    data class Explicit(val locale: Locale) : AppLanguagePreference
}

object AppLanguage {
    fun current(): AppLanguagePreference
    fun set(preference: AppLanguagePreference)          // 主线程调用；≤32 会重建 Activity
    fun available(context: Context): List<LanguageOption>  // 读 locales_config
}

// 系统语言（只读 + 跳转；不提供程序化修改）
object SystemLanguage {
    fun current(context: Context): List<Locale>          // 首个为主语言
    fun displayName(locale: Locale): String              // 用当前 App 语言渲染
    fun openSettings(context: Context): Boolean          // 跳系统语言设置页
}

// 其他 App 的语言入口（Android 13+）
object AppLocaleSettings {
    val isSupported: Boolean
    fun open(context: Context, packageName: String): Boolean
}

// 常用语言数据源
object CommonLocales {
    val all: List<Locale>
}
```

示例：

```kotlin
AppLanguage.set(AppLanguagePreference.FollowSystem)
AppLanguage.set(AppLanguagePreference.Explicit(Locale.forLanguageTag("zh-CN")))

val options = AppLanguage.available(context)

if (AppLocaleSettings.isSupported) {
    AppLocaleSettings.open(context, "com.tencent.mm")
}
```

### 为什么系统语言只做跳转

Android 不允许普通应用修改系统语言：唯一可靠的公开途径不存在，常见做法（
Shizuku / Root / 无障碍自动化）要么需额外授权，要么脆弱。本库 v1 明确不做程序化修改，
只提供「读取当前系统语言 + 跳转系统语言设置页」。

## App 使用

- **本应用语言**：跟随系统或选择本 App 支持的语言，即时生效。
- **系统语言**：查看当前系统语言，一键跳转系统语言设置页。
- **应用语言**：搜索/浏览已安装应用（可含系统应用），点进系统该 App 的语言页设置。

## 构建

```bash
export JAVA_HOME=<JDK 17+>
./gradlew :app:assembleDebug
./gradlew test lint
```

## 已知边界 / 路线图

- **v1.1（规划）**：新增独立模块 `:locale-shizuku`，经 Shizuku 提供「为未声明
  `localeConfig` 的 App 强制设置语言」「读取/批量管理其他 App 的语言」，不改变现有 API。
- 未声明 `localeConfig` 的 App：系统语言页可能不显示语言项（v1.1 可强制覆盖）。
- 部分 OEM 的语言设置页组件名不同：库已多级探测 + 回退设置首页。
- 其他 App 的 per-app 语言仅 Android 13+ 提供系统服务。

## 许可证

图标取自 [Lucide](https://lucide.dev)（ISC 许可证），仅按需内联本项目用到的图标。
