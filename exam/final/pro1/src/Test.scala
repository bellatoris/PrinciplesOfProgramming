package pp201701.fin1test
import pp201701.fin1.Main._
import pp201701.fin1.Data.DataBundle._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  {
    val x = List("A", "B", "C")
    val bx = new BiIterableList(x)

    print_result(
      bx.biIter.getValue match {
        case Some(x) => x == "A"
        case _ => false
      }
    )
  }
}
