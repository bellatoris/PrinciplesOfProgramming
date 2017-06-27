package pp201701.mid2test
import pp201701.mid2.Main._
import pp201701.mid2.Data.DataBundle._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  {
    val cmp = (scala.math.Ordering.Int.compare _).curried
    val emptyIntIntBST : BSTree[Int, Int] = emptyBST[Int, Int]
    val insertedBST = insert(emptyIntIntBST, (1, 3), cmp)
    val opt_r = lookup(insertedBST, 1, cmp)
    print_result(
      opt_r match {
        case Some(n) => n == 3
        case _ => false
      }
    )

    val a = (0 until 10).toList.reverse.zipWithIndex
    println(a)

    def printBST(tree: BSTree[Int, Int]): String = tree match {
      case Leaf() => " "
      case Node(kv, l, r) => printBST(l) + kv.toString + printBST(r)
    }

    println(printBST(insertList(emptyIntIntBST, a, cmp)))
    println(lookup(insertList(emptyIntIntBST, a, cmp), 3, cmp))
    println(lookup(insertList(emptyIntIntBST, a, cmp), 10, cmp))
  }

}
