package pp201701.mid4test
import pp201701.mid4.Main._
import pp201701.mid4.Data.DataBundle._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  def listIntToIList(xs: List[Int]): IList =
    xs match {
      case (h :: t) => ICons(h, listIntToIList(t))
      case Nil => INil()
    }
  {
    val a = listIntToIList(List(1, 2))
    val b = listIntToIList(List(2, 1))
    print_result(reverse(a) == b)
  }
}
