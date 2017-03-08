package pp2017.test
import pp2017.submission.Main.foo

object Test {
  def printOX(b:Boolean) = if (b) println("O") else println("X")

  def main(args: Array[String]) : Unit = {
    printOX(foo(3) == 4)
    printOX(foo(4) == 5)
    printOX(foo(5) == 7)
    printOX(foo(5, 4) == 4)
    args.foreach(arg => println(arg))
  }


}
