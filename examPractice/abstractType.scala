object Main {
  def main(args: Array[String]): Unit = {
    abstract class Iterable[A] {
      type iter_t
      def iter: iter_t
      def getValue(i: iter_t): Option[A]
      def getNext(i: iter_t): iter_t
    }
    def sumElements(xs: Iterable[Int]): Int = {
      def sumElementsIter(iter: xs.iter_t): Int = xs.getValue(iter) match {
        case None => 0
        case Some(n) => n + sumElementsIter(xs.getNext(iter))
      }
      sumElementsIter(xs.iter)
    }

    sealed abstract class MyTree[A] extends Iterable[A] {
      type iter_t = List[A]
      def getValue(l: List[A]) = l.headOption
      def getNext(l: List[A]) = l.tail
    }
    case class Leaf[A]() extends MyTree[A] {
      def iter = Nil
    }
    case class Node[A](v: A, l: MyTree[A], r: MyTree[A]) extends MyTree[A] {
      def iter = v :: (l.iter ++ r.iter)
    }
  }
}
