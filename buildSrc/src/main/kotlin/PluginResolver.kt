import org.bukkit.configuration.file.YamlConfiguration
import org.gradle.api.Project
import java.io.File
import java.io.InputStreamReader
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/25 17:26:24
 *
 * until-the-end/until-the-end.buildSrc.main/PluginResolver.kt
 */

object PluginResolver {
    fun ZipEntry.copy(): ZipEntry {
        val new = ZipEntry(this.name)
        this.comment?.apply { new.comment = this }
        this.lastAccessTime?.apply { new.lastAccessTime = this }
        this.lastAccessTime?.apply { new.lastModifiedTime = this }
        return new
    }

    private fun ClassNode.buildMessages() {
        val met = MethodNode(Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC,
                "forDecompiler", "()V", null, null
        )
        methods.add(0, met)
        fun String.insert() {
            met.visitLdcInsn(this)
            met.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String",
                    "isEmpty", "()Z", false)
            met.visitInsn(Opcodes.POP)
        }

        "Hello".insert()
        "".insert()
        "你在看这里吗?".insert()
        "你在这里看什么呢?".insert()
        "这个项目开源的哦...".insert()
        ".....".insert()
        "hum?".insert()
        "你还在看?".insert()
        "哇哦, 你就这么担心是不是有后门吗?".insert()
        "...".insert()
        ".......".insert()
        "还想看的话, 都可以的哦, 下次再见咯".insert()

        met.visitMaxs(2, 2)
        met.visitInsn(Opcodes.RETURN)
    }

    fun resolve(file: File, project: Project) {
        try {
            resolve0(file, project)
        } catch (any: Throwable) {
            any.printStackTrace()
            throw any
        }
    }

    fun resolve0(file: File, project: Project) {
        println("Target file $file")
        val output = File(project.projectDir, "dist")
        println("Dist dir: $output")
        println("Version: ${Versions.UTE}")
        val outputFile = File(output, "UntilTheEnd v${Versions.UTE}.jar")
        output.mkdirs()
        println("Target: $outputFile")
        Versions.resultFile = outputFile
        ZipOutputStream(RAFOutputStream(outputFile)).use { writer ->
            ZipFile(file).use { zip ->
                val plugin = zip.getEntry("plugin.yml")
                        ?: error("Cannot found `plugin.yml` in $file")
                val description = InputStreamReader(zip.getInputStream(plugin), "utf8").use {
                    YamlConfiguration.loadConfiguration(it)
                }
                description.set("version", Versions.UTE)
                val main = description.getString("main").replace(".", "/") + ".class"
                zip.entries().iterator().forEach { entry ->
                    if (entry.isDirectory)
                        writer.putNextEntry(entry)
                    else when (entry.name) {
                        "plugin.yml" -> {
                            writer.putNextEntry(entry.copy())
                            // Default is UTF8
                            writer.write(description.saveToString().toByteArray())
                        }
                        main -> {
                            val node = zip.getInputStream(entry).use { ClassReader(it) }
                                    .let { val node = ClassNode(); it.accept(node, 0); node }
                            writer.putNextEntry(entry.copy())

                            node.buildMessages()

                            ClassWriter(0).also { node.accept(it) }.toByteArray().apply { writer.write(this) }
                        }
                        else -> {
                            writer.putNextEntry(entry)
                            zip.getInputStream(entry).use { it.copyTo(writer) }
                        }
                    }
                }
            }
        }
    }
}
