package pp201701.fin6test
import pp201701.fin.Main._
import pp201701.fin.Data.DataBundle._
import pp201701.fin.Data.BoxObj._
import pp201701.fin.DictImpl._
import pp201701.fin.Printer._
import pp201701.fin.Fib._

object Test extends App {
  implicit val rproxy : Report[OrdDictionary[Dictionary[Int, BigInt], Int]] =
    ReportOrdDictionary("Fibonacci", (a:Int)=>a.toString, (a:BigInt)=>a.toString)

  val fib10 : OrdDictionary[Dictionary[Int, BigInt], Int] = fibonacci(10)(DictOrdDictionary)
  val report10 = pretty_printer(fib10)

  println(report10)

  // Tests
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  print_result(report10 == "<Fibonacci>\n * 1 : 1\n * 2 : 1\n * 3 : 2\n * 4 : 3\n * 5 : 5\n * 6 : 8\n * 7 : 13\n * 8 : 21\n * 9 : 34\n * 10 : 55\n")
}
