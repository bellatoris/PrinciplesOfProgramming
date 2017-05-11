package pp201701.mid2.Data

object DataBundle {
  sealed abstract class BTree[A]
  case class Leaf[A]() extends BTree[A]
  case class Node[A](value: A, left: BTree[A], right: BTree[A]) extends BTree[A]

  type BSTree[K, V] = BTree[(K, V)]
}
