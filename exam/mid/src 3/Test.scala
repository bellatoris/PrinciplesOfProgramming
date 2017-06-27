package pp201701.mid3test
import pp201701.mid3.Main._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  {
    val mc = new MyClass[Int, Int, Int, Int, Int, Int]()

    val obj1: mc.Ty1 = new {
      val apply =
        (x:{ val func: { val a: Int } => { val b: Int } ; val c: Int }) =>
      new { val b = 0 ; val d = 1 }
      val a: Int = 1
      val b: Int = 2
    }
    val r1 = mc(obj1, 0,0,0,0,0,0)
  }
}
