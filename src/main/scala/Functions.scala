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

  def recordArrays(recordLines: List[String],
                   limit: Int): List[Array[String]] =
    recordLines.map(x => x.split(";", limit))

  def toInt(hex: String): Int = Integer.parseInt(hex, 16)

  def num[T](list: List[T], comment: String = "Num"): Unit = {
    println(s"${comment}: ${list.size}")
  }

  def show[T](list: List[T], comment: String = "Num"): Unit = {
    println(s"${comment}: ${list.size}")
    val uline = "-" * 20
    println(uline)
    list.foreach(println(_))
    println(uline)
  }

  def printIndented(list: List[(Int, Int, Int, Int)], label: String) = {
    println(s"$label indented list")
    list.foreach(f => if (f._4 == 0) print("\n" + f) else print(f)); println
  }

  // specific transforms

  /** Add diff to tuple
   *  0 start of range or individual cp
   *  1 cp distance
   *  2 cp distance (means alternating)
   */
  def addDiff(arr: List[(Int, Int, Int)]): List[(Int, Int, Int, Int)] = {
    arr
      .foldLeft[List[(Int, Int, Int, Int)]](Nil) {
        case (x :: xs, elem) if (x._3 == elem._3) => {
          val diff = elem._1 - x._1
          // only ranges with diff of 1 or 2 matter
          // some ranges with 2 are only 2 long
          if (diff <= 2) (elem._1, elem._2, elem._3, diff) :: x :: xs
          else (elem._1, elem._2, elem._3, 0) :: x :: xs
        }
        case (list, elem) => (elem._1, elem._2, elem._3, 0) :: list
      }
      .reverse
  }

  /** Adjust the start for cases when the code point difference
   * is the same but a new sequence needs to be started because
   * the code point distance is different.
   * Only 2 cases:
   *
   * ((248,216,32,2),(248,216,32,0))
   * ((11559,4295,7264,2),(11559,4295,7264,0))
   *
   * Example
   * ((245,213,32,1),(245,213,32,1))
   * ((246,214,32,1),(246,214,32,1))
   * ((248,216,32,2),(248,216,32,0))
   * ((249,217,32,1),(249,217,32,1))
   * ((250,218,32,1),(250,218,32,1))
   */
  def adjustStart(
      arr: List[(Int, Int, Int, Int)]): List[(Int, Int, Int, Int)] = {
    arr.tail
      .foldLeft[List[(Int, Int, Int, Int)]](List(arr.head)) {
        case (x :: xs, elem) if (x._4 == 0 || x._4 == elem._4) =>
          elem :: x :: xs
        case (list, elem) => (elem._1, elem._2, elem._3, 0) :: list
      }
      .reverse
  }

  def compressRanges(
      arr: List[(Int, Int, Int, Int)]): List[(Int, Int, Int, Int)] = {
    import scala.annotation.tailrec
    val h    = arr.head
    val tail = arr.tail
    @tailrec
    def process(list: List[(Int, Int, Int, Int)],
                acc: List[(Int, Int, Int, Int)],
                prev: (Int, Int, Int, Int)): List[(Int, Int, Int, Int)] = {
      list match {
        case Nil => acc
        case x :: xs =>
          if (x._4 == 0 && x._4 != prev._4) process(xs, x :: prev :: acc, x)
          else if (x._4 == 0 || xs == Nil)
            process(xs, x :: acc, x) // Nil to get last element
          else process(xs, acc, x)
      }
    }
    process(tail, List(h), h).reverse
  }

  def printOutput(ul: String, list: List[(Int, Int, Int, Int)]): Unit = {
    println()
    println(
      list
        .map(_._1)
        .mkString(s"private[this] lazy val ${ul}Ranges = Array[scala.Int](",
                  ", ",
                  ")\n"))
    println(
      list
        .map(_._3)
        .mkString(s"private[this] lazy val ${ul}Deltas = Array[scala.Int](",
                  ", ",
                  ")\n"))
    println(
      list
        .map(_._4)
        .mkString(s"private[this] lazy val ${ul}Steps = Array[scala.Byte](",
                  ", ",
                  ")\n"))
  }

  def processCase(ul: String,
                  tuple2: List[(Int, Int)]): List[(Int, Int, Int, Int)] = {
    num(tuple2, s"$ul tuple2")
    // tp = to point (uc or lc)
    val tuple3 = tuple2.map { case (cp, tp) => (cp, tp, cp - tp) }
    num(tuple3, s"$ul tuple3")
    val diffTuple4 = addDiff(tuple3)
    num(diffTuple4, s"$ul diff tuple4")
    val adjTuple4 = adjustStart(diffTuple4)
    num(adjTuple4, s"$ul adjust tuple4")
    // show adjustStart result
    //val z = diffTuple4.zip(adjTuple4)
    //val zf = z.filter(p => p._1._4 != p._2._4)
    //show(zf, "diff, adj")
    val compTuple4 = compressRanges(adjTuple4)
    num(compTuple4, s"$ul compress ranges")
    printOutput(ul, compTuple4)
    compTuple4
  }
}
