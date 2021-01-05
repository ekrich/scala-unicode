package org.ekrich.unicode

import Functions._

/**
 * Application to create upper and lower case tables.
 * 
 * Web address for version 13 as an example
 * https://www.unicode.org/Public/13.0.0/ucd/
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
    val recs = arrays.map { r =>
      DB(r(0),
         r(1),
         r(2),
         r(3),
         r(4),
         r(5),
         r(6),
         r(7),
         r(8),
         r(9),
         r(10),
         r(11),
         r(12),
         r(13),
         r(14))
    }

    num(recs, "Full DB")
    // Special characters - CAPITALS with LC, UC, and TC
    // (01C5,LATIN CAPITAL LETTER D WITH SMALL LETTER Z WITH CARON,Lt,0,L,<compat> 0044 017E,N,01C4,01C6,01C5)
    // (01C8,LATIN CAPITAL LETTER L WITH SMALL LETTER J,Lt,0,L,<compat> 004C 006A,N,01C7,01C9,01C8)
    // (01CB,LATIN CAPITAL LETTER N WITH SMALL LETTER J,Lt,0,L,<compat> 004E 006A,N,01CA,01CC,01CB)
    // (01F2,LATIN CAPITAL LETTER D WITH SMALL LETTER Z,Lt,0,L,<compat> 0044 007A,N,01F1,01F3,01F2)
    val ulRecs = recs.filter(r => r.uc != "" || r.lc != "")
    num(ulRecs, "Upper/Lower")

    // lowers should have (uc field) except special letters above
    val lowers  = ulRecs.filter(r => r.lc == "" | (r.uc != "" && r.lc != ""))
    val uppers  = ulRecs.filter(r => r.uc == "" | (r.uc != "" && r.lc != ""))
    val lcompat = lowers.filter(r => r.decomp.startsWith("<compat>"))
    val ucompat = uppers.filter(r => r.decomp.startsWith("<compat>"))

    num(lcompat, "Lower <compat>") // advisory
    num(ucompat, "Upper <compat>") // advisory
    num(lowers, "Lowers")
    num(uppers, "Uppers")

    // process lowers - field cp, uc
    val lTuple2 = lowers.map(r => (toInt(r.cp), toInt(r.uc)))
    val lowTup4 = processCase("lower", lTuple2)
    //printIndented(lowTup4, "lower")

    // process uppers - field cp, lc
    val uTuple2 = uppers.map(r => (toInt(r.cp), toInt(r.lc)))
    val uppTup4 = processCase("upper", uTuple2)
    //printIndented(uppTup4, "upper")
  }

}
