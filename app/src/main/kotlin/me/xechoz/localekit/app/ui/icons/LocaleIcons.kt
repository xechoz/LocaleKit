package me.xechoz.localekit.app.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

// 内联自 Lucide（ISC 许可证，https://lucide.dev），仅取本 App 用到的图标，不引整套图标依赖。
private fun strokeIcon(name: String, vararg paths: String): ImageVector {
    val builder = ImageVector.Builder(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        name = name,
    )
    paths.forEach { d ->
        builder.addPath(
            pathData = PathParser().parsePathString(d).toNodes(),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
    }
    return builder.build()
}

object LocaleIcons {

    val Languages: ImageVector by lazy {
        strokeIcon(
            "Languages",
            "M5 8l6 6",
            "M4 14l6 -6 2 -3",
            "M2 5h12",
            "M7 2h1",
            "M22 22l-5 -10 -5 10",
            "M14 18h6",
        )
    }

    val Globe: ImageVector by lazy {
        strokeIcon(
            "Globe",
            "M12 2a10 10 0 1 0 0 20a10 10 0 1 0 0 -20z",
            "M12 2a14.5 14.5 0 0 0 0 20a14.5 14.5 0 0 0 0 -20z",
            "M2 12h20",
        )
    }

    val List: ImageVector by lazy {
        strokeIcon(
            "List",
            "M3 12h.01",
            "M3 18h.01",
            "M3 6h.01",
            "M8 12h13",
            "M8 18h13",
            "M8 6h13",
        )
    }

    val Search: ImageVector by lazy {
        strokeIcon("Search", "M21 21l-4.34 -4.34", "M11 3a8 8 0 1 0 0 16a8 8 0 1 0 0 -16z")
    }

    val ChevronRight: ImageVector by lazy {
        strokeIcon("ChevronRight", "M9 18l6 -6 -6 -6")
    }
}
