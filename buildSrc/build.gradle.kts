plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    maven(url = "https://mirrors.huaweicloud.com/repository/maven")
    // SpigotMC
    maven(url = "https://hub.spigotmc.org/nexus/content/groups/public")
    jcenter()
}

kotlin {
    sourceSets.all {
        languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
    }
}

dependencies {
    fun kotlinx(id: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$id:$version"
    api("org.jsoup:jsoup:1.12.1")
    api("org.ow2.asm:asm:7.2")
    api("org.ow2.asm:asm-tree:7.2")
    api("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT") // YAML
    api("com.google.code.gson:gson:2.8.6")
    api(kotlinx("coroutines-core", "1.3.3"))
}