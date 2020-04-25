import org.gradle.api.Project
import java.io.File

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/25 17:04:55
 *
 * until-the-end/until-the-end.buildSrc.main/Versions.kt
 */

object Versions {
    var resultFile: File? = null
    var project: Project? = null
    val UTE: String
        get() {
            val pro = project ?: error("Project not set")
            val file = File(pro.projectDir, "UTEversion.txt")
            if (file.isFile) {
                return file.readText(Charsets.UTF_8)
            }
            error("Version info not found!")
        }
}