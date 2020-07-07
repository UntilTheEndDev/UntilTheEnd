buildscript {
    repositories {
        mavenLocal()
        maven(url = "https://mirrors.huaweicloud.com/repository/maven")
        jcenter()
    }
}

plugins {
    java
}

Versions.project = project()

group = "io.github.untiltheend"
version = Versions.UTE

repositories {
    mavenLocal()
//    flatDir {
//        dir("libs")
//    }
    if (System.getenv("is_github")?.toBoolean() != true)
        maven(url = "https://mirrors.huaweicloud.com/repository/maven")
    maven(url = "https://repo.codemc.org/repository/maven-public")
    // SpigotMC
    maven(url = "https://hub.spigotmc.org/nexus/content/groups/public")
    // Placeholder API
    maven(url = "http://repo.extendedclip.com/content/repositories/placeholderapi/")
    // Citizens2
    maven(url = "http://repo.citizensnpcs.co/")
    jcenter()
}

dependencies {
    // testCompile("junit", "junit", "4.12")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    // For NMS
    // compileOnly("org.bukkit:craftbukkit:1.12.2-R0.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.10.5")
    compileOnly("org.jetbrains:annotations:17.0.0")
//    compileOnly("LibsDisguises:LibsDisguises:10.0.5")
//    compileOnly("Residence:Residence:4.8.7.4")
    compileOnly(fileTree("libs").include("*.jar"))
    compileOnly("org.apache.logging.log4j:log4j-api:2.13.2")
    compileOnly("net.citizensnpcs:citizens:2.0.26-SNAPSHOT")
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
    // compileOnly("net.citizensnpcs:citizensapi:2.0.26-SNAPSHOT")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
// UTF8
tasks.withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
}
val buildAll: Task by tasks.creating {
    group = "ute"
    dependsOn("build")

    doFirst {
        val out = File(projectDir, "build/libs").walk()
                .filter { it.isFile }
                .filter { it.name == "until-the-end-${Versions.UTE}.jar" }
                .firstOrNull() ?: error("Result not found!")
        PluginResolver.resolve(out, project())

    }
}

val releases: Task by tasks.creating {
    group = "ute"
    dependsOn("buildAll")
    doFirst {
        val file = Versions.resultFile ?: error("Cannot found release jar.")
        GitHub.upload(file, "https://api.github.com/repos/UntilTheEndDev/UntilTheEndReleases/contents/shadow/${project.name}/${file.name}", project())
        Gitee.upload(file)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Project.project(): Project = this

val uteGradleTest: Task by tasks.creating {
    group = "ute"
    doFirst {
        println(Versions.buildTime)
        println(Versions.UTE)
        println(Versions.commit)
        println(Versions.format.format(Versions.buildDate))
    }
}

tasks.getByName("jar", Jar::class) {

    @Suppress("UnstableApiUsage")
    manifest {
        attributes(mapOf(
                "Manifest-Version" to "1.0",
                "Implementation-URL" to "https://github.com/UntilTheEndDev/UntilTheEnd",
                "Implementation-Title" to "UntilTheEnd",
                "Implementation-Version" to Versions.commit,
                "Created-By" to "GitHub Action",
                "BuildTimestamp" to Versions.buildDate.time,
                "BuildDate" to Versions.buildString,
                "Application-Version" to Versions.UTE
        ))
    }
}
