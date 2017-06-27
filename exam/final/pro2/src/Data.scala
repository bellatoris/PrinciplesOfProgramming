package pp201701.fin2.Data
import scala.annotation.tailrec
import scala.math._

object DataBundle {

  type Real = Double

  object Real {
    def equals(x: Double, y: Double) = (x-y).abs <= 1E-3
  }

  class Complex (val real: Real, val imaginary: Real, val magnitude: Real, val angle: Real)

  object ComplexPrivate {
    def rectangularToPolar(real: Double, imaginary: Double) = {

      val (x, y) = (real, imaginary)
      val magnitude = sqrt(pow(x, 2) + pow(y, 2))
      val angle: Double = {
        //from wikipedia
        if(x > 0) atan(y/x)
        else if(x < 0 && y >= 0) atan(y/x) + Pi
        else if(x < 0 && y < 0) atan(y/x) - Pi
        else if(x == 0 && y > 0) Pi/2
        else if(x == 0 && y < 0) -Pi/2
        else 0 //(x == 0 && y == 0), Indeterminate value. We don't check this case.
      }
      (magnitude, angle)
    }

    def polarToRectangular(magnitude: Double, angle: Double) = {
      (magnitude * cos(angle), magnitude * sin(angle))
    }

    @tailrec
    def normalizeAngle(x: Double): Double =
      if(x > Pi) normalizeAngle(x - 2*Pi)
      else if(x <= -Pi) normalizeAngle(x + 2*Pi)
      else x
  }

  trait ComplexBasic {
    def toString(a: Complex) = {
      def formatDouble(x: Double): String =
        if(x < 0) "%.4f".format(x)
        else "+%.4f".format(x)
      s"Rectangular Form: (${formatDouble(a.real)}, ${formatDouble(a.imaginary)}), " +
      s"Polar Form: (${formatDouble(a.magnitude)}, ${formatDouble(a.angle)})"
    }

    def equals(a: Complex, b: Complex) = {
      Real.equals(a.real, b.real) &&
      Real.equals(a.imaginary, b.imaginary) &&
      Real.equals(a.magnitude, b.magnitude) &&
      (Real.equals(a.magnitude, 0) || Real.equals(a.angle, b.angle))
    }
  }


  /*
   Typeclass defining add operator.

   Instance of this typeclass should satisfy the following laws:
   Identity: for all element a of type A, op(identity, a) = a
   Inverse: for all element a of type A, op(a, inverse(a)) = identity
   */
  abstract class AddOp[A] {
    def op(a: A, b: A): A
    val identity: A
    def inverse(a: A): A
  }

  /*
   Typeclass defining multiply operator.

   Instance of this typeclass should satisfy the following laws:
   Identity: for all element a of type A, op(identity, a) = a
   */
  abstract class MultOp[A] {
    def op(a: A, b: A): A
    val identity: A
  }
}
