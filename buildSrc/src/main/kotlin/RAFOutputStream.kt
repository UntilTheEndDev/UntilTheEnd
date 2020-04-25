import java.io.File
import java.io.OutputStream
import java.io.RandomAccessFile

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/25 17:31:44
 *
 * until-the-end/until-the-end.buildSrc.main/RAFOutputStream.kt
 */

class RAFOutputStream(val raf: RandomAccessFile) : OutputStream() {
    constructor(file: File) : this(RandomAccessFile(file, "rw"))

    override fun write(b: Int) {
        raf.write(b)
    }

    override fun write(b: ByteArray) {
        raf.write(b)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        raf.write(b, off, len)
    }

    override fun close() {
        raf.setLength(raf.filePointer)
        raf.close()
    }
}