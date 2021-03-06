object Main {
  def main(args: Array[String]): Unit = {
    abstract class Iter[I, A] {
      def getValue(i: I): Option[A]
      def getNext(i: I): I
    }

    def sumElements[I](xs: I)(implicit proxy: Iter[I, Int]): Int = proxy.getValue(xs) match {
      case None => 0
      case Some(n) => n + sumElements(proxy.getNext(xs))
    }

    def printElements[I, A](xs: I)(implicit proxy: Iter[I, A]): Unit = proxy.getValue(xs) match {
      case None =>
      case Some(n) => { 
        println(n)
        printElements(proxy.getNext(xs))
      }
    }

    implicit def listIter[A]: Iter[List[A], A] = new Iter[List[A], A] {
      def getValue(a: List[A]) = a.headOption
      def getNext(a: List[A]) = a.tail
    }

    val l = List(3, 4, 2, 1)

    println(sumElements(l))
    printElements(l)

    abstract class Iterable[R, I, A] {
      def iter(a: R): I
      def iterProxy: Iter[I, A]
    }

    def sumElements2[R, I](xs: R)(implicit proxy: Iterable[R, I, Int]) = 
      sumElements(proxy.iter(xs))(proxy.iterProxy)
  
    def printElements2[R, I, A](xs: R)(implicit proxy: Iterable[R, I, A]) = 
      printElements(proxy.iter(xs))(proxy.iterProxy)
    

    sealed abstract class MyTree[A]
    case class Empty[A]() extends MyTree[A]
    case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

    implicit def treeIterable[A](implicit proxy: Iter[List[A], A]): Iterable[MyTree[A], List[A], A] = 
      new Iterable[MyTree[A], List[A], A] {
        def iter(a: MyTree[A]): List[A] = a match {
          case Empty() => Nil
          case Node(v, left, right) => v :: (iter(left) ++ iter(right))
      }
      val iterProxy = proxy
    }

    val t : MyTree[Int] = Node(3,Node(4,Empty(),Empty()),Node(2,Empty(),Empty()))
    sumElements2(t)
    printElements2(t)
  }
}
