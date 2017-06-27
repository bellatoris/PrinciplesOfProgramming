package pp201701.fin2
import pp201701.fin2.Data.DataBundle._
import scala.math._

/*
 * ** The submitted code should be runnable. Before upload, you MUST check it
      whether Test.scala runs successfully. Otherwise you may get 0 points for
      all problems.

 * ** Compress the 'src' folder only. Don't put .sh files in the zip file.
      You can put the .iml file together, if you feel difficulty for some reasons.

 * ** Use only the features taught in class. Especially, don't use imperative
      features such as var, while, for, return, and so on. You may get deduction
      in your points.

 * ** Do not use equivalent built-in features in Scala. The core logic should be
      implemented by you.
 */

object Main {
  /*
   Problem 2: Define Typeclasses for Real numbers and Complex numbers
   */

  /***********
   Real Numbers
   ***********/

  implicit def realAddProxy: AddOp[Real] = new AddOp[Real] {
    def op(a: Real, b: Real): Real = a + b
    val identity: Real = 0
    def inverse(a: Real): Real = -a
  }

  implicit def realMultProxy: MultOp[Real] = new MultOp[Real] {
    def op(a: Real, b: Real): Real = a * b
    val identity: Real = 1
  }

  /***********
   Complex Numbers
   ***********/

  /*
   ComplexNumber class and its companion object
   Use these as constructors of 'Complex' outside 'object Complex',
   instead of using direct constructor 'new Complex(..)'.
   */
  object Complex extends ComplexBasic {
    def makeRectangular(real: Real, imaginary: Real): Complex = {
      val (magnitude, angle) = ComplexPrivate.rectangularToPolar(real, imaginary)
      new Complex(real, imaginary, magnitude, ComplexPrivate.normalizeAngle(angle))
    }
    def makePolar(magnitude: Real, angle: Real): Complex = {
      val (real, imaginary) = ComplexPrivate.polarToRectangular(magnitude, angle)
      new Complex(real, imaginary, magnitude, ComplexPrivate.normalizeAngle(angle))
    }
  }

  implicit def complexAddProxy: AddOp[Complex] = new AddOp[Complex] {
    def op(a: Complex, b: Complex): Complex = Complex.makeRectangular(a.real + b.real, 
                                                                      a.imaginary + b.imaginary)
    val identity: Complex = Complex.makeRectangular(0, 0)
    def inverse(a: Complex): Complex = Complex.makeRectangular(-a.real, -a.imaginary)
  }

  implicit def complexMultProxy: MultOp[Complex] = new MultOp[Complex] {
    def op(a: Complex, b: Complex): Complex = 
      Complex.makeRectangular(a.real * b.real - a.imaginary * b.imaginary,
                              a.real * b.imaginary + a.imaginary * b.real)
    val identity: Complex = Complex.makeRectangular(1, 0)
  }

}
