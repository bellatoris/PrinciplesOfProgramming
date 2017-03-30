object Main {
  def main(args: Array[String]): Unit = {
    sealed abstract class BTree[A]
    case class Leaf[A]() extends BTree[A]
    case class Node[A](value: A, left: BTree[A] = Leaf[A](), right: BTree[A] = Leaf[A]()) extends BTree[A]
    
    type BSTree[A] = BTree[(Int, A)]

    def lookup[A](t: BSTree[A], k: Int): Option[A] = t match {
      case Leaf() => None
      case Node((key, v), left, right) => 
        if (k == key) Some(v)
        else if (k < key) lookup(left, k)
        else lookup(right, k)
    }

     def t: BSTree[String] = Node((5,"My5"), Node((4,"My4"),Node((2,"My2"))), Node((7,"My7"),Node((6,"My6"))))
  
    println(lookup(t, 7)) 
  }

}
