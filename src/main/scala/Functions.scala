package org.ekrich.unicode

import scala.io.Source
import java.io.InputStream

object Functions {

  def parseVersion(args: Array[String], default: String = "7.0.0"): String =
    if (args.isEmpty) default else args(0)

  def resourcePath(version: String, filename: String): String = {
    val path = s"/${version}/UCD/${filename}"
    println(s"Path: $path")
    path
  }

  def readLines(path: String): Iterator[String] = {
    val stream: InputStream = getClass.getResourceAsStream(path)
    scala.io.Source.fromInputStream(stream).getLines // 27268
  }

  def recordLines(it: Iterator[String]): Iterator[String] =
    it.filterNot(line => line.startsWith("#") || line.isEmpty())

  def toInt(hex: String): Int = Integer.parseInt(hex, 16)

  def num[T](list: List[T], comment: String = "Num"): Unit = {
    println(s"${comment}: ${list.size}")
  }

  def show[T](list: List[T]): Unit = {
    println(s"Num: ${list.size}")
    val uline = "-" * 20
    println(uline)
    list.foreach(println(_))
    println(uline)
  }
}
