package org.ekrich.unicode

import Functions._

/**
 * Application to Unicode Blocks for Character
 * Web address for version 13 as an example
 * https://www.unicode.org/Public/13.0.0/ucd/
 *
 * Note: change "num" to "show" to see data and uncomment prints as needed
 * Output example:
 * val SURROGATES_AREA = new UnicodeBlock("SURROGATES_AREA", 0x0, 0x0)
 */
case class Block(origName: String, name: String, start: Int, end: Int)

object Blocks {
  // for non default version
  // sbt> run 10.0.0
  var defaultVersion = "15.1.0"
  def main(args: Array[String]): Unit = {
    val version =
      if (args.isEmpty)
        defaultVersion
      else
        parseVersion(args)
    val path     = resourcePath(version, "Blocks.txt")
    val allLines = readLines(path)
    val lines    = recordLines(allLines).toList
    num(lines, "Total Records")

    // 0;   1;
    //range;name
    val arrays = recordArrays(lines, 2)
    val recs = arrays.map { r =>
      val origName = r(1).trim()
      val name     = origName.replace(" ", "_").toUpperCase()
      val lh       = r(0).split("""[.]{2}""", 2)
      Block(origName, name, fromHex(lh(0)), fromHex(lh(1)))
    }

    num(recs, "Blocks")
    val sortedRecs = recs.sortBy(r => (r.start, r.end))
    //println(recs)
    // for copying code
    println()
    printBlockVals(sortedRecs)
    println()
    printBlockList(sortedRecs)
    println()
    val blocksByRecs = blocksBy(sortedRecs)
    printBlocksBy(blocksByRecs)
  }

  // This could probably use Map[String, List[String]] for Accumulator vs tuple
  def blocksBy(list: List[Block]): List[(String, String)] = {
    def addBlocks(list: List[Block],
                  acc: List[(String, String)]): List[(String, String)] =
      list match {
        case Nil => acc
        case head :: tail if (head.origName.contains(" ")) =>
          addBlocks(
            tail,
            (head.name, head.name) ::
              (head.origName.replaceAll(" ", ""), head.name) :: (head.origName,
                                                                 head.name) :: acc)
        case head :: tail =>
          addBlocks(tail,
                    (head.name, head.name) :: (head.origName, head.name) :: acc)
      }
    addBlocks(list, List()).reverse
  }

  /** Print block code to std out
   *
   */
  def printBlockVals(blocks: List[Block]): Unit = {
    println(
      "val SURROGATES_AREA = new UnicodeBlock(\"SURROGATES_AREA\", 0x0, 0x0)")
    blocks
      .foreach { b =>
        println(s"""val ${b.name} = new UnicodeBlock(\"${b.name}\", ${toHex(
          b.start)}, ${toHex(b.end)})""")
      }
  }

  def printBlockList(blocks: List[Block]): Unit = {
    println(
      blocks
        .map(_.name)
        .mkString(s"private val BLOCKS = Array(\n  ", ",\n  ", "\n)"))
  }

  def printBlocksBy(blocks: List[(String, String)]): Unit = {
    println("private val BLOCKS_BY_NAME =")
    println("  scala.collection.mutable.Map.empty[String, UnicodeBlock]")
    println("BLOCKS_BY_NAME.update(\"SURROGATES_AREA\", SURROGATES_AREA)")
    blocks
      .foreach { b =>
        println(s"""BLOCKS_BY_NAME.update(\"${b._1}\", ${b._2})""")
      }
  }
}
