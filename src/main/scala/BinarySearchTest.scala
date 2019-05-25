package org.ekrich.unicode

object BinarySearchTest {
  def main(args: Array[String]): Unit = {
    println("Welcome to the Scala worksheet") //> Welcome to the Scala worksheet
    import java.util.Arrays
    val arr                     = Array(65, 90, 181, 182) //> arr  : Array[Int] = Array(65, 90, 181, 182)
    def ip(insertionPoint: Int) = -insertionPoint - 1
    //> ip: (insertionPoint: Int)Int
    Arrays.binarySearch(arr, 65) //> res0: Int = 0
    Arrays.binarySearch(arr, 90) //> res1: Int = 1
    val f = Arrays.binarySearch(arr, 89) //> f  : Int = -2
    ip(f) //> res2: Int = 1
    // these are not found so check further with insertion point
    // in range returns insertion point in array
    val a = Arrays.binarySearch(arr, 70) //> a  : Int = -2
    ip(a) //> res3: Int = 1
    val b = Arrays.binarySearch(arr, 71) //> b  : Int = -2
    ip(b) //> res4: Int = 1
    //below is 0
    val c = Arrays.binarySearch(arr, 50) //> c  : Int = -1
    ip(c) //> res5: Int = 0
    val d = Arrays.binarySearch(arr, 100) //> d  : Int = -3
    ip(d) //> res6: Int = 2
    // above is array.length
    val e = Arrays.binarySearch(arr, 200) //> e  : Int = -5
    ip(e) //> res7: Int = 4

    0 & 1    //> res8: Int(0) = 0
    2 & 1    //> res9: Int(0) = 0
    4 & 1    //> res10: Int(0) = 0
    1 & 1    //> res11: Int(1) = 1
    3 & 1    //> res12: Int(1) = 1
    (-1) & 1 //> res13: Int(1) = 1
    (-2) & 1 //> res14: Int(0) = 0

    256 & 2 //> res15: Int(0) = 0
    302 & 2 //> res16: Int(2) = 2
    257 & 2 //> res17: Int(0) = 0

    val aa = 60 /* 60 = 0011 1100 */ //> aa  : Int = 60
    val bb = 2 /* 13 = 0000 1101 */ //> bb  : Int = 2
    var cc = 0 //> cc  : Int = 0

    cc = aa & bb /* 12 = 0000 1100 */
    println("a & b = " + cc); //> a & b = 0

    cc = aa | bb /* 61 = 0011 1101 */
    println("a | b = " + cc); //> a | b = 62

    cc = aa ^ bb /* 49 = 0011 0001 */
    println("a ^ b = " + cc); //> a ^ b = 62

    cc = ~aa /* -61 = 1100 0011 */
    println("~a = " + cc); //> ~a = -61

    cc = aa << 2 /* 240 = 1111 0000 */
    println("a << 2 = " + cc); //> a << 2 = 240

    cc = aa >> 2 /* 215 = 1111 */
    println("a >> 2  = " + cc); //> a >> 2  = 15

    cc = aa >>> 2 /* 215 = 0000 1111 */
    println("a >>> 2 = " + cc); //> a >>> 2 = 15

    val x = 4 //> x  : Int = 4
    val y = 8 //> y  : Int = 8
    val z = 2 //> z  : Int = 2

    8 & 2   //> res18: Int(0) = 0
    9 & 2   //> res19: Int(0) = 0
    9 & 1   //> res20: Int(1) = 1
    54 & 2  //> res21: Int(2) = 2
    128 & 2 //> res22: Int(0) = 0
    // odd even test
    128 % 2 //> res23: Int(0) = 0
    130 % 2 //> res24: Int(0) = 0
    129 % 2 //> res25: Int(1) = 1
    131 % 2 //> res26: Int(1) = 1
    // again
    128 & 1 //> res27: Int(0) = 0
    130 & 1 //> res28: Int(0) = 0
    129 & 1 //> res29: Int(1) = 1
    131 & 1 //> res30: Int(1) = 1
  }
}
