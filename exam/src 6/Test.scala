package pp201701.mid6test
import pp201701.mid6.Main._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  {
    print_result(eval(mkTrue, (false,false,false,false,false)) == true)
    print_result(eval(mkOr(mkVar("A"), mkVar("B")), (true, false, true, true, true)) == true)
    print_result(eval(mkAnd(mkTrue, mkVar("A")), (true, false, false, false, false)) == true)
    print_result(eval(mkAnd(mkTrue, mkVar("A")), (false, false, false, false, false)) == false)
    print_result(eval(mkOr(mkVar("Z"), mkVar("C")), (false,false, true, false, false)) == true)
  }
}
