package org.ekrich.unicode

/**
 * Legacy code using casefolding for upper lower case.
 * CaseFolding could be added as an additional library
 * so this could bea starting point.
 * Most code is now shared. Use "run 7.0.0" to get the
 * same results as the first version or upper/lower
 * case in Scala Native.
 */
object CaseFolding {

  import scala.io.Source
  import java.io.InputStream
  import Functions._

  def main(args: Array[String]): Unit = {
    val version  = parseVersion(args)
    val path     = resourcePath(version, "CaseFolding.txt")
    val allLines = readLines(path)
    val lines    = recordLines(allLines).toList
    num(lines, "Total Records")

    val arrays = recordArrays(lines, 4)

    val tuples = arrays.map { c =>
      (c(0), c(1).trim(), c(2).trim, c(3).trim)
    }
    // filter for 'simple case folding C + S' and greater than FFFF? && (t._1.size == 4)
    val fTuples = tuples.filter(t => (t._2 == "C" || t._2 == "S")).toList
    // for test cases as hex
    //println(s"All:\n${fTuples.mkString("\n")}\n")
    val upperLower = fTuples.map(t => (toInt(t._1), toInt(t._3)))
    // for test cases as ints
    //println(s"All:\n${upperLower.mkString("\n")}\n")
    val pairs = upperLower.map { case (u, l) => (u, l, l - u) }

    def addDiffLower(arr: List[(Int, Int, Int)]) = {
      arr
        .foldLeft[List[(Int, Int, Int, Int)]](Nil) {
          case (x :: xs, elem) if (x._3 == elem._3) => {
            val diff = elem._2 - x._2
            // only ranges with diff or 1 or 2 matter
            // some ranges with 2 are only 2 long
            if (diff <= 2) (elem._1, elem._2, elem._3, diff) :: x :: xs
            else (elem._1, elem._2, elem._3, 0) :: x :: xs
          }
          case (list, elem) => (elem._1, elem._2, elem._3, 0) :: list
        }
        .reverse
    }

    def lowerDedup(arr: List[(Int, Int, Int)]) = {
      arr.tail
        .foldLeft[List[(Int, Int, Int)]](List(arr.head)) {
          case (x :: xs, elem) if (x._2 == elem._2) => x :: xs
          case (list, elem)                         => elem :: list
        }
        .reverse
    }

    // process uppers
    val u2 = addDiff(pairs)
    val u3 = adjustStart(u2)
    val u4 = compressRanges(u3)
    //printIndented(u4)

    // Lower case manipulation
    val lPairs = pairs.sortBy { case ((u, l, d)) => ((l, u)) }
    val l2     = lowerDedup(lPairs)
    // 1104 total, 1083 in lowers with dups removed - total 2187 vs 2188 in UnicodeData.txt
    // 244 in uppers because of upper -> lower can have more than one mapping
    // 230 in lowers
    // upper to lower is loss less but back is not reversible
    val l3 = addDiffLower(l2)
    val l4 = adjustStart(l3)
    val l5 = compressRanges(l4)
    //printIndented(l5)

    val uk = u4.unzip { case (_, _, c, d) => (c, d) }
    val ul = u4.unzip { case (a, b, _, _) => (a, b) }
    val lk = l5.unzip { case (_, _, c, d) => (c, d) }
    val ll = l5.unzip { case (a, b, _, _) => (a, b) }
    println(
      uk._1.mkString("private[this] lazy val upperDeltas = Array[scala.Int](",
                     ", ",
                     ")\n"))
    println(
      uk._2.mkString("private[this] lazy val upperSteps = Array[scala.Byte](",
                     ", ",
                     ")\n"))
    println(
      ul._1.mkString("private[this] lazy val upperRanges = Array[scala.Int](",
                     ", ",
                     ")\n"))
    println(
      lk._1.mkString("private[this] lazy val lowerDeltas = Array[scala.Int](",
                     ", ",
                     ")\n"))
    println(
      lk._2.mkString("private[this] lazy val lowerSteps = Array[scala.Byte](",
                     ", ",
                     ")\n"))
    println(
      ll._2.mkString("private[this] lazy val lowerRanges = Array[scala.Int](",
                     ", ",
                     ")\n"))
  }

}
