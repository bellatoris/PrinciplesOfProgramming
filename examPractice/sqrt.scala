object Main {
  def main(args: Array[String]): Unit = {
    def abs(x: Double): Double = 
      if (x > 0) x else -x
    
    def square(x: Double): Double = 
      x * x

    def sqrt(x: Double): Double = {
      def isGoodEnough(guess: Double): Boolean = {
        square(guess) / x <= square(1.001) && square(guess) / x >= square(0.999)
      }

      def improve(guess: Double): Double = 
        (guess + x / guess) / 2.0

      def sqrtIter(guess: Double): Double =
        if (isGoodEnough(guess)) guess
        else sqrtIter(improve(guess))

      sqrtIter(1.0)
    }

    println(sqrt(2.0))
  }
}
