package org.ekrich.unicode

import Functions._

// change "num" to "show" to see data and uncomment prints as needed
object UnicodeData {
  // for non default version
  // sbt> run 10.0.0
  def main(args: Array[String]): Unit = {
    val version  = parseVersion(args)
    val path     = resourcePath(version, "UnicodeData.txt")
    val allLines = readLines(path)
    val lines    = recordLines(allLines).toList
    num(lines, "Total Records")

    // 0;   1;  2; 3;    4;     5;  6;     7;     8;  9;   10; 11;12;13;14;
    //cp;name;cat;cc;bicat;decomp;dec;digval;numval;mir;uname;com;uc;lc;tc;
    val allArrays = recordArrays(lines, 15)
    // filter for upper/lower
    val arrays = allArrays.filter(c => c(12) != "" || c(13) != "")
    num(arrays, "Upper/Lower")
    val tuples = arrays.map { c =>
      (c(0), c(1), c(2), c(3), c(4), c(5), c(9), c(12), c(13), c(14))
    }
    // t._9 is lc field 13
    val (lowers, uppers) = tuples.partition(t => t._9 == "")
    val lcompat          = lowers.filter(t => t._6.startsWith("<compat>"))
    val ucompat          = uppers.filter(t => t._6.startsWith("<compat>"))

    num(lcompat, "Lower <compat>") // advisory
    num(ucompat, "Upper <compat>") // advisory
    num(lowers, "Lowers")
    num(uppers, "Uppers")

    // process lowers - field 0, 12
    val tuple2  = lowers.map(t => (toInt(t._1), toInt(t._8)))
    val lowTup4 = processCase("lower", tuple2)
    //printIndented(lowTup4, "lower")

    // process uppers - field 0, 13
    val uTuple2 = uppers.map(t => (toInt(t._1), toInt(t._9)))
    val uppTup4 = processCase("upper", uTuple2)
    //printIndented(uppTup4, "upper")
  }

}
