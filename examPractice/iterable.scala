object Main {
  def main(args: Array[String]): Unit = {
    abstract class Iter[A] extends Iterable[A] {
      def getValue: Option[A]
      def getNext: Iter[A]
      def iter = this
    }

    abstract class Iterable[A] {
      def iter: Iter[A]
    }

    sealed abstract class MyList[A] extends Iter[A] {
      def append(list: MyList[A]): MyList[A]
    }
    case class MyNil[A]() extends MyList[A] {
      def getValue = None
      def getNext = this
      def append(list: MyList[A]): MyList[A] = list
    }
    case class MyCons[A](head: A, tail: MyList[A]) extends MyList[A] {
      def getValue = Some(head)
      def getNext = tail
      def append(list: MyList[A]): MyList[A] = MyCons(head, tail.append(list))
    }

    sealed abstract class MyTree[A] extends Iterable[A] {
      def iter: MyList[A]
    }
    case class Empty[A]() extends MyTree[A] {
      def iter = MyNil()
    }
    case class Node[A](value: A, 
                      left: MyTree[A] = Empty(), 
                      right: MyTree[A] = Empty()) extends MyTree[A] {
      val iter = MyCons(value, left.iter.append(right.iter))
    }

    def sumElements(xs: Iter[Int]): Int = xs.getValue match {
      case None => 0
      case Some(x) => x + sumElements(xs.getNext)
    }
    def sumElementsGen(xs: Iterable[Int]): Int = 
      sumElements(xs.iter)

    val t : MyTree[Int] =
        Node(3, Node(4,Node(2,Empty(),Empty()),
            Node(3,Empty(),Empty())),
            Node(5,Empty(),Empty()))

    println(sumElementsGen(t))

    val lst : MyList[Int] = MyCons(3, MyCons(4, MyCons(2,MyNil())))
    println(sumElementsGen(lst))
  }
}
