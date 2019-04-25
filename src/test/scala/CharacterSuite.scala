import org.scalatest._
import org.scalatest.FunSuite
import java.lang.{Character => JCharacter}
import CaseFoldingTest._


/** Test suite for [[java.lang.Character]]
  *
  * To be consistent the implementations should be based on
  * Unicode 7.0.
  * @see [[http://www.unicode.org/Public/7.0.0 Unicode 7.0]]
  *
  * Overall code point range U+0000 - U+D7FF and U+E000 - U+10FFF.
  * Surrogate code points are in the gap and U+FFFF
  * is the max value for [[scala.Char]].
  */
object CharacterSuite extends FunSuite {

  /** Upper/Lowercase based on Unicode 7 case folding.
    * @see [[http://www.unicode.org/Public/7.0.0/ucd/CaseFolding.txt]]
    */
  test("toLowerCase") {
    assert(toLowerCase('A') equals 'a')
    assert(toLowerCase('a') equals 'a')
    assert(toLowerCase('a') != 'A')
    assert(toLowerCase('F') equals 'f')
    assert(toLowerCase('Z') equals 'z')
    assert(toLowerCase('\n') equals '\n')
    assert(toLowerCase('µ') equals 'μ') 
    /** The implementation with the Char argument forwards
      * to the implementation with the Int argument.
      * Most sequences that step by two have alternating
      * upper and lowercase code points. The Int tests
      * are below.
      */
    /**
     * (65313,65345,32,0)(65338,65370,32,1)
(66560,66600,40,0)(66599,66639,40,1)
(71840,71872,32,0)(71871,71903,32,1)
     */
    assert(toLowerCase('A'.toInt) equals 'a'.toInt)
    assert(toLowerCase('a'.toInt) equals 'a'.toInt)
    assert(toLowerCase('a'.toInt) != 'A'.toInt)
  }

  test("toUpperCase") {
    assert(toUpperCase('a') equals 'A')
    assert(toUpperCase('A') equals 'A')
    assert(toUpperCase('A') != 'a')
    assert(toUpperCase('f') equals 'F')
    assert(toUpperCase('z') equals 'Z')
    assert(toUpperCase('\n') equals '\n')
    assert(toUpperCase('ß') equals 'ẞ')
    /** The Int tests are below. */
    assert(toUpperCase('a'.toInt) equals 'A'.toInt)
    assert(toUpperCase('A'.toInt) equals 'A'.toInt)
    assert(toUpperCase('A'.toInt) != 'a'.toInt)
  }
}
