import org.gradle.api.Project
import java.io.File
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/25 17:04:55
 *
 * until-the-end/until-the-end.buildSrc.main/Versions.kt
 */

@Suppress("MemberVisibilityCanBePrivate")
object Versions {
    var resultFile: File? = null
    var project: Project? = null
    val UTE: String
        get() {
            val pro = project ?: error("Project not set")
            val file = File(pro.projectDir, "dev-version.txt")
            if (file.isFile) {
                return file.readText(Charsets.UTF_8).trim()
            }
            error("Version info not found!")
        }

    val commit by lazy {
        kotlin.runCatching {
            Runtime.getRuntime()
                    .exec("git rev-parse HEAD")
                    .apply { waitFor() }
                    .inputStream
                    .reader(Charsets.UTF_8)
                    .use { it.readText() }
        }.getOrElse { error ->
            error.printStackTrace()
            "ERROR: $error"
        }
    }
    private val zone = TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"))!!

    val buildDate = Date()

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss '[Asia/Shanghai]'").apply {
        this.timeZone = zone
    }

    val buildTime = buildDate.toInstant()
            .atZone(zone.toZoneId())!!

    val buildString = format.format(buildDate)!!
}