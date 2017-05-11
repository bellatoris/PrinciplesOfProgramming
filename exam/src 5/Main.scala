package pp201701.mid5

/*
 *  Midterm of Principles of Programming 2017
 *  
 *  General Instructions:
 *  - './compile.sh' compiles your code
 *  - './run.sh' runs a simple test case given in Test.scala
 *  - Before submission, check your code by running './run.sh'. Uncompilable code will get 0 points.
 *  - Do not use imperative features. (ex. for, while, var, etc)
 *  - Compress the 'src' folder with '.zip' format for submission.
 */

object Main {
  /*
   Problem 5: Newton's method
p
   Implement a function that finds the n-th root of the given input number k, by using the Newton's method.
   (x is the n-th root of k when x^n = k.)

   Assume n >= 1 and k > 0.

   Newton's method:
   - First, guess any initial value for the n-th root.
   - Keep improving the guessed value until it becomes close enough to the real value.

   The error ratio should be less then 0.1%. In other words, the ratio (x^n / k) should be between 0.999 ~ 1.001.

   Your implementation should not expose any functions except 'nth_root' itself. Use blocks to hide internal function definitions.

   We provide how to calculate improve approximated value:

   [ improved_guess = ((n-1)*guess + k/(guess^(n-1)))/n ]

   */
  def nth_root(k:Double, n:Int) = {
    def isGoodEnough(guess: Double): Boolean = { 
      val ratio = power(guess, n) / k
      ratio > 0.999 && ratio < 1.001
    }

    def power(guess: Double, n: Int): Double = {
      if (n == 0) 1
      else guess * power(guess, n - 1)
    }

    def improve(guess: Double): Double = {
      if (isGoodEnough(guess)) guess
      else improve((guess * (n - 1) + k / power(guess, n -1)) / n)
    }
    
    improve(1.0)
  }
}
