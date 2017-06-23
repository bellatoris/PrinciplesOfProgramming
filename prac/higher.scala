import scala.language.higherKinds

object Main {
  def main(args: Array[String]): Unit = {
    abstract class Iter[I[_]] {
      def getValue[A](a: I[A]): Option[A]
      def getNext[A](a: I[A]): I[A]
    }

    def sumElements[I[_]](xs: I[Int])(implicit itr: Iter[I]): Int = itr.getValue(xs) match {
      case None => 0
      case Some(n) => n + sumElements(itr.getNext(xs))
    }

    def printElements[I[_], A](xs: I[A])(implicit itr: Iter[I]): Unit = itr.getValue(xs) match {
      case None =>
      case Some(n) => {
        println(n)
        printElements(itr.getNext(xs))
      }
    }

    implicit val listIter: Iter[List] = new Iter[List] {
      def getValue[A](a: List[A]) = a.headOption
      def getNext[A](a: List[A]) = a.tail
    }

    val l = List(3, 5, 2, 1)
    sumElements(l) //sumElements(l)(listIter) 
    printElements(l) //printElements(l)(listIter)

    abstract class Iterable[R[_], I[_]] {
      def iter[A](a: R[A]): I[A]
      def iterProxy: Iter[I]
    }

    def sumElements2[R[_], I[_]](xs: R[Int])(implicit proxy: Iterable[R, I]) = 
      sumElements(proxy.iter(xs))(proxy.iterProxy)

    def printElements2[R[_], I[_], A](xs: R[A])(implicit proxy: Iterable[R, I]) = 
      printElements(proxy.iter(xs))(proxy.iterProxy)

    sealed abstract class MyTree[A]
    case class Empty[A]() extends MyTree[A]
    case class Node[A](value: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

    implicit val treeIterable: Iterable[MyTree, List] = new Iterable[MyTree, List] {
      def iter[A](a: MyTree[A]): List[A] = a match {
        case Empty() => Nil
        case Node(v, left, right) => v :: (iter(left) ++ iter(right))
      }
      val iterProxy = implicitly[Iter[List]]
    }

    val t: MyTree[Int] = Node(3,Node(4,Empty(),Empty()),Node(2,Empty(),Empty()))
    sumElements2(t) //sumElements2(t)(treeIterable) 
    printElements2(t) //printElements2(t)(treeIterable)

    implicit def iterIterable[I[_]](implicit proxy: Iter[I]): Iterable[I, I] = new Iterable[I, I] {
      def iter[A](a: I[A]) = a
      def iterProxy = proxy
    }

    sumElements2(l) //sumElements2(l)(iterIterable(listIter)) 
    printElements2(l) //printElements2(l)(iterIterable(listIter))

    abstract class Foo[I[_[_]]] {
      def get: I[List]
    }

    def f(x: Foo[Iter]): Iter[List] = x.get
  }
}
