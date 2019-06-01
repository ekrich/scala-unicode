package org.ekrich.unicode

import Functions._

/**
 *
 * Note: change "num" to "show" to see data and uncomment prints as needed
 */
object CaseUpperLower {
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
    val arrays = recordArrays(lines, 15)
    val tuples = arrays.map { c =>
      (c(0), c(1), c(2), c(3), c(4), c(5), c(9), c(12), c(13), c(14))
    }
    num(tuples, "Full DB")
    // Special characters - CAPITALS with LC, UC, and TC
    // (01C5,LATIN CAPITAL LETTER D WITH SMALL LETTER Z WITH CARON,Lt,0,L,<compat> 0044 017E,N,01C4,01C6,01C5)
    // (01C8,LATIN CAPITAL LETTER L WITH SMALL LETTER J,Lt,0,L,<compat> 004C 006A,N,01C7,01C9,01C8)
    // (01CB,LATIN CAPITAL LETTER N WITH SMALL LETTER J,Lt,0,L,<compat> 004E 006A,N,01CA,01CC,01CB)
    // (01F2,LATIN CAPITAL LETTER D WITH SMALL LETTER Z,Lt,0,L,<compat> 0044 007A,N,01F1,01F3,01F2)
    val ulTuples = tuples.filter(c => c._8 != "" || c._9 != "")
    num(ulTuples, "Upper/Lower")

    // lowers should have t._9 (uc field) except special letters above
    val lowers  = ulTuples.filter(t => t._9 == "" | (t._8 != "" && t._9 != ""))
    val uppers  = ulTuples.filter(t => t._8 == "" | (t._8 != "" && t._9 != ""))
    val lcompat = lowers.filter(t => t._6.startsWith("<compat>"))
    val ucompat = uppers.filter(t => t._6.startsWith("<compat>"))

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
