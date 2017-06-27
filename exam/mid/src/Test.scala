package pp201701.mid1test
import pp201701.mid1.Main._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  print_result(fib(4) == 3)

  for (i <- 1 until 10) {
    print(fib(i)+ " ")
  }
  println()

  fib(100000)
}
