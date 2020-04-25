import io.ktor.client.request.header
import io.ktor.client.request.post
import kotlinx.coroutines.runBlocking
import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate
import java.io.File
import java.util.*

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/25 22:09:06
 *
 * until-the-end/until-the-end.buildSrc.main/Gitee.kt
 */

object Gitee {

    private fun getGiteeToken(): String {

        System.getProperty("gitee_token", null)?.let {
            return@getGiteeToken it.trim()
        }
        System.getenv("gitee_token")?.let { return@getGiteeToken it.trim() }
        error("Cannot find gitee token, " +
                "please specify by providing JVM parameter 'github_token'"
        )
    }

    fun upload(file: File) = runBlocking {
        val target = "https://gitee.com/api/v5/repos/Karlatemp-bot/UntilTheEndReleases/contents/releases/${file.name}"
        Http.post<String>(target) {
            header("Content-Type", "application/json;charset=UTF-8")
            body = json {
                obj {
                    "access_token" value getGiteeToken()
                    "content" value String(Base64.getEncoder().encode(file.readBytes()))
                    "message" value "automatically upload on release"
                    "branch" value "master"
                }
            }
        }
    }
}