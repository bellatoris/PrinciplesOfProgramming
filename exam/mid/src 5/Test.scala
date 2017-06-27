package pp201701.mid5test
import pp201701.mid5.Main._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  {
    print_result(nth_root(2.0, 2) < 2)
    println(nth_root(8.0, 3))
    println(nth_root(64, 3))
    println(nth_root(27, 3))
  }
}
