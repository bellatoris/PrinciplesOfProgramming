package pp201701.mid1
import scala.annotation.tailrec
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
  /* Problem 1: Tail-recursive Fibonacci
   * Related to HW1-2
   * Implement a tail-recursive function that calculates Fibonacci numbers.
   * Assume 1 <= n <= 10^5.
   * Fibonacci number: fib(1) = 1, fib(2) = 1, fib(n) = fib(n-1) + fib(n-2)
   */

  def fib(n: Int): BigInt = {
    @tailrec
    def _fibC(idx: Int, current: BigInt, past: BigInt): BigInt = {
      if (idx == n) current
      else _fibC(idx + 1, current + past, current)
    }
    _fibC(1, 1, 0)
  }
}
