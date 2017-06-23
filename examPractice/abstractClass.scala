import scala.language.implicitConversions

object Main {
  def main(args: Array[String]): Unit = {
    abstract class Iter[A] extends Iterable[A] {
      def iter = this
      def getValue: Option[A]
      def getNext: Iter[A]
    }

    def sumElements(xs: Iter[Int]): Int = xs.getValue match {
      case None => 0
      case Some(n) => n + sumElements(xs.getNext)
    }

    sealed abstract class MyList[A] extends Iter[A] {
      def append(l: MyList[A]): MyList[A]
    }
    case class MyNil[A]() extends MyList[A] {
      def getValue = None
      def getNext = this
      def append(l: MyList[A]) = l
    }
    case class MyCons[A](hd: A, tl: MyList[A]) extends MyList[A] {
      def getValue = Some(hd)
      def getNext = tl
      def append(l: MyList[A]) = MyCons(hd, tl.append(l))
    }

    val t1 = MyCons(3, MyCons(4, MyNil()))
    println(sumElements(t1))

    class IntCounter(n: Int) extends Iter[Int] {
      def getValue = if (n > 0) Some(n) else None
      def getNext = new IntCounter(n - 1)
    }

    println(sumElements(new IntCounter(10)))

    abstract class Iterable[A] {
      def iter: Iter[A]
    }

    sealed abstract class MyTree[A] extends Iterable[A] {
      def iter: MyList[A]
    }
    case class Leaf[A]() extends MyTree[A] {
      def iter = MyNil[A]()
    }
    case class Node[A](v: A, l: MyTree[A], r: MyTree[A]) extends MyTree[A] {
      def iter = MyCons[A](v, l.iter.append(r.iter))
    }

    def sumElementsGen(xs: Iterable[Int]): Int =
      sumElements(xs.iter)

    class ListIter[A](val l: List[A]) extends Iter[A] {
      def getValue = l.headOption
      def getNext = ListIter(l.tail)
    }

    object ListIter {
      implicit def apply[A](l: List[A]): ListIter[A] = new ListIter[A](l)
    }
    
    println(sumElementsGen(ListIter(List(1,2,3))))
  }
}
