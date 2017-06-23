object Main {
  def main(args: Array[String]): Unit = {
    sealed abstract class MyList[A]() 
    case class MyNil[A]() extends MyList[A]
    case class MyCons[A](hd: A, tl: MyList[A]) extends MyList[A]

    sealed abstract class MyTree[A]()
    case class Leaf[A]() extends MyTree[A]
    case class Node[A](v: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

    val t: MyTree[Int] = Node(3, Node(4, Leaf(), Leaf()), Leaf())

    val a: Int = t match {
      case Leaf() => 0
      case Node(v, l, r) => v
    }

  }
}
