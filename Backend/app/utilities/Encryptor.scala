package utilities

import java.io._
import scala.sys.process._


object Encryptor {
  private val executable = "e2e.exe"
  private val password = "doBt2505!"

  def encrypt(data: String, key: Int): String = {
    run(en = true, data, key)
  }

  def decrypt(data: String, key: Int): String = {
    run(en = false, data, key)
  }

  private def run(en: Boolean, data: String, key: Int): String = {
    val tempFile = createTempFileWithData(data)
    val fullPath = buildFullPath(tempFile.getPath, key, en)
    val result = executeProcess(fullPath)
    tempFile.delete()
    result
  }

  private def createTempFileWithData(data: String): File = {
    val tempFile = File.createTempFile("data", ".edc")
    val writer = new BufferedWriter(new FileWriter(tempFile))
    writer.write(data)
    writer.close()
    tempFile
  }

  private def buildFullPath(filePath: String, key: Int, en: Boolean): String = {
    Seq(executable, password, filePath, key.toString, if (en) "1" else "0").mkString(" ")
  }

  private def executeProcess(fullPath: String): String = {
    val result = new StringBuilder()
    try {
      val process = Process(fullPath.split("\\s+").toList).run(ProcessLogger(line => result.append(line)))
      val exitCode = process.exitValue()
      if (exitCode != 0) {
        return "Program Failed!"
      }
    } catch {
      case e: IOException =>
        e.printStackTrace()
      case e: InterruptedException =>
        e.printStackTrace()
    }
    result.toString()
  }
}