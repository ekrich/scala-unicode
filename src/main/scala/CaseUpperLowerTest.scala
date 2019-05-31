package org.ekrich.unicode

object CaseUpperLowerTest {

  def main(args: Array[String]): Unit = {
    import java.lang.Character

    def toInt(hex: String): Int = Integer.parseInt(hex, 16)
    def show(from: Char, to: Char) =
      println(s"${from.toInt} = '$from' => '$to'")
    println(s"FFFF == ${toInt("FFFF")}")
    println("toUpperCase ---------------------------")

    (0 to 400)
      .map(i => i.toChar)
      .map(c => (c, toUpperCase(c)))
      .foreach { case (l, u) => show(l, u) }

    println("toLowerCase ---------------------------")

    (0 to 400)
      .map(i => i.toChar)
      .map(c => (c, toLowerCase(c)))
      .foreach { case (l, u) => show(l, u) }

    println("toUpperCase cp => sn vs jdk")
    def p(cp: Int, n: Int, j: Int) = {
      def render(cp: Int) = s"${cp.toChar}/${cp.toHexString}/$cp"
      val s = "same"
      val sn = if (cp == n) s else render(n)
      val jvm = if (cp == j) s else render(j)
      s"${render(cp)} => ${sn} vs ${jvm}"
    }
    val codePoints = (toInt("0000") to toInt("D7FF")).toList ::: (toInt("E000") to toInt(
      "10FFFF")).toList
    val upp = codePoints
      .map { cp =>
        val n = toUpperCase(cp)
        val j = Character.toUpperCase(cp)
        if (n == j) None else Option(p(cp, n, j))
      }
      .flatMap(x => x)

    println(s"num-diffs=${upp.size}\n${upp.mkString("\n")}")
    println("toLowerCase cp => sn vs jdk")
    val low = codePoints.toList
      .map { cp =>
        val n = toLowerCase(cp)
        val j = Character.toLowerCase(cp)
        if (n == j) None else Option(p(cp, n, j))
      }
      .flatMap(x => x)

    //.foldLeft(true)((l,r) => l && r)

    println(s"num-diffs=${low.size}\n${low.mkString("\n")}")

  }
  import java.util.Arrays

// Create tables to support toUpperCase and toLowerCase transformations
  // using the Unicode 7.0 database. This now uses UnicodeData.txt.
  // Refer to the following project for the transformation code.
  // https://github.com/ekrich/scala-unicode

  private[this] lazy val upperDeltas = Array[scala.Int](-32, -32, -32, -32, -32,
    -32, -1, -1, 199, -1, -1, -1, -1, -1, -1, 121, -1, -1, -210, -1, -1, -206,
    -1, -205, -205, -1, -79, -202, -203, -1, -205, -207, -211, -209, -1, -211,
    -213, -214, -1, -1, -218, -1, -218, -1, -218, -1, -217, -217, -1, -1, -219,
    -1, -1, -2, -1, -2, -1, -2, -1, -1, -1, -1, -2, -1, -1, 97, 56, -1, -1, 130,
    -1, -1, -10795, -1, 163, -10792, -1, 195, -69, -71, -1, -1, -1, -1, -1,
    -116, -38, -37, -37, -64, -63, -63, -32, -32, -32, -32, -8, -1, -1, 60, -1,
    7, -1, 130, 130, -80, -80, -32, -32, -1, -1, -1, -1, -15, -1, -1, -1, -1,
    -48, -48, -7264, -7264, -7264, -7264, -1, -1, 7615, -1, -1, 8, 8, 8, 8, 8,
    8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 74, 74, 9, 86, 86, 9, 8,
    8, 100, 100, 8, 8, 112, 112, 7, 128, 128, 126, 126, 9, 7517, 8383, 8262,
    -28, -16, -16, -1, -26, -26, -48, -48, -1, 10743, 3814, 10727, -1, -1,
    10780, 10749, 10783, 10782, -1, -1, 10815, 10815, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, 35332, -1, -1, -1, 42280, -1, -1, -1,
    -1, 42308, 42319, 42315, 42305, 42258, 42282, -32, -32, -40, -40, -32, -32)

  private[this] lazy val upperSteps = Array[scala.Byte](0, 1, 0, 1, 0, 1, 0, 2,
    0, 0, 2, 0, 2, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 2, 0, 2, 0, 0, 2, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0,
    2, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 1, 0,
    1, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 1, 0, 1, 0, 0, 0, 2, 0, 0, 2, 0, 1, 0, 1,
    0, 1, 0, 1, 0, 1, 0, 2, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0,
    1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0,
    0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 2, 0,
    2, 0, 2, 0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1)

  private[this] lazy val upperRanges = Array[scala.Int](65, 90, 192, 214, 216,
    222, 256, 302, 304, 306, 310, 313, 327, 330, 374, 376, 377, 381, 385, 386,
    388, 390, 391, 393, 394, 395, 398, 399, 400, 401, 403, 404, 406, 407, 408,
    412, 413, 415, 416, 420, 422, 423, 425, 428, 430, 431, 433, 434, 435, 437,
    439, 440, 444, 452, 453, 455, 456, 458, 459, 475, 478, 494, 497, 498, 500,
    502, 503, 504, 542, 544, 546, 562, 570, 571, 573, 574, 577, 579, 580, 581,
    582, 590, 880, 882, 886, 895, 902, 904, 906, 908, 910, 911, 913, 929, 931,
    939, 975, 984, 1006, 1012, 1015, 1017, 1018, 1021, 1023, 1024, 1039, 1040,
    1071, 1120, 1152, 1162, 1214, 1216, 1217, 1229, 1232, 1326, 1329, 1366,
    4256, 4293, 4295, 4301, 7680, 7828, 7838, 7840, 7934, 7944, 7951, 7960,
    7965, 7976, 7983, 7992, 7999, 8008, 8013, 8025, 8031, 8040, 8047, 8072,
    8079, 8088, 8095, 8104, 8111, 8120, 8121, 8122, 8123, 8124, 8136, 8139,
    8140, 8152, 8153, 8154, 8155, 8168, 8169, 8170, 8171, 8172, 8184, 8185,
    8186, 8187, 8188, 8486, 8490, 8491, 8498, 8544, 8559, 8579, 9398, 9423,
    11264, 11310, 11360, 11362, 11363, 11364, 11367, 11371, 11373, 11374, 11375,
    11376, 11378, 11381, 11390, 11391, 11392, 11490, 11499, 11501, 11506, 42560,
    42604, 42624, 42650, 42786, 42798, 42802, 42862, 42873, 42875, 42877, 42878,
    42886, 42891, 42893, 42896, 42898, 42902, 42920, 42922, 42923, 42924, 42925,
    42928, 42929, 65313, 65338, 66560, 66599, 71840, 71871)

  private[this] lazy val lowerDeltas = Array[scala.Int](32, 32, -743, 32, 32,
    32, 32, -121, 1, 1, 232, 1, 1, 1, 1, 1, 1, 1, 1, 300, -195, 1, 1, 1, 1, 1,
    -97, 1, -163, -130, 1, 1, 1, 1, 1, 1, 1, 1, 1, -56, 2, 2, 2, 1, 1, 79, 1, 1,
    2, 1, 1, 1, 1, 1, 1, -10815, -10815, 1, 1, 1, -10783, -10780, -10782, 210,
    206, 205, 205, 202, 203, -42319, 205, -42315, 207, -42280, -42308, 209, 211,
    -10743, -42305, 211, -10749, 213, 214, -10727, 218, 218, -42282, 218, 69,
    217, 217, 71, 219, -42258, -84, 1, 1, 1, -130, -130, 38, 37, 37, 32, 32, 31,
    32, 32, 64, 63, 63, 62, 57, 47, 54, 8, 1, 1, 86, 80, -7, 116, 96, 1, 1, 32,
    32, 80, 80, 1, 1, 1, 1, 1, 1, 15, 1, 1, 48, 48, -35332, -3814, 1, 1, 59, 1,
    1, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -74, -74, -86,
    -86, -100, -100, -128, -128, -112, -112, -126, -126, -8, -8, -8, -8, -8, -8,
    -8, -8, -9, 7205, -9, -8, -8, -8, -8, -7, -9, 28, 16, 16, 1, 26, 26, 48, 48,
    1, 10795, 10792, 1, 1, 1, 1, 1, 1, 1, 1, 1, 7264, 7264, 7264, 7264, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 32, 32, 40, 40, 32, 32)

  private[this] lazy val lowerSteps = Array[scala.Byte](0, 1, 0, 0, 1, 0, 1, 0,
    0, 2, 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0,
    0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 1, 0,
    0, 2, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1,
    0, 0, 1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 2, 0, 2,
    0, 2, 0, 0, 2, 0, 1, 0, 0, 0, 2, 0, 0, 2, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
    2, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0,
    0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0,
    2, 0, 0, 1, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 1,
    0, 1, 0, 1)

  private[this] lazy val lowerRanges = Array[scala.Int](97, 122, 181, 224, 246,
    248, 254, 255, 257, 303, 305, 307, 311, 314, 328, 331, 375, 378, 382, 383,
    384, 387, 389, 392, 396, 402, 405, 409, 410, 414, 417, 421, 424, 429, 432,
    436, 438, 441, 445, 447, 454, 457, 460, 462, 476, 477, 479, 495, 499, 501,
    505, 543, 547, 563, 572, 575, 576, 578, 583, 591, 592, 593, 594, 595, 596,
    598, 599, 601, 603, 604, 608, 609, 611, 613, 614, 616, 617, 619, 620, 623,
    625, 626, 629, 637, 640, 643, 647, 648, 649, 650, 651, 652, 658, 670, 837,
    881, 883, 887, 891, 893, 940, 941, 943, 945, 961, 962, 963, 971, 972, 973,
    974, 976, 977, 981, 982, 983, 985, 1007, 1008, 1009, 1010, 1011, 1013, 1016,
    1019, 1072, 1103, 1104, 1119, 1121, 1153, 1163, 1215, 1218, 1230, 1231,
    1233, 1327, 1377, 1414, 7545, 7549, 7681, 7829, 7835, 7841, 7935, 7936,
    7943, 7952, 7957, 7968, 7975, 7984, 7991, 8000, 8005, 8017, 8023, 8032,
    8039, 8048, 8049, 8050, 8053, 8054, 8055, 8056, 8057, 8058, 8059, 8060,
    8061, 8064, 8071, 8080, 8087, 8096, 8103, 8112, 8113, 8115, 8126, 8131,
    8144, 8145, 8160, 8161, 8165, 8179, 8526, 8560, 8575, 8580, 9424, 9449,
    11312, 11358, 11361, 11365, 11366, 11368, 11372, 11379, 11382, 11393, 11491,
    11500, 11502, 11507, 11520, 11557, 11559, 11565, 42561, 42605, 42625, 42651,
    42787, 42799, 42803, 42863, 42874, 42876, 42879, 42887, 42892, 42897, 42899,
    42903, 42921, 65345, 65370, 66600, 66639, 71872, 71903)

  private[this] object CaseUtil {
    lazy val a = lowerRanges(0)
    lazy val z = lowerRanges(1)
    lazy val A = upperRanges(0)
    lazy val Z = upperRanges(1)
    // other low char optimization whitespace, punctuation, etc.
    lazy val upperMu                        = upperRanges(2)
    lazy val lowerBeta                      = lowerRanges(2)
    def insertionPoint(idx: Int)            = (-(idx) - 1)
    def convert(codePoint: Int, delta: Int) = codePoint - delta
  }

  private[this] def toCase(codePoint: Int,
                           asciiLow: Int,
                           asciiHigh: Int,
                           lowFilter: Int,
                           ranges: Array[scala.Int],
                           deltas: Array[scala.Int],
                           steps: Array[scala.Byte]): Int = {
    import CaseUtil._
    if (asciiLow <= codePoint && codePoint <= asciiHigh)
      convert(codePoint, deltas(0)) // ascii
    else if (codePoint < lowFilter) codePoint // whitespace, punctuation, etc.
    else {
      val idx = Arrays.binarySearch(ranges, codePoint)
      if (idx >= 0) {
        convert(codePoint, deltas(idx))
      } else {
        val ip = insertionPoint(idx)
        // ip == 0 is below ranges but < lowFilter above covers that
        if (ip == ranges.size) codePoint // above ranges
        else {
          val step = steps(ip)
          if (step == 0) {
            // no range involved
            codePoint
          } else {
            val delta      = deltas(ip)
            val upperBound = ranges(ip)
            if (step == 1) {
              convert(codePoint, delta)
            } else {
              // step == 2 so check both odd or even
              if ((upperBound & 1) == (codePoint & 1)) {
                convert(codePoint, delta)
              } else {
                codePoint
              }
            }
          }
        }
      }
    }
  }

  /* Conversions */
  def toUpperCase(c: scala.Char): scala.Char = {
    toUpperCase(c.toInt).toChar
  }

  def toUpperCase(codePoint: Int): Int = {
    import CaseUtil._
    toCase(codePoint, a, z, lowerBeta, lowerRanges, lowerDeltas, lowerSteps)
  }

  def toLowerCase(c: scala.Char): scala.Char = {
    toLowerCase(c.toInt).toChar
  }

  def toLowerCase(codePoint: Int): Int = {
    import CaseUtil._
    toCase(codePoint, A, Z, upperMu, upperRanges, upperDeltas, upperSteps)
  }
}
