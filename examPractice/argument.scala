object Main {
  def main(args: Array[String]): Unit = {
    abstract class Iterable[A](eq: (A, A) => Boolean) {
      type iter_t
      def iter: iter_t
      def getValue(i: iter_t): Option[A]
      def getNext(i: iter_t): iter_t
      def hasElement(a: A): Boolean = {
        def hasElementIter(i: iter_t): Boolean = getValue(i) match {
          case None => false
          case Some(n) => {
            if (eq(a, n)) true
            else hasElementIter(getNext(i))
          }
        }
        hasElementIter(iter)
      }
    }

    sealed abstract class MyTree[A](eq: (A, A) => Boolean) extends Iterable[A](eq) {
      type iter_t = List[A]
      def getValue(i: iter_t) = i.headOption
      def getNext(i: iter_t) = i.tail
    }
    case class Leaf[A](eq: (A, A) => Boolean) extends MyTree[A](eq) {
      def iter = Nil
    }
    case class Node[A](eq: (A, A) => Boolean, 
                       v: A, l: MyTree[A], r: MyTree[A]) extends MyTree[A](eq) {
      def iter = v :: (l.iter ++ r.iter)
    }

    val eq = (x: Int, y: Int) => x == y
    val leaf = Leaf(eq)
    def node(n: Int, t1: MyTree[Int]=leaf, t2: MyTree[Int]=leaf) = Node(eq, n, t1, t2)

    val t: MyTree[Int] = node(3, node(4, node(2), node(3)), node(5))
     
    def sumElements(xs: Iterable[Int]): Int = {
      def sumElementsIter(i: xs.iter_t): Int = xs.getValue(i) match {
        case None => 0
        case Some(n) => n + sumElementsIter(xs.getNext(i))
      }
      sumElementsIter(xs.iter)
    }
    println(sumElements(t),
    t.hasElement(5),
    t.hasElement(10))
  }
}
